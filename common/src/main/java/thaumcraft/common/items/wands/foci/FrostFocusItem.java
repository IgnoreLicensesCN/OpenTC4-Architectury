package thaumcraft.common.items.wands.foci;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.projectile.frostfocus.FrostShardEntity;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;

import java.util.List;

import static thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes.*;

public class FrostFocusItem extends BasicFocusItem {
    public FrostFocusItem(Properties props) {
        super(props);
    }

    public FrostFocusItem() {
        this(new Properties().stacksTo(1));
    }

    public static final List<FocusUpgradeType> RANK_0_UPGRADES =
            List.of(FRUGAL, POTENCY, ALCHEMISTS_FROST);
    public static final List<FocusUpgradeType> RANK_1_UPGRADES =
            List.of(FRUGAL, POTENCY);
    public static final List<FocusUpgradeType> RANK_2_UPGRADES =
            List.of(FRUGAL, POTENCY, SCATTER_SHOT, ICE_BOULDER, ALCHEMISTS_FROST);
    public static final List<FocusUpgradeType> RANK_3_UPGRADES =
            List.of(FRUGAL, FRUGAL, ENLARGE);
    public static final List<FocusUpgradeType> RANK_4_UPGRADES =
            List.of(FRUGAL, POTENCY, ALCHEMISTS_FROST);

    @Override
    public @NotNull List<FocusUpgradeType> getPossibleUpgradesByRank(ItemStack focusStack, int rank) {
        if (focusStack == null) {return List.of();}
        if (rank == 0) return RANK_0_UPGRADES;
        if (rank == 1) return RANK_1_UPGRADES;
        if (rank == 2) return RANK_2_UPGRADES;
        if (rank == 3) return RANK_3_UPGRADES;
        if (rank == 4) return RANK_4_UPGRADES;
        return List.of();
    }
    public static final CentiVisList<Aspect> BASE_COST = UnmodifiableCentiVisList.of(
            Aspects.WATER, 5,
            Aspects.FIRE, 2,
            Aspects.ENTROPY, 2
    );
    public static final CentiVisList<Aspect> SCATTER_COST = UnmodifiableCentiVisList.of(
            Aspects.WATER, 20,
            Aspects.FIRE, 2,
            Aspects.ENTROPY, 2,
            Aspects.AIR, 5
    );
    public static final CentiVisList<Aspect> BOULDER_COST = UnmodifiableCentiVisList.of(
            Aspects.WATER, 20,
            Aspects.FIRE, 2,
            Aspects.ENTROPY, 2,
            Aspects.EARTH, 5
    );

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        if (isUpgradedWith(focusStack,SCATTER_SHOT,upgrades)) {
            return SCATTER_COST;
        }
        if (isUpgradedWith(focusStack,ICE_BOULDER,upgrades)) {
            return BOULDER_COST;
        }
        return BASE_COST;
    }

    @Override
    public int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack) {
        var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);
        if (isUpgradedWith(focusStack,SCATTER_SHOT,upgrades) || isUpgradedWith(focusStack,ICE_BOULDER,upgrades)) {
            return 10;
        }
        return 4;
    }

    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(
            ItemStack wandStack,
            ItemStack focusStack,
            Level level,
            LivingEntity user,
            InteractionHand interactionHand) {
        if (!(wandStack.getItem() instanceof ICentiVisContainerItem<?> centiVisContainerItemNotCasted)){
            return InteractionResultHolder.pass(wandStack);
        }
        var centiVisContainer = (ICentiVisContainerItem<Aspect>)centiVisContainerItemNotCasted;
        if (!checkAndSetCooldown(focusStack,wandStack,user)){
            return InteractionResultHolder.pass(wandStack);
        }
        var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);
        if (centiVisContainer.consumeAllCentiVis(
                wandStack,user,getCentiVisCost(focusStack,upgrades),
                true, ThaumcraftWandConsumptionTypes.FOCUS,!level.isClientSide
        )){
            int frosty = upgrades.getInt(ALCHEMISTS_FROST);
            int potency = upgrades.getInt(POTENCY);
            if (this.isUpgradedWith(focusStack,SCATTER_SHOT,upgrades)) {
                doScatterShot(level, user, potency, frosty);
            } else if (this.isUpgradedWith(focusStack,ICE_BOULDER,upgrades)) {
                doIceboulderShoot(level, user, potency, frosty);
            } else {
                doCommonShoot(level, user, potency, frosty);
            }
            level.playSound(user,user.blockPosition(), ThaumcraftSounds.ICE, SoundSource.BLOCKS, 0.4F, 1.0F + level.getRandom().nextFloat() * 0.1F);
        }
        user.swing(interactionHand);
        return InteractionResultHolder.sidedSuccess(wandStack,level.isClientSide);
    }

    protected void doCommonShoot(Level level, LivingEntity user, int potency, int frosty) {
        var shard = new FrostShardEntity(user, level);
        shard.damage = 3.0F + potency * 1.5F;
        shard.frosty = frosty;
        shard.shootFromRotation(user, user.getXRot(), user.getYRot(), 0,1.5F, 1);
        level.addFreshEntity(shard);
    }

    protected void doIceboulderShoot(Level level, LivingEntity user, int potency, int frosty) {
        var shard = new FrostShardEntity(user, level);
        shard.damage = 4 + potency * 2;
        shard.bounce = 0.8;
        shard.bounceLimit = 6;
        shard.frosty = frosty;
        shard.shootFromRotation(user, user.getXRot(), user.getYRot(), 0,1.5F, 1);
        level.addFreshEntity(shard);
    }

    protected void doScatterShot(Level level, LivingEntity user, int potency, int frosty) {
        for(int a = 0; a < 5 + potency * 2; ++a) {
            var shard = new FrostShardEntity(user, level);
            shard.damage = 1.0F;
            shard.fragile = true;
            shard.frosty = frosty;
            shard.shootFromRotation(user, user.getXRot(), user.getYRot(), 0,1.5F, 8);
            level.addFreshEntity(shard);
        }
    }

}
