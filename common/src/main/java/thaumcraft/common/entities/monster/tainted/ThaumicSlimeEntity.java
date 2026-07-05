package thaumcraft.common.entities.monster.tainted;

import com.linearity.opentc4.mixinaccessors.SlimeAttackGoalAccessorGetter;
import com.linearity.opentc4.mixinaccessors.SlimeMoveControlAccessorGetter;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;

import java.util.EnumSet;

public class ThaumicSlimeEntity extends Slime {

    public ThaumicSlimeEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.THAUMIC_SLIME(), level);
    }
    public ThaumicSlimeEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    public ThaumicSlimeEntity(Level level, LivingEntity thaumicSlimeFrom, LivingEntity victim) {
        this(level);
        this.setSize(1,true);
        var prevSlimeBoundingBox = thaumicSlimeFrom.getBoundingBox();
        var victimBoundingBox = victim.getBoundingBox();

        var selfY = (prevSlimeBoundingBox.minY + prevSlimeBoundingBox.maxY) / (double)2.0F;
        double headingX = victim.getX() - thaumicSlimeFrom.getX();
        double headingY = victimBoundingBox.minY + (double)(victim.getBbHeight() / 3.0F) - this.getY();
        double headingZ = victim.getZ() - thaumicSlimeFrom.getZ();
        double var12 = MathHelper.sqrt_double(headingX * headingX + headingZ * headingZ);
        if (var12 >= 1.0E-7) {
            this.setPos(thaumicSlimeFrom.getX(),selfY,thaumicSlimeFrom.getZ());
//            this.eyeHeight = 0.0F;
            float var20 = (float)var12 * 0.2F;
            this.setThrowableHeading(
                    new Vec3(headingX,headingY + var20,headingZ),
                    1.5F,
                    1.0F
            );
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes();
    }
    protected int spitCounter = 100;

    protected void dealDamage(LivingEntity livingEntity) {
        if (this.isAlive()) {
            int i =(int) Math.clamp(0.25F * Math.sqrt(this.getSize()) + 0.25F,1,127);
            if (this.distanceToSqr(livingEntity) < i * i
                    && this.hasLineOfSight(livingEntity)
                    && livingEntity.hurt(this.damageSources().mobAttack(this), this.getAttackDamage())
            ) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.doEnchantDamageEffects(this, livingEntity);
            }
        }
    }

    public void setThrowableHeading(Vec3 throwingVec, float throwStrengthMultiplier, float offsetMultiplier) {
        this.lookAt(EntityAnchorArgument.Anchor.EYES,throwingVec);
        this.setDeltaMovement(this.getDeltaMovement()
                .scale(1./throwingVec.length())
                .offsetRandom(this.random,0.015f * offsetMultiplier)
                .scale(throwStrengthMultiplier));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        var defaultAttackGoalAccessor = ((SlimeAttackGoalAccessorGetter)this).opentc4$getSlimeAttackGoal();
        if (defaultAttackGoalAccessor instanceof Goal attackGoal) {
            this.goalSelector.removeGoal(attackGoal);
        }
        this.goalSelector.addGoal(2,new ThaumicSlimeAttackGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, ThaumicSlimeEntity.class, true));
    }

    @Override
    public void remove(RemovalReason removalReason) {
        int i = this.getSize();
        if (!this.level().isClientSide && i > 1 && this.isDeadOrDying()) {
            Component component = this.getCustomName();
            boolean bl = this.isNoAi();
            float f = i / 4.0F;
            int k = (int) Math.sqrt(getSize());

            for (int l = 0; l < k; l++) {
                float g = (l % 2 - 0.5F) * f;
                float h = (l / 2.F - 0.5F) * f;
                Slime slime = this.getType().create(this.level());
                if (slime != null) {
                    if (this.isPersistenceRequired()) {
                        slime.setPersistenceRequired();
                    }

                    slime.setCustomName(component);
                    slime.setNoAi(bl);
                    slime.setInvulnerable(this.isInvulnerable());
                    slime.setSize(1, true);
                    slime.moveTo(this.getX() + g, this.getY() + 0.5, this.getZ() + h, this.random.nextFloat() * 360.0F, 0.0F);
                    this.level().addFreshEntity(slime);
                }
            }
        }
        this.setRemoved(removalReason);
        this.brain.clearMemories();
    }

    @Override
    protected int getJumpDelay() {
        return this.random.nextInt(16) + 8;
    }
    @Override
    public void setSize(int i, boolean bl) {
        super.setSize(i, bl);
        int j = Mth.clamp(i, 1, 127);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(j);
        float ss = (float)Math.sqrt(i);
        this.xpReward = (int) Mth.clamp(ss, 1, 127);
    }

    public static class ThaumicSlimeAttackGoal extends Goal {
        private final ThaumicSlimeEntity slime;
        private int growTiredTimer;

        public ThaumicSlimeAttackGoal(ThaumicSlimeEntity slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity == null) {
                return false;
            } else {
                return this.slime.canAttack(livingEntity) && ((SlimeMoveControlAccessorGetter) this.slime).opentc4$getSlimeMoveControl() != null;
            }
        }

        @Override
        public void start() {
            this.growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity == null) {
                return false;
            } else {
                return this.slime.canAttack(livingEntity) && --this.growTiredTimer > 0;
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity != null) {
                this.slime.lookAt(livingEntity, 10.0F, 10.0F);
                int thisSlimeSize = slime.getSize();
                var level = slime.level();
                if (livingEntity instanceof ThaumicSlimeEntity anotherSlime
                        && anotherSlime.isMergeableSlime(this.slime)
                        && this.slime.isMergeableSlime(anotherSlime)
                ){

                    int anotherSlimeSize = anotherSlime.getSize();
                    if (thisSlimeSize >= anotherSlimeSize) {
                        this.slime.mergeSlime(anotherSlime);
                    }else {
                        anotherSlime.mergeSlime(this.slime);
                    }
                }
                else{
                    if (this.slime.spitCounter > 0) {
                        --this.slime.spitCounter;
                    }
                    if (slime.distanceToSqr(livingEntity.position()) > 16.0F && slime.spitCounter <= 0 && thisSlimeSize > 3) {
                        slime.spitCounter = 101;
                        if (!level.isClientSide) {
                            var slimeThrown = new ThaumicSlimeEntity(level, slime, livingEntity);
                            level.addFreshEntity(slimeThrown);
                        }

                        level.playSound(slime, slime.blockPosition(), ThaumcraftSounds.GORE, SoundSource.HOSTILE, 1.0F, (level.random.nextFloat() * 0.4F + 0.8F) * 0.8F);
                        slime.setSize(thisSlimeSize - 1,false);
                    }
                }

            }
            var moveControlExpected = ((SlimeMoveControlAccessorGetter)this.slime).opentc4$getSlimeMoveControl();
            if (moveControlExpected != null) {
                moveControlExpected.opentc4$setDirection(this.slime.getYRot(), this.slime.isDealsDamage());
            }
        }
    }

    protected boolean isMergeableSlime(ThaumicSlimeEntity anotherSlime) {
        return anotherSlime.getSize() < 100 && anotherSlime.tickCount > 100 && anotherSlime != this;
    }

    protected void mergeSlime(ThaumicSlimeEntity anotherSlime) {
        int thisSlimeSize = getSize();
        var widthTotal = anotherSlime.getBbWidth() + this.getBbWidth();
        if (anotherSlime.position().distanceToSqr(this.position()) < widthTotal*widthTotal) {
            this.setSize(Math.min(100, thisSlimeSize + anotherSlime.getSize()),true);
            anotherSlime.discard();
        }
    }

}
