package thaumcraft.common.entities.monster.boss;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.ai.goals.ZombieLikeAttackGoal;
import thaumcraft.common.entities.championmod.ChampionModifier;
import thaumcraft.common.entities.championmod.abstracts.entity.IChampionModifiedNamingRuleOwner;
import thaumcraft.common.entities.projectile.golemorb.GolemOrbEntity;
import thaumcraft.common.lib.utils.EntityUtils;

import static com.linearity.opentc4.Consts.EldritchGolemEntityTagAccessors.HEADLESS;

public class EldritchGolemEntity extends ThaumcraftBossEntity implements IChampionModifiedNamingRuleOwner, RangedAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_HEADLESS_ID = SynchedEntityData.defineId(EldritchGolemEntity.class, EntityDataSerializers.BOOLEAN);
    protected int beamCharge = 0;
    protected boolean chargingBeam = false;
    protected int arcing = 0;
    protected int ax = 0;
    protected int ay = 0;
    protected int az = 0;
    public int attackTimer;
    public EldritchGolemEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ELDRITCH_GOLEM(), level);
    }
    public EldritchGolemEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return ThaumcraftBossEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 250)
                .add(Attributes.ATTACK_DAMAGE,10)
                .add(Attributes.MOVEMENT_SPEED,0.3)
                .add(Attributes.ARMOR,6)
                ;
    }

    @Override
    protected void registerGoals() {
        addLookingAtGoals();
        addBehaviourGoals();
    }

    protected void addLookingAtGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new EldritchGolemEntityRangedAttackGoal(this, 1.0F, 5, 5, 24.0F));
        this.goalSelector.addGoal(2, new ZombieLikeAttackGoal(this, 1.1F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        addTargetGoals();
    }

    protected void addTargetGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public void setNameWhenChampionModified(LivingEntity living, ChampionModifier modifier) {
        this.setCustomName(Component.translatable("entity.Thaumcraft.EldritchGolem.name",modifier.getChampionModifierNameLocalized()));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_HEADLESS_ID, false);
    }
    public void setHeadless(boolean headless) {
        entityData.set(DATA_HEADLESS_ID, headless);
    }
    public boolean isHeadless() {
        return entityData.get(DATA_HEADLESS_ID);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setHeadless(HEADLESS.readBooleanFromCompoundTag(tag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        HEADLESS.writeBooleanToCompoundTag(tag, isHeadless());
    }

    public static class EldritchGolemEntityRangedAttackGoal extends RangedAttackGoal{
        protected final @NotNull EldritchGolemEntity golem;

        public EldritchGolemEntityRangedAttackGoal(@NotNull EldritchGolemEntity rangedAttackMob, double d, int i, float f) {
            super(rangedAttackMob, d, i, f);
            this.golem = rangedAttackMob;
        }

        public EldritchGolemEntityRangedAttackGoal(@NotNull EldritchGolemEntity rangedAttackMob, double d, int i, int j, float f) {
            super(rangedAttackMob, d, i, j, f);
            this.golem = rangedAttackMob;
        }

        @Override
        public boolean canUse() {
            return golem.isHeadless() && super.canUse();
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return getBbHeight()*(isHeadless()? (3.3F/3.5F):(3F/3.5F));
    }


    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource arg) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos arg, BlockState arg2) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        this.invulnerableTicksLimit = 100;
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }



    @Override
    public boolean canSpawnSprintParticle() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7F && this.random.nextInt(5) == 0;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if (this.attackTimer > 0) {
            return false;
        } else {
            this.attackTimer = 10;
            this.level().broadcastEntityEvent(this, (byte)4);
            boolean flag = target.hurt(
                    level().damageSources().mobAttack(this), (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.75F);
            if (flag) {
                target.addDeltaMovement(new Vec3(0,0.2,0));
                if (this.isHeadless()) {
                    target.addDeltaMovement(
                            new Vec3(
                                    -MathHelper.sin(this.getYRot() * (float)Math.PI / 180.0F) * 1.5F,
                                    0.1,
                                    MathHelper.cos(this.getYRot() * (float)Math.PI / 180.0F) * 1.5F
                            )
                    );
                }
            }

            return flag;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!level().isClientSide && damage > this.getHealth() && !this.isHeadless()) {
            this.setHeadless(true);
            this.invulnerableTicksLimit = 100;
            var posX = this.getX();
            var posY = this.getY();
            var posZ = this.getZ();
            double xx = MathHelper.cos(this.getYRot() % 360.0F / 180.0F * (float)Math.PI) * 0.75F;
            double zz = MathHelper.sin(this.getYRot() % 360.0F / 180.0F * (float)Math.PI) * 0.75F;
            this.level().explode(this, posX + xx, posY + (double)this.getEyeHeight(), posZ + zz, 2.0F, Level.ExplosionInteraction.MOB);
            this.setHeadless(true);
            return false;
        } else {
            return super.hurt(source, damage);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity victim, float f) {
        if (EntityUtils.canEntityBeSeen(this,victim) && !this.chargingBeam && this.beamCharge > 0) {
            this.beamCharge -= 15 + this.random.nextInt(5);
            this.lookAt(victim, 30.0F, 30.0F);
            Vec3 v = this.getLookAngle();
            var blast = new GolemOrbEntity(this, victim,this.level());
            blast.setPos(blast.position().add(v.x,0,v.z));
            var victimMotion = victim.getDeltaMovement();
            double d0 = victim.getX() + victimMotion.x - this.getX();
            double d1 = victim.getY() - this.getY() - (victim.getBbHeight() / 2.0F);
            double d2 = victim.getZ() + victimMotion.z - this.getZ();
            blast.shoot(d0, d1, d2, 0.66F, 5.0F);
            this.playSound(ThaumcraftSounds.EG_ATTACK, 1.0F, 1.0F + this.random.nextFloat() * 0.1F);
            this.level().addFreshEntity(blast);
        }
    }

    @Override
    public void handleEntityEvent(byte p_70103_1_) {
        if (p_70103_1_ == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else if (p_70103_1_ == 18) {
            this.invulnerableTicksLimit = 150;
        } else if (p_70103_1_ == 19) {
            if (this.arcing == 0) {
                float radius = 2.0F + this.random.nextFloat() * 2.0F;
                double radians = Math.toRadians(this.random.nextInt(360));
                double deltaX = (double)radius * Math.cos(radians);
                double deltaZ = (double)radius * Math.sin(radians);
                int bx = MathHelper.floor_double(this.getX() + deltaX);
                int by = MathHelper.floor_double(this.getY());
                int bz = MathHelper.floor_double(this.getZ() + deltaZ);

                for(int c = 0; c < 5 && this.level().getBlockState(new BlockPos(bx, by, bz)).isAir(); --by) {
                    ++c;
                }

                if (this.level().getBlockState(new BlockPos(bx, by + 1, bz)).isAir() && !this.level().getBlockState(new BlockPos(bx, by, bz)).isAir()) {
                    this.ax = bx;
                    this.ay = by;
                    this.az = bz;
                    this.arcing = 8 + this.random.nextInt(5);
                    this.playSound(ThaumcraftSounds.JACOBS, 0.8F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
                }
            }
        } else {
            super.handleEntityEvent(p_70103_1_);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.invulnerableTicksLimit == 150){
            this.level().broadcastEntityEvent(this, (byte)18);
        }

        if (this.invulnerableTicksLimit > 0) {
            this.heal(2);
        }
        super.tick();
        if (level().isClientSide) {
            clientTick();
        } else {
            if (this.isHeadless() && this.beamCharge <= 0) {
                this.chargingBeam = true;
            }

            if (this.isHeadless() && this.chargingBeam) {
                ++this.beamCharge;
                this.level().broadcastEntityEvent(this, (byte)19);
                if (this.beamCharge == 150) {
                    this.chargingBeam = false;
                }
            }
        }
    }

    private void clientTick() {
        if (level().isClientSide){
            if (level() instanceof ClientLevel clientLevel){

                if (this.isHeadless()) {
                    this.setXRot(0.0F);
                    float f1 = MathHelper.cos(-this.getYRot() * ((float)Math.PI / 180F) - (float)Math.PI);
                    float f2 = MathHelper.sin(-this.getYRot() * ((float)Math.PI / 180F) - (float)Math.PI);
                    float f3 = -MathHelper.cos(-this.getXRot() * ((float)Math.PI / 180F));
                    float f4 = MathHelper.sin(-this.getXRot() * ((float)Math.PI / 180F));
                    var v = new Vec3(f2 * f3, f4, f1 * f3);
                    if (this.random.nextInt(20) == 0) {
                        float a = (this.random.nextFloat() - this.random.nextFloat()) / 2.0F;
                        float b = (this.random.nextFloat() - this.random.nextFloat()) / 2.0F;
                        ClientFXUtils.spark((float)(
                                        this.getX() + v.x + (double)a),
                                (float)this.getY() + this.getEyeHeight() - 0.25F,
                                (float)(this.getZ() + v.z + (double)b),
                                0.3F,
                                0.65F + this.random.nextFloat() * 0.1F,
                                1.0F, 1.0F, 0.8F
                        );
                    }

                    ClientFXUtils.drawVentParticles(clientLevel,
                            (double)((float)this.getX()) + v.x * 0.66, (float)this.getY() + this.getEyeHeight() - 0.75F, (double)((float)this.getZ()) + v.z * 0.66, 0.0F, 0.001, 0.0F, 5592405, 4.0F);
                    if (this.arcing > 0) {
                        ClientFXUtils.arcLightning(clientLevel, this.getX(), this.getY() + (double)(this.getBbHeight() / 2.0F), this.getZ(), (double)this.ax + (double)0.5F, this.ay + 1, (double)this.az + (double)0.5F, 0.65F + this.random.nextFloat() * 0.1F, 1.0F, 1.0F, 1.0F - (float)this.arcing / 10.0F);
                        --this.arcing;
                    }
                }
            }
        }
    }
}
