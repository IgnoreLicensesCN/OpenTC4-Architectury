package thaumcraft.common.entities.projectile.primalfocus;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import thaumcraft.api.listeners.worldgen.node.NodeGenerationManager;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.world.biomes.BiomeUtils;

import java.util.Comparator;

import static com.linearity.opentc4.utils.consts.EntityTypeTests.LIVING_TEST;

public class PrimalOrbEntity extends ThrowableProjectile {
    protected boolean seeker = false;
    public PrimalOrbEntity(LivingEntity thrower, Level level) {
        super(ThaumcraftEntities.ThaumcraftEntityTypeInstances.PRIMAL_ORB(), level);
        this.shootFromRotation(thrower,thrower.getXRot(),thrower.getYRot(),0,0.5F,1);
    }
    public PrimalOrbEntity(EntityType<? extends PrimalOrbEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        
    }
    public void setSeeker(boolean seeker){
        this.seeker = seeker;
    }

    @Override
    protected float getGravity() {
        return 0.001F;
    }
    
    protected void playClientEffect(Level level) {
        if (level instanceof ClientLevel clientLevel){
            var posOffset = Vec3.ZERO.offsetRandom(random,0.4F);
            for(int a = 0; a < 6; ++a) {
                ClientFXUtils.wispFX4(clientLevel,
                        posOffset.x,
                        posOffset.y,
                        posOffset.z, this, a, true, 0.0F);
            }
            posOffset = Vec3.ZERO.offsetRandom(random,0.4F);
            var posWithOffset = position().add(posOffset);
            ClientFXUtils.wispFX2(clientLevel,
                    posWithOffset.x,posWithOffset.y,posWithOffset.z,
                    0.1F,
                    random.nextInt(6), 
                    true, true, 0.0F);
        }
    }

    @Override
    public void tick() {
//        if (this.isInsideOfMaterial(Material.portal)) {
//            this.onImpact(new HitResult(this));
//        }
        var level = this.level();
        if (level.isClientSide) {
            playClientEffect(level);
        }
        
        if (this.tickCount > 20) {
            if (!this.seeker) {
                setDeltaMovement(getDeltaMovement().offsetRandom(random,0.02F));
            } else {
                level.getEntities(
                        LIVING_TEST,
                        new AABB(blockPosition()).inflate(16),
                        e -> !e.isDeadOrDying()
                ).stream().min(
                        Comparator.comparingDouble(a -> a.distanceToSqr(PrimalOrbEntity.this))
                ).ifPresent(
                        living -> {
                            var livingPos = living.position();
                            var orbPos = PrimalOrbEntity.this.position();
                            var direction = new Vec3(
                                    livingPos.x-orbPos.x,
                                    living.getBoundingBox().minY + living.getEyeHeight() - orbPos.y,
                                    livingPos.z-orbPos.z
                            ).normalize();
                            var orbMotion = PrimalOrbEntity.this.getDeltaMovement();
                            orbMotion = orbMotion.add(direction.scale(0.2));
                            orbMotion = new Vec3(Math.clamp(orbMotion.x,-0.2,0.2),Math.clamp(orbMotion.y,-0.2,0.2),Math.clamp(orbMotion.z,-0.2,0.2));
                            PrimalOrbEntity.this.setDeltaMovement(orbMotion);
                        }
                );
            }
        }
        super.tick();
        if (this.tickCount > 500){
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public void taintSplosion() {
        if (!(level() instanceof ServerLevel serverLevel)) {
            return;
        }
        var basePos = blockPosition();
        for(int a = 0; a < 10; ++a) {
            var pickPos = basePos.offset(random.nextInt(13)-6,0,random.nextInt(13)-6);
            BiomeUtils.setPosTaintAndSetTaintSourceIfNotTaint(serverLevel, pickPos);
        }

    }

    protected void playClientOnHitEffect(){
        if (!(level() instanceof ClientLevel clientLevel)) {return;}
        for(int a = 0; a < 6; ++a) {
            for(int b = 0; b < 6; ++b) {
                var offset = Vec3.ZERO.offsetRandom(random,1F);
                var offsetScaled = offset.scale(10);
                var posWithOffset = position().add(offset);
                var posWithOffsetScaled = position().add(offsetScaled);
                ClientFXUtils.wispFX3(
                        clientLevel,
                        posWithOffset.x,posWithOffset.y,posWithOffset.z,
                        posWithOffsetScaled.x,posWithOffsetScaled.y,posWithOffsetScaled.z,
                        0.4F, b, true, 0.05F);
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        var level = this.level();
        if (level.isClientSide) {
            playClientOnHitEffect();
        }else {
            float specialChance = 1.0F;
            float expl = 2.0F;
//            if (mop.typeOfHit == MovingObjectType.BLOCK
//                    & this.isInsideOfMaterial(Material.portal)
//            ) {
//                expl = 4.0F;
//                specialChance = 10.0F;
//            }
            var pos = position();
            level.explode(null, pos.x,pos.y,pos.z, expl, Level.ExplosionInteraction.TNT);
            if (!this.seeker && random.nextInt(100) <= specialChance) {
                if (random.nextBoolean()) {
                    this.taintSplosion();
                } else {
                    NodeGenerationManager.createRandomNodeAt(
                            level,blockPosition(),random,false,false,true
                    );
                }
            }

            this.remove(RemovalReason.DISCARDED);
        }
    }
}
