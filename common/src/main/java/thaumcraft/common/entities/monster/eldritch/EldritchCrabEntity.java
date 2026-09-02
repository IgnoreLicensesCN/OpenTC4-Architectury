package thaumcraft.common.entities.monster.eldritch;

import com.linearity.opentc4.annotations.StoleFrom;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.IItemStackBreakAnimationPlayable;
import thaumcraft.common.entities.abstracts.ISpiderWithoutSkeletonRiding;
import thaumcraft.common.entities.monster.cultists.CultistEntity;

import java.util.UUID;

import static thaumcraft.common.items.ThaumcraftItemInstances.CULTIST_PLATE_CHESTPLATE;

public class EldritchCrabEntity extends Spider implements ISpiderWithoutSkeletonRiding {
    private static final EntityDataAccessor<Boolean> DATA_HELM_ID
            = SynchedEntityData.defineId(EldritchCrabEntity.class, EntityDataSerializers.BOOLEAN);
    public static final UUID HELM_SPEED_MODIFIER_UUID = UUID.fromString("c02d4d36-617b-4ec9-9fe7-bf792e5c006f");
    public static final UUID HELM_ARMOR_MODIFIER_UUID = UUID.fromString("daba19f7-88b2-4a7b-b9bc-1fdf045edd9d");

    public EldritchCrabEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.ELDRITCH_CRAB(), level);
    }

    public EldritchCrabEntity(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.removeAllGoals(g -> g instanceof MeleeAttackGoal);
        this.goalSelector.addGoal(4,new EldritchCrabAttackGoal(this));
        super.registerGoals();
        this.targetSelector.removeAllGoals(_ignored -> true);

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class,true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class,true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, CultistEntity.class,true));
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.MOVEMENT_SPEED, 3);
    }

    public boolean hasHelm() {
        return Boolean.TRUE == entityData.get(DATA_HELM_ID);
    }

    public void setHelm(boolean helm) {
        entityData.set(DATA_HELM_ID, helm);

        updateSpeedAttribute(helm);
        updateArmorAttribute(helm);
    }

    protected void updateSpeedAttribute(boolean helm) {
        var movementSpeedAttribute = getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (movementSpeedAttribute != null) {
            movementSpeedAttribute.removeModifier(HELM_SPEED_MODIFIER_UUID);
            if (helm) {
                movementSpeedAttribute.addPermanentModifier(
                        new AttributeModifier(
                                HELM_SPEED_MODIFIER_UUID,
                                "helm_speed_addition",
                                -0.025,
                                AttributeModifier.Operation.ADDITION
                        )
                );
            }
        }
    }
    protected void updateArmorAttribute(boolean helm) {
        var armorAttribute = getAttributes().getInstance(Attributes.ARMOR);
        if (armorAttribute != null) {
            armorAttribute.removeModifier(HELM_ARMOR_MODIFIER_UUID);
            if (helm) {
                armorAttribute.addPermanentModifier(
                        new AttributeModifier(
                                HELM_ARMOR_MODIFIER_UUID,
                                "helm_armor_addition",
                                5,
                                AttributeModifier.Operation.ADDITION
                        )
                );
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_HELM_ID, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount < 20) {
            this.fallDistance = 0.0F;
        }
    }

    @StoleFrom("net.minecraft.world.entity.monster.Spider$SpiderAttackGoal")
    public static class EldritchCrabAttackGoal extends MeleeAttackGoal {
        protected final EldritchCrabEntity crab;
        public EldritchCrabAttackGoal(EldritchCrabEntity spider) {
            super(spider, 1.0F, true);
            this.crab = spider;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.mob.isVehicle();
        }

        @Override
        public boolean canContinueToUse() {
            var ridingEntity = this.mob.getVehicle();
            if (ridingEntity instanceof LivingEntity living && living.isAlive()) {
                return true;
            }
            return super.canContinueToUse();
        }

        @Override
        public void tick() {
            var ridingEntity = this.mob.getVehicle();

            if (ridingEntity == null) {
                var target = this.mob.getTarget();
                if (target != null) {
                    if (target.getPassengers().isEmpty()
                            && !this.mob.onGround()
                            && !this.crab.hasHelm()
                            && target.isAlive()
                            && this.mob.getY() - target.getY() >= (double)(target.getBbHeight() / 2.0F)
                            && this.mob.distanceToSqr(target) < (double)4.0F){
                        this.mob.startRiding(this.mob.getTarget());
                    }
                }
            }
            if (ridingEntity instanceof LivingEntity living && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                checkAndPerformAttack(living,this.mob.getPerceivedTargetDistanceSquareForMeleeAttack(living));
                if (this.mob.getRandom().nextFloat() < 0.2F) {
                    this.mob.stopRiding();
                }
            }
            super.tick();
        }

        @Override
        protected void resetAttackCooldown() {
            this.adjustedTickDelay(10 + this.mob.getRandom().nextInt(10));
        }

        @Override
        protected double getAttackReachSqr(LivingEntity livingEntity) {
            return (4.0F + livingEntity.getBbWidth());
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean result = super.doHurtTarget(entity);
        if (result) {
            this.playSound(ThaumcraftSounds.CRAB_CLAW,1.0F, 0.9F + this.level().random.nextFloat() * 0.2F);
        }
        return result;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        boolean result = super.hurt(damageSource, f);
        if (result) {
            if (this.hasHelm() && this.getHealth() / this.getMaxHealth() <= 0.5F) {
                this.setHelm(false);

                ((IItemStackBreakAnimationPlayable)this).playBreakItemAnimation(CULTIST_PLATE_CHESTPLATE().getDefaultInstance());
            }
        }
        return result;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ThaumcraftSounds.CRAB_TALK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.HOSTILE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ThaumcraftSounds.CRAB_DEATH;
    }
}
