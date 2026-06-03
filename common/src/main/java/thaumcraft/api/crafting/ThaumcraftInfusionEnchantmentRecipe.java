package thaumcraft.api.crafting;

import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.recipeclean.itemmatch.EnchantmentItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import com.linearity.opentc4.recipeclean.recipewrapper.RecipeInAndOutSampler;
import com.linearity.opentc4.utils.vanilla1710.Vanilla1710Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectView;
import thaumcraft.api.crafting.interfaces.IInfusionAspectsModifiable;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.network.fx.PacketFXInfusionSourceS2C;
import thaumcraft.common.lib.resourcelocations.InfusionRecipeResourceLocation;

import java.util.*;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static com.linearity.opentc4.utils.EntityTypeTests.PLAYER_TEST;
import static com.linearity.opentc4.utils.IndexPicker.indexByTime;
import static com.linearity.opentc4.utils.IndexPicker.pickByTime;


public class ThaumcraftInfusionEnchantmentRecipe extends InfusionRecipe
        implements RecipeInAndOutSampler,
        IAspectCalculableRecipe,
        IInfusionAspectsModifiable {

    public final AspectList<Aspect> basicCostAspects;
    public final RecipeItemMatcher[] components;
    public final Enchantment enchantment;
    public final int recipeXP;
    public final int instability;
    private final ItemStack[] inputSampleArr;

    public ThaumcraftInfusionEnchantmentRecipe(
            InfusionRecipeResourceLocation id,
            ResearchItem research,
            Enchantment toApply,
            AspectList<Aspect> basicCostAspects,
            int inst,
            RecipeItemMatcher[] recipe
    ) {
        super(
                id,
                research, itemStacks -> {
                    var inCenter = itemStacks[itemStacks.length - 1];
                    if (inCenter == null || inCenter.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    inCenter = inCenter.copy();
                    if (!toApply.canEnchant(inCenter)) {
                        return ItemStack.EMPTY;
                    }
                    var currentLevel = EnchantmentHelper.getItemEnchantmentLevel(toApply, inCenter);
                    if (toApply.getMaxLevel() <= currentLevel) {
                        return ItemStack.EMPTY;
                    }
                    EnchantmentHelper.setEnchantmentLevel(inCenter.getOrCreateTag(), currentLevel + 1);
                    return inCenter;

                },
                inst,
                basicCostAspects,
                EnchantmentItemMatcher.of(toApply),
                recipe,
                EnchantmentItemMatcher.of(toApply)
        );
        this.enchantment = toApply;
        this.components = recipe;
        this.inputSampleArr = new ItemStack[components.length + 1];
        this.inputSampleArr[components.length] = new ItemStack(Items.ENCHANTED_BOOK);
        this.basicCostAspects = basicCostAspects;
        this.instability = inst;
        this.recipeXP = Math.max(1, toApply.getMinCost(1) / 3);
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     */
    public boolean matches(List<ItemStack> input, ItemStack central, Level world, Player player, BlockPos probablyInfusionMatrixPos) {
        if (!super.matches(input, central, world, player, probablyInfusionMatrixPos)) {
            return false;
        }

        if (!enchantment.canEnchant(central) || !Vanilla1710Utils.isItemTool(central)) {
            return false;
        }

        Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(central);
        for (Map.Entry<Enchantment, Integer> enchantmentLevelEntry : map1.entrySet()) {
            int lvl = enchantmentLevelEntry.getValue();
            Enchantment ench = enchantmentLevelEntry.getKey();
            boolean enchantmentEqualFlag = Objects.equals(ench, enchantment);
            if (enchantmentEqualFlag &&
                    lvl >= ench.getMaxLevel()) {
                return false;
            }
            if (!enchantmentEqualFlag &&
                    (!enchantment.isCompatibleWith(ench) ||
                            !ench.isCompatibleWith(enchantment))) {
                return false;
            }
        }
        return checkPlayersNearbyExperience(player, probablyInfusionMatrixPos);
    }

    public boolean checkPlayersNearbyExperience(Player player, BlockPos probablyInfusionMatrixPos) {
        var level = player.level();

        List<Player> targets =
                level.getEntities(
                        PLAYER_TEST,
                        new AABB(player.blockPosition()).inflate(10.0F, 10.0F, 10.0F),
                        _ignored -> true
                );
        int levelRemaining = this.recipeXP;
        if (!targets.isEmpty()) {
            for (Player playerBeingTokenXP : targets) {
                //taking XP from players(yes multiple players!)
                if (playerBeingTokenXP.experienceLevel > 0) {
                    levelRemaining -= Math.min(playerBeingTokenXP.experienceLevel, levelRemaining);
                    if (levelRemaining == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void costPlayersNearbyExperience(Player player, BlockPos probablyInfusionMatrixPos) {
        var level = player.level();
        if (!(level instanceof ServerLevel serverLevel)){
            throw new IllegalCallerException("should be called in serverLevel");
        }

        List<Player> targets =
                level.getEntities(
                        PLAYER_TEST,
                        new AABB(player.blockPosition()).inflate(10.0F, 10.0F, 10.0F),
                        _ignored -> true
                );
        int levelRemaining = this.recipeXP;
        if (!targets.isEmpty()) {
            for (Player playerBeingTokenXP : targets) {
                //taking XP from players(yes multiple players!)
                if (playerBeingTokenXP.experienceLevel > 0) {
                    levelRemaining -= Math.min(playerBeingTokenXP.experienceLevel, levelRemaining);


                    playerBeingTokenXP.giveExperienceLevels(-playerBeingTokenXP.experienceLevel);
                    playerBeingTokenXP.hurt(level.damageSources().magic(), level.random.nextInt(2));
                    @RGBColor int pickColor = playerBeingTokenXP.getTeamColor();
                    if (pickColor == 0xffffff) {
                        pickColor =
                                ((level.random.nextInt(64) + 32) << 16)
                                        + ((level.random.nextInt(64) + 32) << 8)
                                        + (level.random.nextInt(64) + 32);
                    }
                    new PacketFXInfusionSourceS2C(
                            probablyInfusionMatrixPos.getX(), probablyInfusionMatrixPos.getY(), probablyInfusionMatrixPos.getZ(),
                            (byte) 0, (byte) 0, (byte) 0,
                            pickColor).sendToAllAround(serverLevel, player.blockPosition(), 32 * 32);
                    level.playSound(playerBeingTokenXP,
                            playerBeingTokenXP.blockPosition(),
                            SoundEvents.LAVA_EXTINGUISH,
                            SoundSource.PLAYERS,
                            1.0F,
                            2.0F + level.random.nextFloat() * 0.4F);
                    if (levelRemaining == 0) {
                        return;
                    }

                }
            }
        }
    }


    public Enchantment getEnchantment() {
        return enchantment;

    }

    public AspectList<Aspect> getAspects() {
        return basicCostAspects;
    }


    public int getInstability(ItemStack recipeInput) {
        int i = 0;
        Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(recipeInput);
        for (Map.Entry<Enchantment, Integer> entry : map1.entrySet()) {
            Enchantment ench = entry.getKey();
            int lvl = entry.getValue();
            i += lvl;
        }
        return (i / 2) + instability;
    }

    public int calcXP(ItemStack recipeInput) {
        return recipeXP * (1 + EnchantmentHelper.getEnchantments(recipeInput).get(enchantment));
    }

    public AspectList<Aspect> getAspectsModified(ItemStack recipeInput, AspectList<Aspect> basicCostAspects) {
        float mod = EnchantmentHelper.getEnchantments(recipeInput).get(enchantment);
        Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(recipeInput);
        for (Map.Entry<Enchantment, Integer> entry : map1.entrySet()) {
            Enchantment ench = entry.getKey();
            int lvl = entry.getValue();
            if (ench != enchantment)
                mod += lvl * .1f;
        }
        AspectList<Aspect> aspsResult = new LinkedHashAspectList<>(basicCostAspects.size(), 1);
        final float finalMod = mod;
        basicCostAspects.forEach((asp, amount) -> aspsResult.put(asp, (int) (amount * finalMod)));
        return new UnmodifiableAspectView<>(aspsResult);
    }

    @Override
    public ItemStack[] getInputSample() {
        //setting central a book
        int lvlBefore = indexByTime(enchantment.getMaxLevel() - 1);
        if (lvlBefore == 0) {
            enchantmentMapBefore.clear();
        } else {
            enchantmentMapBefore.put(enchantment, lvlBefore);
        }
        EnchantmentHelper.setEnchantments(
                enchantmentMapBefore
                , inputSampleArr[inputSampleArr.length - 1]
        );


        for (int i = 0; i < inputSampleArr.length; i++) {
            inputSampleArr[i] = pickByTime(components[i].getAvailableItemStackSample());
        }
        return inputSampleArr;
    }

    private final ItemStack[] enchantmentSampleBook = new ItemStack[]{new ItemStack(Items.ENCHANTED_BOOK)};
    private final Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
    private final Map<Enchantment, Integer> enchantmentMapBefore = new HashMap<>();

    @Override
    public ItemStack[] getOutputSample(ItemStack[] inputSample) {
        int lvlBefore = indexByTime(enchantment.getMaxLevel() - 1);

        enchantmentMap.put(enchantment, lvlBefore + 1);
        EnchantmentHelper.setEnchantments(
                enchantmentMap
                , enchantmentSampleBook[0]
        );
        return enchantmentSampleBook;
    }


    @Override
    public void onInfusionStart(Level atLevel, BlockPos matrixPos, @Nullable String playerNameActivatedInfusion) {
        super.onInfusionStart(atLevel, matrixPos, playerNameActivatedInfusion);
        Player player = platformUtils.getServer().getPlayerList().getPlayerByName(playerNameActivatedInfusion);
        if (player == null){
            player = atLevel.getNearestPlayer(
                    matrixPos.getX() + 0.5,
                    matrixPos.getY() + 0.5,
                    matrixPos.getZ() + 0.5,
                    10,false);
        }
        if (player != null) {
            costPlayersNearbyExperience(player, matrixPos);
        }
    }
}
