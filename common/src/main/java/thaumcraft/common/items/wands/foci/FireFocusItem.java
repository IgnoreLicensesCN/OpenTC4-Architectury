package thaumcraft.common.items.wands.foci;

import com.google.common.collect.MapMaker;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.projectile.firefocus.EmberEntity;
import thaumcraft.common.entities.projectile.firefocus.ExplosiveOrbEntity;
import thaumcraft.common.items.wands.WandCooldownManager;
import thaumcraft.common.items.wands.render.waveanimations.AbstractWandWaveAnimation;
import thaumcraft.common.items.wands.render.waveanimations.ThaumcraftWandWaveAnimations;

import java.util.List;
import java.util.Map;

import static thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes.*;

public class FireFocusItem extends BasicFocusItem{

    public FireFocusItem(Properties properties) {
        super(properties);
    }
    public FireFocusItem() {
        this(new Properties().stacksTo(1));
    }

    public static final @Unmodifiable CentiVisList<Aspect> BASE_COST = UnmodifiableCentiVisList.of(
            Aspects.FIRE,10
    );
    public static final @Unmodifiable CentiVisList<Aspect> BEAM_COST = UnmodifiableCentiVisList.of(
            Aspects.FIRE,10,
            Aspects.ORDER,3
    );
    public static final @Unmodifiable CentiVisList<Aspect> BALL_COST = UnmodifiableCentiVisList.of(
            Aspects.FIRE,66,
            Aspects.ENTROPY,33
    );


    public static final List<FocusUpgradeType> RANK_0_UPGRADES =
            List.of(FRUGAL, POTENCY);

    public static final List<FocusUpgradeType> RANK_1_UPGRADES =
            List.of(FRUGAL, POTENCY, ALCHEMISTS_FIRE);

    public static final List<FocusUpgradeType> RANK_2_UPGRADES =
            List.of(FRUGAL, POTENCY, FIREBALL, FIRE_BEAM);

    public static final List<FocusUpgradeType> RANK_3_UPGRADES =
            List.of(FRUGAL, POTENCY, ALCHEMISTS_FIRE);

    public static final List<FocusUpgradeType> RANK_4_UPGRADES =
            List.of(FRUGAL, POTENCY);

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

    @Override
    public AbstractWandWaveAnimation getWaveAnimation(ItemStack focusStack,ItemStack wandStack) {
        var upgrades = getAppliedFocusUpgrades(focusStack);
        return isUpgradedWith(focusStack,FIREBALL,upgrades) ? ThaumcraftWandWaveAnimations.WAVE:ThaumcraftWandWaveAnimations.CHARGE;
    }

    @Override
    public boolean isCentiVisCostPerTick(ItemStack focusStack, @Nullable ItemStack wandStack) {
        return getFocusUpgradesWithWandModifiers(focusStack,wandStack).getInt(FIREBALL) <= 0;
    }

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        if (upgrades.getInt(FIREBALL) > 0) {
            return BALL_COST;
        }
        if (upgrades.getInt(FIRE_BEAM) > 0) {
            return BEAM_COST;
        }
        return BASE_COST;
    }

    @Override
    public int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack) {
        var upgrades = getAppliedFocusUpgradesWithOrder(focusStack);
        if (upgrades.contains(FIREBALL)) {
            return 20;
        }
        return 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandStack, ItemStack focusStack, Level level, LivingEntity user, InteractionHand interactionHand) {
        var cooldownManager = WandCooldownManager.getFromLiving(user);
        if (cooldownManager == null) {
            return InteractionResultHolder.pass(wandStack);
        }
        var wandItem = wandStack.getItem();
        if (wandItem instanceof ICentiVisContainerItem<?> centiVisContainerItemNotCasted) {
            ICentiVisContainerItem<Aspect> centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerItemNotCasted;
            var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);
            if (upgrades.getInt(FIREBALL) <= 0) {
                user.startUsingItem(interactionHand);
                cooldownManager.setCooldown(user,-1);
                return InteractionResultHolder.sidedSuccess(wandStack,level.isClientSide);
            }
            if (centiVisContainer.consumeAllCentiVis(
                    wandStack,
                    user,
                    this.getCentiVisCost(focusStack,upgrades),
                    !level.isClientSide,
                    ThaumcraftWandConsumptionTypes.FOCUS,
                    !level.isClientSide)
            ) {
                if (!level.isClientSide) {
                    ExplosiveOrbEntity orb = new ExplosiveOrbEntity(user,level);
                    orb.strength += upgrades.getInt(POTENCY) * 0.4F;
                    orb.onFire = upgrades.getInt(ALCHEMISTS_FIRE) > 0;
                    level.addFreshEntity(orb);
                    level.playSound(null,user.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS);
                }
                user.swing(interactionHand);
            }
        }
        return InteractionResultHolder.pass(wandStack);
    }

    Map<LivingEntity,Integer> soundTicks = new MapMaker().weakKeys().makeMap();

    @Override
    public void onUsingFocusTick(ItemStack wandStack, ItemStack focusStack, LivingEntity user, int count) {
        if (!checkAndSetCooldown(focusStack,wandStack,user)) {
            user.stopUsingItem();
            return;
        }

        if (!(wandStack.getItem() instanceof ICentiVisContainerItem<?> centiVisContainerItemNotCasted)) {
            user.stopUsingItem();
            return;
        }
        var centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerItemNotCasted;
        var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);
        var cost = getCentiVisCost(focusStack,upgrades);
        if (!centiVisContainer.consumeAllCentiVis(wandStack,user,cost,true,ThaumcraftWandConsumptionTypes.FOCUS,!user.level().isClientSide)) {
            user.stopUsingItem();
            return;
        }
        var level = user.level();
        if (!level.isClientSide) {
            if (soundTicks.getOrDefault(user, 0) < user.tickCount) {
                soundTicks.put(user, user.tickCount + 5);
                level.playSound(null,user.blockPosition(), ThaumcraftSounds.FIRE_LOOP, SoundSource.PLAYERS, 0.33F, 2.0F);
            }
            boolean fireBeamUpgraded = upgrades.getInt(FIRE_BEAM) > 0;
            float scatter = fireBeamUpgraded ? 0.25F : 15.0F;
            int potency = upgrades.getInt(POTENCY);
            for(int a = 0; a < 2 + potency; ++a) {
                EmberEntity orb = new EmberEntity(user,user.level(), scatter);
                orb.damage = (2 + potency);
                if (fireBeamUpgraded) {
                    orb.damage += 0.5F;
                    orb.damage *= 1.5F;
                    orb.duration = 30;
                }

                orb.firey = upgrades.getInt(ALCHEMISTS_FIRE);
                orb.setPos(orb.position().add(orb.getDeltaMovement()));
                level.addFreshEntity(orb);
            }
        }
    }
}
