package thaumcraft.common.entities.projectile;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.effects.ThaumcraftEffects;
import thaumcraft.common.lib.world.biomes.BiomeUtils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeLookups;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.LIVING_TEST;
import static thaumcraft.common.entities.ThaumcraftEntities.EntityTags.TAINTED;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.FIBROUS_TAINT;
import static thaumcraft.common.items.ThaumcraftItemInstances.TAINT_BOTTLE;

public class TaintBottleEntity extends ThrowableItemProjectile {
    public TaintBottleEntity(Level par1World) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINT_BOTTLE(), par1World);
    }

    public TaintBottleEntity(EntityType<TaintBottleEntity> type, Level par1World) {
        super(type, par1World);
    }

    public TaintBottleEntity(LivingEntity par2EntityLiving, Level par1World) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINT_BOTTLE(), par2EntityLiving, par1World);
    }

    public TaintBottleEntity(EntityType<TaintBottleEntity> type, LivingEntity par2EntityLiving, Level par1World) {
        super(type, par2EntityLiving, par1World);
    }

    public TaintBottleEntity(Level par1World, double par2, double par4, double par6) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINT_BOTTLE(), par6, par2, par4, par1World);
    }

    public TaintBottleEntity(EntityType<TaintBottleEntity> type, double par2, double par4, double par6, Level par1World) {
        super(type, par6, par2, par4, par1World);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return TAINT_BOTTLE();
    }

    @Override
    protected float getGravity() {
        return 0.05F;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (!level().isClientSide()) {
            if (level() instanceof ServerLevel serverLevel) {
                var atPos = blockPosition();
                var living = serverLevel.getEntities(LIVING_TEST,new AABB(atPos).inflate(5),l -> !l.getType().is(TAINTED));
                living.forEach(l -> l.addEffect(new MobEffectInstance(ThaumcraftEffects.ThaumcraftEffectTypeInstances.FLUX_TAINT(),100,0)));


                var biomeToSet = ThaumcraftBiomeLookups.biomeHolderForLevel(serverLevel,ThaumcraftBiomeIDs.TAINT_KEY);
                var stateToSet = FIBROUS_TAINT().defaultBlockState();

                for (int _times = 0;_times < 10;_times++){
                    var xOffset = random.nextInt(11)-5;
                    var zOffset = random.nextInt(11)-5;
                    var pickPos = atPos.offset(xOffset,0,zOffset);
                    var pickPosBelow = pickPos.below();
                    if (serverLevel.random.nextBoolean() && !serverLevel.getBiome(pickPos).is(ThaumcraftBiomeIDs.TAINT_KEY)){
                        BiomeUtils.setPosTaint(serverLevel,pickPos,biomeToSet);
                        if (serverLevel.getBlockState(pickPosBelow).isCollisionShapeFullBlock(serverLevel,pickPosBelow)){
                            var pickState = serverLevel.getBlockState(pickPos);
                            if (pickState.canBeReplaced() || pickState.isAir()){
                                serverLevel.setBlockAndUpdate(pickPos,stateToSet);
                            }
                        }
                    }
                }
            }
            setRemoved(RemovalReason.DISCARDED);
            this.level().levelEvent(2002, this.blockPosition(), 0xFF00FF);
        } else {
            for (int a = 0; a < ClientFXUtils.particleCount(100); ++a) {
                ClientFXUtils.taintsplosionFX(this);
            }
            //TODO:[maybe wont finished]taint bottle splash effect
//            String s = "iconcrack_" + BuiltInRegistries.ITEM.getKey(ConfigItems.itemBottleTaint) + "_" + 0;
//
//            for(int k1 = 0; k1 < 8; ++k1) {
//                Minecraft.getMinecraft().renderGlobal.spawnParticle(s, x, y, z, world.getRandom().nextGaussian() * 0.15, world.getRandom().nextDouble() * 0.2, world.getRandom().nextGaussian() * 0.15);
//            }
//
//            world.playSound(x, y, z, "game.potion.smash", 1.0F, world.getRandom().nextFloat() * 0.1F + 0.9F, false);
        }
        super.onHit(hitResult);
    }
}
