package thaumcraft.common.entities.projectile.frostfocus;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.ThaumcraftEntities;

import static com.linearity.opentc4.Consts.FrostShardEntityTagAccessors.*;
import static thaumcraft.common.blocks.ThaumcraftBlocks.ThaumcraftBlockInstances.FROST_FOCUS_PARTICLE_PROVIDER;

public class FrostShardEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> DATA_FROSTY_ID
            = SynchedEntityData.defineId(FrostShardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_FRAGILE_ID
            = SynchedEntityData.defineId(FrostShardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_BOUNCE_ID
            = SynchedEntityData.defineId(FrostShardEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_BOUNCE_LIMIT_ID
            = SynchedEntityData.defineId(FrostShardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_DAMAGE_ID
            = SynchedEntityData.defineId(FrostShardEntity.class, EntityDataSerializers.FLOAT);

    public FrostShardEntity(EntityType<FrostShardEntity> type, Level par1World) {
        super(type, par1World);
    }

    public FrostShardEntity(LivingEntity living, Level par1World) {
        super(ThaumcraftEntities.ThaumcraftEntityTypeInstances.FROST_SHARD(), living, par1World);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_FROSTY_ID, 0);
        entityData.define(DATA_FRAGILE_ID, false);
        entityData.define(DATA_BOUNCE_ID,0.5F);
        entityData.define(DATA_BOUNCE_LIMIT_ID,3);
        entityData.define(DATA_DAMAGE_ID,0.0F);
    }

    @Override
    protected float getGravity() {
        return isFragile() ? 0.015F : 0.05F;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setBounce(BOUNCE.readFloatFromCompoundTag(tag));
        setBounceLimit(BOUNCE_LIMIT.readIntFromCompoundTag(tag));
        setFragile(FRAGILE.readBooleanFromCompoundTag(tag));
        setDamage(DAMAGE.readFloatFromCompoundTag(tag));
        setFrosty(FROSTY.readIntFromCompoundTag(tag));

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        BOUNCE.writeFloatToCompoundTag(tag, getBounce());
        BOUNCE_LIMIT.writeIntToCompoundTag(tag, getBounceLimit());
        FRAGILE.writeBooleanToCompoundTag(tag, isFragile());
        DAMAGE.writeFloatToCompoundTag(tag, getDamage());
        FROSTY.writeIntToCompoundTag(tag, getFrosty());
    }

    @Override
    public void tick() {
        super.tick();
        var level = level();
        if (level.isClientSide) {
            if (getFrosty() > 0) {
                float s = getDamage() / 10.0F;

                for (int a = 0; a < getFrosty(); ++a) {
                    ClientFXUtils.sparkle(
                            (float) this.getX() - s + this.random.nextFloat() * s * 2.0F,
                            (float) this.getY() - s + this.random.nextFloat() * s * 2.0F,
                            (float) this.getZ() - s + this.random.nextFloat() * s * 2.0F,
                            0.4F, 6, 0.005F);
                }
            }
        }

    }

    @Override
    protected void updateRotation() {
        double dx = this.getDeltaMovement().x;
        double dy = this.getDeltaMovement().y;
        double dz = this.getDeltaMovement().z;

        double horiz = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Math.atan2(dx, dz) * (180D / Math.PI));
        float pitch = (float) (Math.atan2(dy, horiz) * (180D / Math.PI));

        this.setXRot(lerpRotation(this.xRotO, yaw));
        this.setYRot(lerpRotation(this.yRotO, pitch));
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var entity = entityHitResult.getEntity();
        var pos = position();
        var posOffset = position().subtract(entity.position());
        var movement = getDeltaMovement().multiply(
                (posOffset.x != 0 ? -1 : 1) * 0.66,
                (posOffset.y != 0 ? -0.9 : 1) * 0.66,
                (posOffset.z != 0 ? -1 : 1) * 0.66
        );

        this.setDeltaMovement(movement);
        var level = level();
        level.addParticle(
                new BlockParticleOption(ParticleTypes.BLOCK, FROST_FOCUS_PARTICLE_PROVIDER().defaultBlockState()),
                pos.x, pos.y, pos.z,
                4.0F * (this.random.nextFloat() - 0.5F),
                0.5F,
                (this.random.nextFloat() - 0.5F) * 4.0F
        );
        scaleSpeedWithBounce();
        if (!level.isClientSide) {
            entity.hurt(
                    level.damageSources().thrown(this, getOwner()), getDamage()
            );
            if (entity instanceof LivingEntity living && this.getFrosty() > 0) {
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, getFrosty() - 1));
                if (this.isFragile()) {
                    living.invulnerableTime = 0;
                }
            }

            if (this.isFragile()) {
                this.remove(RemovalReason.DISCARDED);
                this.playSound(Blocks.ICE.defaultBlockState().getSoundType().getBreakSound(), 0.3F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                entity.setDeltaMovement(entity.getDeltaMovement().scale(1. / 10.));
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        var level = level();
        Direction dir = blockHitResult.getDirection();
        var movement = getDeltaMovement().multiply(
                (dir.getStepX() != 0 ? -1 : 1),
                (dir.getStepY() != 0 ? -0.9 : 1),
                (dir.getStepZ() != 0 ? -1 : 1)
        );

        this.setDeltaMovement(movement);
        var pos = blockHitResult.getBlockPos();
        var hitState = level.getBlockState(pos);

        this.playSound(
                hitState.getSoundType().getBreakSound(),
                0.3F,
                1.2F / (this.random.nextFloat() * 0.2F + 0.9F)
        );
        level.addParticle(
                new BlockParticleOption(ParticleTypes.BLOCK, hitState),
                pos.getX(), pos.getY(), pos.getZ(),
                4.0F * (this.random.nextFloat() - 0.5F),
                0.5F,
                (this.random.nextFloat() - 0.5F) * 4.0F
        );//it doesnt break block at 4.2.3.5
        scaleSpeedWithBounce();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        this.markHurt();
        var bounceLimit = this.getBounceLimit() - 1;
        this.setBounceLimit(bounceLimit);
        if (bounceLimit <= 0) {
            this.remove(RemovalReason.DISCARDED);
            this.playSound(Blocks.ICE.defaultBlockState().getSoundType().getBreakSound(),
                    0.3F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            var pos = position();
            for (int a = 0; a < 8.0F * this.getDamage(); ++a) {
                level().addParticle(
                        new BlockParticleOption(ParticleTypes.BLOCK, FROST_FOCUS_PARTICLE_PROVIDER().defaultBlockState()),
                        pos.x, pos.y, pos.z,
                        4.0F * (this.random.nextFloat() - 0.5F),
                        0.5F,
                        (this.random.nextFloat() - 0.5F) * 4.0F
                );
            }
        }


    }

    private void scaleSpeedWithBounce() {
        var movement = this.getDeltaMovement().scale(this.getBounce());
        this.setDeltaMovement(movement);
        this.setPos(this.position().subtract(movement.scale(0.05 * movement.length())));
    }

    public boolean isFragile() {
        return entityData.get(DATA_FRAGILE_ID);
    }

    public void setFragile(boolean fragile) {
        entityData.set(DATA_FRAGILE_ID, fragile);
    }

    public int getFrosty() {
        return entityData.get(DATA_FROSTY_ID);
    }

    public void setFrosty(int frosty) {
        entityData.set(DATA_FROSTY_ID, frosty);
    }

    public float getBounce() {
        return entityData.get(DATA_BOUNCE_ID);
    }

    public void setBounce(float bounce) {
        entityData.set(DATA_BOUNCE_ID,bounce);
    }

    public int getBounceLimit() {
        return entityData.get(DATA_BOUNCE_LIMIT_ID);
    }

    public void setBounceLimit(int bounceLimit) {
        entityData.set(DATA_BOUNCE_LIMIT_ID, bounceLimit);
    }

    public float getDamage() {
        return entityData.get(DATA_DAMAGE_ID);
    }

    public void setDamage(float damage) {
        entityData.set(DATA_DAMAGE_ID, damage);
    }
}
