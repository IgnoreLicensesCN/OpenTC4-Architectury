package thaumcraft.api.crafting.infusion;

import com.linearity.colorannotation.annotation.RGBColor;
import com.linearity.opentc4.recipeclean.itemmatch.EnchantableMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.EnchantableResultMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.utils.vanilla1710.Vanilla1710Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectView;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.network.fx.PacketFXInfusionSourceS2C;
import thaumcraft.common.lib.resourcelocations.InfusionRecipeResourceLocation;

import java.util.*;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static com.linearity.opentc4.utils.consts.EntityTypeTests.PLAYER_TEST;


public class SimpleInfusionEnchantmentRecipe extends InfusionRecipe {

    public final AspectList<Aspect> basicCostAspects;
    public final @Unmodifiable List<RecipeItemMatcher> components;
    public final Enchantment enchantment;
    public final int recipeXP;
    public final int instability;
    public final ResearchItem research;
    public final EnchantableMatcher matcher;
    public final EnchantableResultMatcher resultMatcher;

    public SimpleInfusionEnchantmentRecipe(
            InfusionRecipeResourceLocation id,
            @Unmodifiable List<RecipeItemMatcher> components,
            Enchantment toEnchant,
            AspectList<Aspect> basicCostAspects,
            ResearchItem research,
            int instability,
            int recipeXP

    ) {
        super(id);
        this.basicCostAspects = basicCostAspects;
        this.research = research;
        this.instability = instability;
        this.enchantment = toEnchant;
        this.recipeXP = recipeXP;
        this.components = components;
        this.matcher = EnchantableMatcher.of(this.enchantment);
        this.resultMatcher = EnchantableResultMatcher.of(this.enchantment);
    }

    @Override
    public ResearchItem getResearch() {
        return research;
    }

    @Override
    public List<ItemStack> getExampleRecipeInput() {
        return matcher.getAvailableItemStackSample();
    }

    @Override
    public ItemStack getRecipeOutput(ItemStack input) {
        var result = input.copy();
        var enchantmentMap = new HashMap<>(EnchantmentHelper.getEnchantments(input));
        enchantmentMap.put(enchantment, EnchantmentHelper.getEnchantments(input).getOrDefault(enchantment,0) + 1);
        EnchantmentHelper.setEnchantments(enchantmentMap,result);
        return result;
    }

    @Override
    public AspectList<Aspect> getAspects(ItemStack input) {
        return getAspectsModified(input, this.basicCostAspects);
    }

    //    public SimpleInfusionEnchantmentRecipe(
//            InfusionRecipeResourceLocation id,
//            ResearchItem research,
//            Enchantment toApply,
//            AspectList<Aspect> basicCostAspects,
//            int inst,
//            RecipeItemMatcher[] recipe
//    ) {
//        super(
//                id,
//                research, itemStacks -> {
//                    var inCenter = itemStacks[itemStacks.length - 1];
//                    if (inCenter == null || inCenter.isEmpty()) {
//                        return ItemStack.EMPTY;
//                    }
//                    inCenter = inCenter.copy();
//                    if (!toApply.canEnchant(inCenter)) {
//                        return ItemStack.EMPTY;
//                    }
//                    var currentLevel = EnchantmentHelper.getItemEnchantmentLevel(toApply, inCenter);
//                    if (toApply.getMaxLevel() <= currentLevel) {
//                        return ItemStack.EMPTY;
//                    }
//                    EnchantmentHelper.setEnchantmentLevel(inCenter.getOrCreateTag(), currentLevel + 1);
//                    return inCenter;
//
//                },
//                inst,
//                basicCostAspects,
//                EnchantmentItemMatcher.of(toApply),
//                recipe,
//                EnchantmentItemMatcher.of(toApply)
//        );
//        this.enchantment = toApply;
//        this.components = recipe;
//        this.inputSampleArr = new ItemStack[components.length + 1];
//        this.inputSampleArr[components.length] = new ItemStack(Items.ENCHANTED_BOOK);
//        this.basicCostAspects = basicCostAspects;
//        this.instability = inst;
//        this.recipeXP = Math.max(1, toApply.getMinCost(1) / 3);
//    }

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     */
    public boolean matches(List<ItemStack> input, ItemStack central, Level world, Player player, BlockPos probablyInfusionMatrixPos) {
        if (!simpleMatch(input, central, player, research, components, matcher)) {
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
        return checkPlayersNearbyExperience(player, probablyInfusionMatrixPos, calcXP(central));
    }

    public boolean checkPlayersNearbyExperience(Player player, BlockPos probablyInfusionMatrixPos,int toCost) {
        var level = player.level();

        List<Player> targets =
                level.getEntities(
                        PLAYER_TEST,
                        new AABB(player.blockPosition()).inflate(10.0F, 10.0F, 10.0F),
                        _ignored -> true
                );
        int levelRemaining = toCost;
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

    public void costPlayersNearbyExperience(Player player, BlockPos probablyInfusionMatrixPos,int toCost) {
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
        int levelRemaining = toCost;
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

    public AspectList<Aspect> getAspectsExample() {
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

    protected AspectList<Aspect> getAspectsModified(ItemStack recipeInput, AspectList<Aspect> basicCostAspects) {
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
    public void onInfusionStart(Level atLevel, BlockPos matrixPos, @Nullable String playerNameActivatedInfusion,ItemStack centralStack) {
        super.onInfusionStart(atLevel, matrixPos, playerNameActivatedInfusion,centralStack);
        Player player = platformUtils.getServer().getPlayerList().getPlayerByName(playerNameActivatedInfusion);
        if (player == null){
            player = atLevel.getNearestPlayer(
                    matrixPos.getX() + 0.5,
                    matrixPos.getY() + 0.5,
                    matrixPos.getZ() + 0.5,
                    10,false);
        }
        if (player != null) {
            costPlayersNearbyExperience(player, matrixPos,calcXP(centralStack));
        }
    }

    @Override
    public List<ItemStack> getRemainingStacks(List<ItemStack> inputsNotCenter){
        List<ItemStack> outStacks = new LinkedList<>();
        int counter = 0;
        for (var in: inputsNotCenter){
            var matcher = components.get(counter);
            if (!matcher.matches(in)){
                throw new IllegalArgumentException("should match recipe before getRemainingStacks!");
            }
            outStacks.add(matcher.getRemainingStack(in));
            counter += 1;
        }
        return outStacks;
    }

    @Override
    public boolean matchViaOutput(ItemStack res) {
        return resultMatcher.matches(res);
    }
}
