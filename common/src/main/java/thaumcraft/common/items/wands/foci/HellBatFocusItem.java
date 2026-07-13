package thaumcraft.common.items.wands.foci;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.research.ThaumcraftResearches;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.projectile.hellbatfocus.FireBatEntity;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.List;

import static thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes.CONSUMPTION_FOCUS;
import static thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes.*;

public class HellBatFocusItem extends BasicFocusItem {
    public HellBatFocusItem(Properties props) {
        super(props);
    }

    public HellBatFocusItem() {
        this(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandStack, ItemStack focusStack, Level level, LivingEntity user, InteractionHand interactionHand) {
        if (!(wandStack.getItem() instanceof ICentiVisContainerItem<?> centiVisContainerItemNotCasted)){
            return InteractionResultHolder.pass(wandStack);
        }
        var centiVisContainer = (ICentiVisContainerItem<Aspect>)centiVisContainerItemNotCasted;

        var entityHitResult = EntityUtils.getPointedEntity(
                user,
                32,
                e -> !e.isSpectator()
                && !(e instanceof OwnableEntity ownable && ownable.getOwnerUUID() == user.getUUID())
                && e instanceof LivingEntity
        );
        if (entityHitResult != null) {
            if (entityHitResult.getEntity() instanceof LivingEntity target){
                if (!checkAndSetCooldown(focusStack,wandStack,user)){
                    return InteractionResultHolder.pass(wandStack);
                }
                var upgrades = getFocusUpgradesWithWandModifiers(
                        focusStack,
                        wandStack
                );
                if (!centiVisContainer.consumeAllCentiVis(
                        wandStack,
                        user,
                        getCentiVisCost(focusStack,upgrades),
                        !level.isClientSide,
                        CONSUMPTION_FOCUS,
                        !level.isClientSide)){
                    return InteractionResultHolder.pass(wandStack);
                }
                user.swing(interactionHand);
                var spawnAtPos = user.position().add(user.getLookAngle().scale(0.5));
                FireBatEntity firebat = new FireBatEntity(level);
                firebat.setPos(spawnAtPos.add(0,firebat.getBbHeight(),0));
                firebat.setXRot(user.getXRot());
                firebat.setYRot(0);
                firebat.setTarget(target);
                firebat.setDamageBonus(upgrades.getInt(POTENCY));
                firebat.setSummoned(true);
                firebat.setOwnerUUID(user.getUUID());
                firebat.setResting(false);
                if (upgrades.getInt(DEVIL_BATS) > 0){
                    firebat.setDevil(true);
                }
                if (upgrades.getInt(BAT_BOMBS) > 0){
                    firebat.setExplosive(true);
                }
                if (upgrades.getInt(VAMPIRE_BATS) > 0){
                    firebat.setVampire(true);
                }
                if (!level.isClientSide){
                    if (level.addFreshEntity(firebat)){
                        for (int l2 = 0; l2 < 20; ++l2)
                        {
                            double particleX = spawnAtPos.x - 0.5 + level.random.nextFloat()*2;
                            double particleY = spawnAtPos.y - 0.5 + level.random.nextFloat()*2;
                            double particleZ = spawnAtPos.z - 0.5 + level.random.nextFloat()*2;

                            level.addParticle(
                                    ParticleTypes.SMOKE,
                                    particleX,particleY,particleZ,
                                    0.0, 0.0, 0.0
                            );
                            level.addParticle(
                                    ParticleTypes.FLAME,
                                    particleX,particleY,particleZ,
                                    0.0, 0.0, 0.0
                            );
                        }
                        level.playSound(
                                user,
                                user.blockPosition(),
                                ThaumcraftSounds.ICE,
                                SoundSource.NEUTRAL,
                                0.1F,
                                0.8F + level.getRandom().nextFloat() * 0.1F
                        );
                        return InteractionResultHolder.success(wandStack);
                    }
                }else {
                    return InteractionResultHolder.pass(wandStack);
                }
            }
            level.playSound(
                    user,
                    user.blockPosition(),
                    ThaumcraftSounds.WAND_FAIL,
                    SoundSource.NEUTRAL,
                    0.1F,
                    0.8F + level.getRandom().nextFloat() * 0.1F
            );
        }

        return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
    }

    public static final List<FocusUpgradeType> RANK_0_UPGRADES =
            List.of(FRUGAL, POTENCY);
    public static final List<FocusUpgradeType> RANK_1_UPGRADES =
            List.of(FRUGAL, POTENCY);
    public static final List<FocusUpgradeType> RANK_2_UPGRADES =
            List.of(FRUGAL, POTENCY, BAT_BOMBS, DEVIL_BATS);
    public static final List<FocusUpgradeType> RANK_3_UPGRADES =
            List.of(FRUGAL, FRUGAL);
    public static final List<FocusUpgradeType> RANK_4_UPGRADES =
            List.of(FRUGAL, POTENCY, VAMPIRE_BATS);

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
            Aspects.FIRE, 200,
            Aspects.ENTROPY, 100,
            Aspects.AIR, 100
    );
    public static final CentiVisList<Aspect> BOMB_COST = UnmodifiableCentiVisList.of(
            Aspects.FIRE, 100,
            Aspects.ENTROPY, 200,
            Aspects.AIR, 100
    );
    public static final CentiVisList<Aspect> DEVIL_COST = UnmodifiableCentiVisList.of(
            Aspects.FIRE, 100,
            Aspects.ENTROPY, 100,
            Aspects.AIR, 100,
            Aspects.EARTH, 100
    );

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        if (isUpgradedWith(focusStack,BAT_BOMBS,upgrades)) {
            return BOMB_COST;
        }
        if (isUpgradedWith(focusStack,DEVIL_BATS,upgrades)) {
            return DEVIL_COST;
        }
        return BASE_COST;
    }

    @Override
    public boolean canApplyUpgrade(ItemStack focusStack, LivingEntity upgradeApplier, FocusUpgradeType type) {
        if (type == VAMPIRE_BATS) {
            return super.canApplyUpgrade(focusStack, upgradeApplier, type)
                    && ThaumcraftResearches.VAMPIRE_BATS.isLivingEntityCompletedResearch(upgradeApplier);
        }
        return super.canApplyUpgrade(focusStack, upgradeApplier, type);
    }

    @Override
    public int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack) {
        return 20;
    }

}
