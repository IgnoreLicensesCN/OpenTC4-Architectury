package thaumcraft.common.entities.projectile.firefocus;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import thaumcraft.api.damagesource.ThaumcraftDamageSources;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.ThaumcraftEntities;

import static com.linearity.opentc4.Consts.ExplosiveOrbEntityTagAccessors.ON_FIRE;
import static com.linearity.opentc4.Consts.ExplosiveOrbEntityTagAccessors.STRENGTH;

public class ExplosiveOrbEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Float> DATA_STRENGTH_ID
            = SynchedEntityData.defineId(ExplosiveOrbEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_ON_FIRE_ID
            = SynchedEntityData.defineId(ExplosiveOrbEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_STRENGTH_ID, 1.0F);
        entityData.define(DATA_ON_FIRE_ID, false);
    }

    public ExplosiveOrbEntity(Level par1World){
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.EXPLOSIVE_ORB(),par1World);
    }
    public ExplosiveOrbEntity(EntityType<ExplosiveOrbEntity> type, Level par1World) {
        super(type,par1World);
    }
    public ExplosiveOrbEntity(LivingEntity par2EntityLiving, Level par1World){
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.EXPLOSIVE_ORB(),par2EntityLiving,par1World);
    }
    public ExplosiveOrbEntity(EntityType<ExplosiveOrbEntity> type, LivingEntity par2EntityLiving, Level par1World) {
        super(type,par2EntityLiving,par1World);
    }

    public ExplosiveOrbEntity(Level par1World, double par2, double par4, double par6) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.EXPLOSIVE_ORB(),par2, par4, par6, par1World);
    }
    public ExplosiveOrbEntity(EntityType<ExplosiveOrbEntity> type, double par2, double par4, double par6, Level par1World) {
        super(type,par2, par4, par6, par1World);
    }

    @Override
    protected float getGravity() {
        return 0.01F;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setStrength(STRENGTH.readIntFromCompoundTag(compoundTag));
        setOnFire(ON_FIRE.readBooleanFromCompoundTag(compoundTag));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        STRENGTH.readIntFromCompoundTag(compoundTag);
        ON_FIRE.readBooleanFromCompoundTag(compoundTag);
    }

    @Override
    public void tick() {
        super.tick();
        var level = level();
        
        if (level.isClientSide) {
            if (level instanceof ClientLevel clientLevel){
                ClientFXUtils.drawGenericParticles(
                        clientLevel,
                        this.xOld + (this.random.nextFloat() * 0.6F - 0.3F),
                        this.yOld + (this.random.nextFloat() * 0.6F - 0.3F),
                        this.zOld + (this.random.nextFloat() * 0.6F - 0.3F),
                        0.0F, 0.0F, 0.0F,
                        1.0F, 1.0F, 1.0F,
                        0.8F,
                        false,
                        151,
                        9,
                        1,
                        7 + this.random.nextInt(5),
                        0, 2.0F + this.random.nextFloat());
            }
        }
        if (this.tickCount >= 500){
            this.remove(RemovalReason.DISCARDED);
        }
    }


    public DamageSource causeFireballDamage(Entity thrower) {
        return thrower == null
                ? new DamageSource(ThaumcraftDamageSources.getHolder(this.level(),DamageTypes.ON_FIRE), this, this)
                : new DamageSource(ThaumcraftDamageSources.getHolder(this.level(),DamageTypes.FIREBALL), this, thrower);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        var level = level();
        if (!level.isClientSide) {
            if (hitResult instanceof EntityHitResult entityHitResult) {
                var victim = entityHitResult.getEntity();
                victim.hurt(causeFireballDamage(this.getOwner()), this.getStrength() * 1.5F);
            }

            level.explode(null, this.getX(), this.getY(), this.getZ(), this.getStrength(), this.isOnFire(), Level.ExplosionInteraction.MOB);
        }

        this.setRemoved(RemovalReason.DISCARDED);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            this.markHurt();
            Entity entity = damageSource.getEntity();
            if (entity != null) {
                if (!this.level().isClientSide) {
                    Vec3 vec3 = entity.getLookAngle();
                    this.setDeltaMovement(vec3.multiply(0.9F,0.9F,0.9F));
                    this.setOwner(entity);
                }

                return true;
            } else {
                return false;
            }
        }
    }
    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity);
    }

    public float getStrength() {
        return entityData.get(DATA_STRENGTH_ID);
    }

    public void setStrength(float strength) {
        entityData.set(DATA_STRENGTH_ID, strength);
    }

    @Override
    public boolean isOnFire() {
        return entityData.get(DATA_ON_FIRE_ID);
    }

    public void setOnFire(boolean onFire) {
        entityData.set(DATA_ON_FIRE_ID, onFire);
    }
}
