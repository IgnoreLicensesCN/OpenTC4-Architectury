package thaumcraft.common.entities.projectile.hellbatfocus;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.ai.goals.FireBatNearestAttackableTargetGoal;
import thaumcraft.common.lib.utils.EntityUtils;

import java.util.UUID;

import static com.linearity.opentc4.Consts.FireBatEntityTagAccessors.*;

//in fact this is not only for focus
public class FireBatEntity extends Monster implements OwnableEntity {

    private static final TargetingConditions BAT_RESTING_TARGETING = TargetingConditions.forNonCombat().range(4.0);
    public static final UUID DEVIL_MAX_HEALTH_ADDITION_UUID = UUID.fromString("5b878a39-54ca-4930-a999-c2cc65f785d7");

    public static AttributeModifier DEVIL_MAX_HEALTH_ADDITION() {
        return new AttributeModifier(
                DEVIL_MAX_HEALTH_ADDITION_UUID, "Devil bat health addition", 10, AttributeModifier.Operation.ADDITION
        );
    }

    public static final UUID BAT_DAMAGE_ADDITION_UUID = UUID.fromString("d71dda1a-b164-4adc-b477-be561fa5f978");


    private static final int RESTING_FLAG = 1;
    private static final int EXPLOSIVE_FLAG = 1 << 1;
    private static final int DEVIL_FLAG = 1 << 2;
    private static final int VAMPIRE_FLAG = 1 << 3;
    private static final int SUMMONED_FLAG = 1 << 4;
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID
            = SynchedEntityData.defineId(FireBatEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> DATA_DAMAGE_BONUS_ID
            = SynchedEntityData.defineId(FireBatEntity.class, EntityDataSerializers.INT);
    protected UUID ownerUUID;
    @Nullable
    protected BlockPos targetPosition;

    public FireBatEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.FIRE_BAT(), level);
    }

    public FireBatEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        setResting(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        targetSelector.addGoal(2, new FireBatNearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected float getSoundVolume() {
        return 0.1F;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 0.95F;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return this.isResting() && this.random.nextInt(4) != 0 ? null : SoundEvents.BAT_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BAT_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }


    @Override
    public @Nullable UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        var level = level();
        if (level.isClientSide) {
            doClientParticle(level);
        }

        if (this.isResting()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setPosRaw(this.getX(), Mth.floor(this.getY()) + 1.0 - this.getBbHeight(), this.getZ());
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
        }
        attackCooldown -= 1;
    }

    private void doClientParticle(Level level) {
        if (level instanceof ClientLevel clientLevel) {
            if (isExplosive()) {
                ClientFXUtils.drawGenericParticles(
                        clientLevel,
                        this.xOld + (this.random.nextFloat() * 0.2F) - 0.1F,
                        this.yOld + (this.getBbHeight() / 2.0F) + (this.random.nextFloat() * 0.2F) - 0.1F,
                        this.zOld + (this.random.nextFloat() * 0.2F) - 0.1F,
                        0.0F, 0.0F, 0.0F,
                        1.0F, 1.0F, 1.0F,
                        0.8F,
                        false,
                        151, 9, 1,
                        7 + this.random.nextInt(5),
                        0, 1.0F + this.random.nextFloat() * 0.5F);
            }
            if (!isVampire()) {
                clientLevel.addParticle(
                        ParticleTypes.SMOKE,
                        this.xOld + (this.random.nextFloat() * 0.4F) - 0.2F,
                        this.yOld + (this.getBbHeight() / 2.0F) + (this.random.nextFloat() * 0.4F) - 0.2F,
                        this.zOld + (this.random.nextFloat() * 0.4F) - 0.2F,
                        0.0F, 0.0F, 0.0F
                );
                clientLevel.addParticle(
                        ParticleTypes.FLAME,
                        this.xOld + (this.random.nextFloat() * 0.4F) - 0.2F,
                        this.yOld + (this.getBbHeight() / 2.0F) + (this.random.nextFloat() * 0.4F) - 0.2F,
                        this.zOld + (this.random.nextFloat() * 0.4F) - 0.2F,
                        0.0F, 0.0F, 0.0F
                );
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        this.entityData.define(DATA_DAMAGE_BONUS_ID, 0);
    }

    public boolean isResting() {
        return getFlag(RESTING_FLAG);
    }

    public void setResting(boolean resting) {
        setFlag(RESTING_FLAG, resting);
    }

    public boolean isExplosive() {
        return getFlag(EXPLOSIVE_FLAG);
    }

    public void setExplosive(boolean explosive) {
        setFlag(EXPLOSIVE_FLAG, explosive);
    }

    public boolean isDevil() {
        return getFlag(DEVIL_FLAG);
    }

    public void setDevil(boolean devil) {
        setFlag(DEVIL_FLAG, devil);
        var maxHealthAttribute = getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.removeModifier(DEVIL_MAX_HEALTH_ADDITION_UUID);
            if (devil) {
                maxHealthAttribute.addPermanentModifier(DEVIL_MAX_HEALTH_ADDITION());
            }
        }
        updateAttackDamage();
    }

    public boolean isVampire() {
        return getFlag(VAMPIRE_FLAG);
    }

    public void setVampire(boolean vampire) {
        setFlag(VAMPIRE_FLAG, vampire);
    }

    public boolean isSummoned() {
        return getFlag(SUMMONED_FLAG);
    }

    public void setSummoned(boolean summoned) {
        setFlag(SUMMONED_FLAG, summoned);
        updateAttackDamage();
    }

    protected final boolean getFlag(int flag) {
        return (entityData.get(DATA_FLAGS_ID) & flag) != 0;
    }

    protected final void setFlag(int flag, boolean flagValue) {
        byte b = this.entityData.get(DATA_FLAGS_ID);
        if (flagValue) {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b | flag));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (b & ~flag));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.ATTACK_DAMAGE, 1);
    }

    protected void updateAttackDamage() {

        var maxHealthAttribute = getAttribute(Attributes.ATTACK_DAMAGE);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.removeModifier(BAT_DAMAGE_ADDITION_UUID);
            if (isSummoned()) {
                maxHealthAttribute.addPermanentModifier(new AttributeModifier(
                        BAT_DAMAGE_ADDITION_UUID, "Bat damage addition",
                        (isDevil() ? 2 : 1) + getDamageBonus()
                        , AttributeModifier.Operation.ADDITION
                ));
            }
        }
    }

    public int getDamageBonus() {
        return entityData.get(DATA_DAMAGE_BONUS_ID);
    }

    public void setDamageBonus(int damageBonus) {
        entityData.set(DATA_DAMAGE_BONUS_ID, damageBonus);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            if (!level().isClientSide && this.isResting()) {
                this.setResting(false);
            }

            return super.hurt(damageSource, f);
        }
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor levelAccessor, MobSpawnType mobSpawnType) {
        int var4 = level().getLightEmission(blockPosition());
        return var4 <= this.random.nextInt(7) && super.checkSpawnRules(levelAccessor, mobSpawnType);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(DATA_FLAGS_ID, FLAGS.readByteFromCompoundTag(tag));
        entityData.set(DATA_DAMAGE_BONUS_ID, DAMAGE_BONUS.readIntFromCompoundTag(tag));
        ownerUUID = OWNER.readFromCompoundTag(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        FLAGS.writeByteToCompoundTag(compoundTag, entityData.get(DATA_FLAGS_ID));
        DAMAGE_BONUS.writeIntToCompoundTag(compoundTag, entityData.get(DATA_DAMAGE_BONUS_ID));
        OWNER.writeToCompoundTag(compoundTag, ownerUUID);
    }


    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        BlockPos blockPos = this.blockPosition();
        BlockPos blockPos2 = blockPos.above();
        var level = level();
        if (this.isResting()) {
            boolean bl = this.isSilent();
            if (level.getBlockState(blockPos2).isRedstoneConductor(level, blockPos)) {
                if (this.random.nextInt(200) == 0) {
                    this.yHeadRot = this.random.nextInt(360);
                }

                if (level.getNearestPlayer(BAT_RESTING_TARGETING, this) != null) {
                    this.setResting(false);
                    if (!bl) {
                        level.levelEvent(null, 1025, blockPos, 0);
                    }
                }
            } else {
                this.setResting(false);
                if (!bl) {
                    level.levelEvent(null, 1025, blockPos, 0);
                }
            }
        } else {
            var target = getTarget();
            if (target != null){
                if (target.level() != this.level()) {
                    target = null;
                    setTarget(null);
                }
            }
            if (target == null) {
                if (this.isSummoned()) {
                    this.hurt(level.damageSources().generic(), 2.0F);
                }
                tryResting(level, blockPos2);
            } else {
                tryAttackEntity(target, target.distanceToSqr(this));
                double var1 = target.getX() - this.getX();
                double var3 = target.getY() + (double) (target.getEyeHeight() * 0.66F) - this.getY();
                double var5 = target.getZ() - this.getZ();
                adjustRotationAndSpeed(var1, var3, var5);
            }
            if (target instanceof Player player && player.isInvulnerable()) {
                setTarget(null);
            }
        }
    }

    //vanilla part
    protected void tryResting(Level level, BlockPos blockPos2) {
        if (this.targetPosition != null
                && (!level.isEmptyBlock(this.targetPosition)
                || this.targetPosition.getY() <= level.getMinBuildHeight())
        ) {
            this.targetPosition = null;
        }

        if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerToCenterThan(this.position(), 2.0)) {
            this.targetPosition = BlockPos.containing(
                    this.getX() + this.random.nextInt(7) - this.random.nextInt(7),
                    this.getY() + this.random.nextInt(6) - 2.0,
                    this.getZ() + this.random.nextInt(7) - this.random.nextInt(7)
            );
        }

        double d = this.targetPosition.getX() + 0.5 - this.getX();
        double e = this.targetPosition.getY() + 0.1 - this.getY();
        double f = this.targetPosition.getZ() + 0.5 - this.getZ();
        adjustRotationAndSpeed(d, e, f);
        if (this.random.nextInt(100) == 0 && level.getBlockState(blockPos2).isRedstoneConductor(level, blockPos2)) {
            this.setResting(true);
        }
    }

    protected void adjustRotationAndSpeed(double d, double e, double f) {
        Vec3 vec3 = this.getDeltaMovement();
        Vec3 vec32 = vec3.add((Math.signum(d) * 0.5 - vec3.x) * 0.1F, (Math.signum(e) * 0.7F - vec3.y) * 0.1F, (Math.signum(f) * 0.5 - vec3.z) * 0.1F);
        this.setDeltaMovement(vec32);
        float g = (float) (Mth.atan2(vec32.z, vec32.x) * 180.0F / (float) Math.PI) - 90.0F;
        float h = Mth.wrapDegrees(g - this.getYRot());
        this.zza = 0.5F;
        this.setYRot(this.getYRot() + h);
    }

    protected int attackCooldown = 0;

    protected void tryAttackEntity(LivingEntity target, double distanceToEntitySqr) {
        float distanceLimit = Math.max(2.5F, target.getBbWidth() * 1.1F);
        float distanceLimitSqr = distanceLimit * distanceLimit;
        var targetBb = target.getBoundingBox();
        var selfBb = this.getBoundingBox();
        var selfPos = position();
        if (!(
                this.attackCooldown <= 0
                        && distanceToEntitySqr < distanceLimitSqr
                        && targetBb.maxY > selfBb.minY
                        && targetBb.minY < selfBb.maxY
        )) {
            return;
        }

        var level = level();
        if (this.isSummoned()) {
            EntityUtils.setRecentlyHit(target, 100);
        }

        if (this.isVampire()) {
            var owner = getOwner();
            if (owner != null && !owner.hasEffect(MobEffects.REGENERATION)) {
                owner.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 26, 1));
            }

            this.heal(1.0F);
        }

        this.attackCooldown = 20;
        if ((this.isExplosive()
                || level.random.nextInt(10) == 0)
                && level.isClientSide
                && !this.isDevil()
        ) {
            target.invulnerableTime = 0;
            level.explode(this,
                    selfPos.x, selfPos.y, selfPos.z,
                    1.5F + (this.isExplosive() ? this.getDamageBonus() * 0.33F : 0.0F),
                    true,//set on fire
                    Level.ExplosionInteraction.MOB);
            this.remove(RemovalReason.DISCARDED);
        } else if (!this.isVampire() && !level.random.nextBoolean() && !target.fireImmune()) {
            target.setSecondsOnFire(this.isSummoned() ? 4 : 2);
        } else {
            var motion = target.getDeltaMovement();
            doHurtTarget(target);
            target.setDeltaMovement(motion);
            target.setOnGround(true);
        }

        level.playSound(this,
                blockPosition(),
                SoundEvents.BAT_HURT,
                SoundSource.HOSTILE,
                0.5F, 0.9F + level.random.nextFloat() * 0.2F);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        super.dropCustomDeathLoot(damageSource, i, bl);
        if (isSummoned()) {
            this.spawnAtLocation(Items.GUNPOWDER);
        }
    }
}
