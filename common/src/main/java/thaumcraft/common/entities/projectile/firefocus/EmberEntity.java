package thaumcraft.common.entities.projectile.firefocus;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import thaumcraft.api.damagesource.ThaumcraftDamageSources;
import thaumcraft.common.entities.ThaumcraftEntities;

public class EmberEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> DATA_DURATION_ID
            = SynchedEntityData.defineId(EmberEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_FIREY_ID
            = SynchedEntityData.defineId(EmberEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_DAMAGE_ID
            = SynchedEntityData.defineId(EmberEntity.class, EntityDataSerializers.FLOAT);

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_DURATION_ID, 20);
        entityData.define(DATA_FIREY_ID, 0);
        entityData.define(DATA_DAMAGE_ID, 1.0F);
    }
    public EmberEntity(Level par1World){
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.EMBER(),par1World);
    }
    public EmberEntity(EntityType<EmberEntity> type, Level par1World) {
        super(type,par1World);
    }
    public EmberEntity(LivingEntity par2EntityLiving, Level par1World){
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.EMBER(),par2EntityLiving,par1World);
    }
    public EmberEntity(LivingEntity par2EntityLiving, Level par1World,float scatter){
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.EMBER(),par2EntityLiving,par1World);
        this.shootFromRotation(par2EntityLiving,par2EntityLiving.getXRot(),par2EntityLiving.getYRot(),0,1,scatter);
    }
    public EmberEntity(EntityType<EmberEntity> type, LivingEntity par2EntityLiving, Level par1World) {
        super(type,par2EntityLiving,par1World);
    }

    public EmberEntity(Level par1World, double par2, double par4, double par6) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.EMBER(),par2, par4, par6, par1World);
    }
    public EmberEntity(EntityType<EmberEntity> type, double par2, double par4, double par6, Level par1World) {
        super(type,par2, par4, par6, par1World);
    }

    @Override
    public void tick() {

        if (this.tickCount > this.getDuration()) {
            this.remove(RemovalReason.DISCARDED);
        }

        if (this.getDuration() <= 20) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.95, 0.95, 0.95));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.975, 0.975, 0.975));
        }

        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.66, 0.66, 0.66));
        }
        super.tick();
    }


    @Override
    protected float getGravity() {
        return 0;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        return false;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        var level = level();
        if (!level.isClientSide) {
            if (hitResult instanceof EntityHitResult entityHitResult) {
                var entity = entityHitResult.getEntity();
                var damageSource = new DamageSource(ThaumcraftDamageSources.getHolder(level, DamageTypes.FIREBALL),this,getOwner());
                if (!entity.isInvulnerableTo(damageSource)
                        && entity.hurt(damageSource,
                        this.getDamage())
                ) {
                    entity.setSecondsOnFire(3 + this.getFirey());
                }
            } else if (hitResult instanceof BlockHitResult blockHitResult) {
                if (this.random.nextFloat() < 0.025F * (float) this.getFirey()) {
                    var hitPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());

                    if (level.getBlockState(hitPos).isAir()) {
                        level.setBlockAndUpdate(hitPos,Blocks.FIRE.defaultBlockState());
                    }
                }
            }
        }
        this.setRemoved(RemovalReason.DISCARDED);
    }

    public int getDuration() {
        return entityData.get(DATA_DURATION_ID);
    }

    public void setDuration(int duration) {
        entityData.set(DATA_DURATION_ID,duration);
    }

    public int getFirey() {
        return entityData.get(DATA_FIREY_ID);
    }

    public void setFirey(int firey) {
        entityData.set(DATA_FIREY_ID,firey);
    }

    public float getDamage() {
        return entityData.get(DATA_DAMAGE_ID);
    }

    public void setDamage(float damage) {
        entityData.set(DATA_DAMAGE_ID,damage);
    }
}
