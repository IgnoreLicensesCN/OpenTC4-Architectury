package thaumcraft.common.items.wands.foci;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.client.fx.migrated.bolt.FXLightningBolt;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.projectile.shockfocus.ShockOrbEntity;
import thaumcraft.common.items.wands.WandCooldownManager;
import thaumcraft.common.items.wands.render.waveanimations.AbstractWandWaveAnimation;
import thaumcraft.common.items.wands.render.waveanimations.ThaumcraftWandWaveAnimations;
import thaumcraft.common.lib.network.fx.PacketFXZapS2C;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.*;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.LIVING_TEST;
import static thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes.*;

public class ShockFocusItem extends BasicFocusItem{


    public ShockFocusItem(Properties properties) {
        super(properties);
    }
    public ShockFocusItem() {
        this(new Properties().stacksTo(1));
    }

    @Override
    public int getActivationCooldownTicks(ItemStack focusStack, @NotNull ItemStack wandStack) {
        var upgrades = getAppliedFocusUpgrades(focusStack);
        return upgrades.getInt(CHAIN_LIGHTING) > 0 ? 10:
                upgrades.getInt(EARTH_SHOCK) > 0 ? 20:
                        5
                ;
    }

    @Override
    public AbstractWandWaveAnimation getWaveAnimation(ItemStack focusStack,ItemStack wandStack) {
        var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);
        return isUpgradedWith(focusStack,EARTH_SHOCK,upgrades)? ThaumcraftWandWaveAnimations.WAVE:ThaumcraftWandWaveAnimations.CHARGE;
    }

    public static final List<FocusUpgradeType> RANK_0_UPGRADES =
            List.of(FRUGAL, POTENCY);
    public static final List<FocusUpgradeType> RANK_1_UPGRADES =
            List.of(FRUGAL, POTENCY);
    public static final List<FocusUpgradeType> RANK_2_UPGRADES =
            List.of(FRUGAL, POTENCY, CHAIN_LIGHTING, EARTH_SHOCK);
    public static final List<FocusUpgradeType> RANK_3_UPGRADES =
            List.of(FRUGAL, FRUGAL, ENLARGE);
    public static final List<FocusUpgradeType> RANK_4_UPGRADES =
            List.of(FRUGAL, POTENCY, ENLARGE);
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
    public boolean canApplyUpgrade(ItemStack focusStack, LivingEntity upgradeApplier, FocusUpgradeType type) {
        if (type == ENLARGE){
            var upgrades = getAppliedFocusUpgrades(focusStack);
            if (!(upgrades.getInt(CHAIN_LIGHTING) > 0 || upgrades.getInt(ENLARGE) > 0)){
                return false;
            }
        }
        return super.canApplyUpgrade(focusStack, upgradeApplier, type);
    }

    public static final CentiVisList<Aspect> BASE_COST = UnmodifiableCentiVisList.of(
            Aspects.AIR, 25
    );
    public static final CentiVisList<Aspect> CHAIN_COST = UnmodifiableCentiVisList.of(
            Aspects.AIR, 40,
            Aspects.WATER, 10
    );
    public static final CentiVisList<Aspect> EARTH_SHOCK_COST = UnmodifiableCentiVisList.of(
            Aspects.AIR, 75,
            Aspects.EARTH, 25
    );

    @Override
    public CentiVisList<Aspect> getCentiVisCost(ItemStack focusStack, Object2IntMap<FocusUpgradeType> upgrades) {
        if (upgrades.getInt(CHAIN_LIGHTING) > 0){
            return CHAIN_COST;
        }
        if (upgrades.getInt(EARTH_SHOCK) > 0){
            return EARTH_SHOCK_COST;
        }
        return BASE_COST;
    }

    @Override
    public InteractionResultHolder<ItemStack> onFocusRightClick(ItemStack wandStack, ItemStack focusStack, Level level, LivingEntity user, InteractionHand interactionHand) {
        var cooldownManager = WandCooldownManager.getFromLiving(user);
        if (cooldownManager == null){
            return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
        }

        if (wandStack.getItem() instanceof ICentiVisContainerItem<?> centiVisContainerItemNotCasted) {
            var centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerItemNotCasted;
            var upgrades = getAppliedFocusUpgrades(focusStack);
            if (upgrades.getInt(EARTH_SHOCK) > 0){
                if (centiVisContainer.consumeAllCentiVis(
                        wandStack,
                        user,
                        getCentiVisCost(focusStack,upgrades),
                        !level.isClientSide,
                        ThaumcraftWandConsumptionTypes.CONSUMPTION_FOCUS,
                        !level.isClientSide
                )){
                    if (!level.isClientSide){
                        var orb = new ShockOrbEntity(user,level);
                        orb.area += upgrades.getInt(ENLARGE) * 2;
                        orb.damage = (int)((double)orb.damage + upgrades.getInt(POTENCY) * 1.33);
                        orb.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
                        level.addFreshEntity(orb);
                        level.playSound(
                                orb,
                                user.blockPosition(),
                                ThaumcraftSounds.ZAP,
                                SoundSource.PLAYERS,
                                1.0F,
                                1.0F + (level.getRandom().nextFloat()) * 0.4F - 0.2F
                        );
                    }
                    user.swing(interactionHand);
                    return InteractionResultHolder.sidedSuccess(wandStack,level.isClientSide);
                }
            }else {
                cooldownManager.setCooldown(user,-1);
                user.startUsingItem(interactionHand);
                return InteractionResultHolder.sidedSuccess(wandStack,level.isClientSide);
            }
        }
        return super.onFocusRightClick(wandStack, focusStack, level, user, interactionHand);
    }

    @Override
    public void onUsingFocusTick(ItemStack wandStack, ItemStack focusStack, LivingEntity user, int count) {
        if (!checkAndSetCooldown(focusStack,wandStack,user)){
            user.stopUsingItem();
            return;
        }
        if (!(wandStack.getItem() instanceof ICentiVisContainerItem<?> centiVisContainerItemNotCasted)) {
            user.stopUsingItem();
            return;
        }
        var level = user.level();
        var centiVisContainer = (ICentiVisContainerItem<Aspect>) centiVisContainerItemNotCasted;
        var upgrades = getFocusUpgradesWithWandModifiers(focusStack,wandStack);
        if (!centiVisContainer.consumeAllCentiVis(
                wandStack,
                user,
                getCentiVisCost(focusStack,upgrades),
                !level.isClientSide,
                ThaumcraftWandConsumptionTypes.CONSUMPTION_FOCUS,
                !level.isClientSide
        )){
            user.stopUsingItem();
            return;
        }
        var pointedEntityHitResult = EntityUtils.getPointedEntity(user, 20.0F);
        if (level.isClientSide){
            playLightingAndSparkleEffect(user, pointedEntityHitResult);
        }else {
            var pos = user.blockPosition();
            level.playSound(user,pos,ThaumcraftSounds.SHOCK,SoundSource.PLAYERS, 0.25F, 1.0F);
            if (pointedEntityHitResult == null){
                return;
            }
            if (pointedEntityHitResult.getEntity() instanceof LivingEntity victim
            ) {
                int chainLightingTargetCount = upgrades.getInt(CHAIN_LIGHTING) * 2;
                var dmgSource = user instanceof Player player
                        ?level.damageSources().playerAttack(player)
                        : level.damageSources().mobAttack(user);
                victim.hurt(
                        dmgSource
                        , (float)((chainLightingTargetCount > 0 ? 6 : 4) + upgrades.getInt(POTENCY))
                );
                if (chainLightingTargetCount > 0) {
                    chainLightingTargetCount += upgrades.getInt(ENLARGE) * 2;
                    final LivingEntity[] center = {victim};
                    Set<LivingEntity> iteratedTarget = new HashSet<>(chainLightingTargetCount + 2);
                    iteratedTarget.add(center[0]);
                    iteratedTarget.add(user);

                    for (int _lightTime = 0; _lightTime < chainLightingTargetCount; ++_lightTime) {
                        AABB box = new AABB(center[0].blockPosition()).inflate(8);
                        var entities = level.getEntities(
                                LIVING_TEST,box,_l -> !iteratedTarget.contains(_l)
                        );

                        if (entities.isEmpty()){
                            break;
                        }
                        entities.stream()
                                .min(Comparator.comparingDouble(a -> a.distanceToSqr(center[0])))
                                .ifPresent(closest -> {
                                    if (level instanceof ServerLevel serverLevel) {
                                        new PacketFXZapS2C(center[0].getId(), closest.getId()).sendToAllAround(
                                                serverLevel,
                                                center[0].blockPosition(),
                                                4096
                                        );
                                    }
                                    iteratedTarget.add(closest);
                                    closest.hurt(
                                            dmgSource
                                            , 4 + upgrades.getInt(POTENCY)
                                    );
                                    center[0] = closest;
                                });
                    }
                }
            }
        }
    }
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.CLIENT)
    public static void playLightingAndSparkleEffect(LivingEntity user, EntityHitResult pointedEntityHitResult) {
        var hitResult = user.pick(20, 0, false);
        if (pointedEntityHitResult == null) {
            var pos = hitResult.getLocation();
            for(int a = 0; a < 5; ++a) {
                ClientFXUtils.sparkle(
                        (float)pos.x + (user.level().random.nextFloat()) * 0.6F-0.3F,
                        (float)pos.y + (user.level().random.nextFloat()) * 0.6F-0.3F,
                        (float)pos.z + (user.level().random.nextFloat()) * 0.6F-0.3F,
                        2.0F + user.level().random.nextFloat(),
                        2,
                        0.05F + user.level().random.nextFloat() * 0.05F);
            }
            playLightingEffect(user.level(), user, pos.x,pos.y,pos.z, true);
        }else{
            var entity = pointedEntityHitResult.getEntity();
            var px = entity.getX();
            var py = entity.getBoundingBox().minY + (double)(entity.getBbHeight() / 2.0F);
            var pz = entity.getZ();

            for(int a = 0; a < 5; ++a) {
                ClientFXUtils.sparkle(
                        (float)px + (user.level().random.nextFloat()) * 1.2F - 0.6F,
                        (float)py + (user.level().random.nextFloat()) * 1.2F - 0.6F,
                        (float)pz + (user.level().random.nextFloat()) * 1.2F - 0.6F,
                        2.0F + user.level().random.nextFloat(),
                        2,
                        0.05F + user.level().random.nextFloat() * 0.05F
                );
            }
            playLightingEffect(user.level(), user, px, py, pz, true);
        }
    }
    @RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.CLIENT)
    public static void playLightingEffect(Level level, LivingEntity living, double xx, double yy, double zz, boolean offset) {
        if (!(level instanceof ClientLevel clientLevel)) {return;}
        var eyePos = living.getEyePosition();
        var lookVec = living.getLookAngle();
        var start = eyePos.add(
                lookVec.scale(0.36)
        );

        FXLightningBolt bolt = new FXLightningBolt(
                clientLevel,
                start.x,start.y,start.z,
                xx, yy, zz, level.getRandom().nextLong(), 6, 0.5F, 8);
        bolt.defaultFractal();
        bolt.setType(2);
        bolt.setWidth(0.125F);
        bolt.finalizeBolt();
    }
}
