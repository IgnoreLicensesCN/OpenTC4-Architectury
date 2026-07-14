package thaumcraft.common.entities.monster.tainted;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintacleEntityClientAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.damagesource.ThaumcraftDamageSources;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.IMobAttackDamageTypeReplaceable;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeLookups;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeTags;

import java.lang.ref.WeakReference;
import java.util.EnumSet;

import static thaumcraft.common.entities.ThaumcraftEntities.taintedMobWontAttack;

public class TaintacleEntity extends Monster implements IMobAttackDamageTypeReplaceable {
    public TaintacleEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTACLE(), level);
    }
    public TaintacleEntity(EntityType<? extends TaintacleEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected @NotNull WeakReference<TaintacleAttackGoal> attackGoalRef = new WeakReference<>(null);
    @Override
    protected void registerGoals() {
        var attackGoal = new TaintacleAttackGoal(this);
        this.attackGoalRef = new WeakReference<>(attackGoal);
        this.goalSelector.addGoal(
                1,attackGoal
        );
        this.targetSelector.addGoal(
                1,new NearestAttackableTargetGoal<>(this,LivingEntity.class,true, living -> !taintedMobWontAttack(living))
        );
    }

    @Override
    public boolean canPickUpLoot() {
        return false;
    }
    public int getExperienceReward() {
        return 10;
    }
    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50).add(Attributes.ATTACK_DAMAGE,7);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        var hurtByDirect = damageSource.getEntity();
        if (this.getType().is(ThaumcraftEntities.EntityTags.CAN_SPAWN_SMALL_TAINTACLE)
                && hurtByDirect != null
                && this.distanceToSqr(hurtByDirect) > 256.0F && !level().isClientSide
        ) {
            this.spawnTentacles(hurtByDirect.blockPosition());
        }
        return super.hurt(damageSource, f);
    }


    protected void spawnTentacles(BlockPos spawnAtPos) {
        var level = level();
        var biome = level.getBiome(spawnAtPos);
        var spawnAtState = level.getBlockState(spawnAtPos);

        if (biome.is(ThaumcraftBiomeTags.SMALL_TAINTACLE_CAN_SPAWN)
                && (
                spawnAtState.is(ThaumcraftBlocks.Tags.SMALL_TAINTACLE_CAN_SPAWN)
                || level.getBlockState(spawnAtPos.below()).is(ThaumcraftBlocks.Tags.SMALL_TAINTACLE_CAN_SPAWN)
        )) {
            var attackGoal = this.attackGoalRef.get();
            if (attackGoal != null) {
                attackGoal.ticksUntilNextAttack = 40 + level.random.nextInt(20);
            }
            var smallType = getSmallTaintacleType();
            if (smallType != null){
                var smallTaintacle = smallType.create(level);
                if (smallTaintacle != null) {
                    smallTaintacle.setPos(
                            spawnAtPos.getX() + 2 * random.nextFloat() - 1, spawnAtPos.getY(),
                            spawnAtPos.getZ() + 2 * random.nextFloat() - 1
                    );
                    level.addFreshEntity(smallTaintacle);
                }

                this.playSound(ThaumcraftSounds.TENTACLE, this.getSoundVolume(), this.getVoicePitch());
                if (!biome.is(ThaumcraftBiomeIDs.TAINT_ID)
                        && (spawnAtState.isAir())
                        && BlockUtils.isAdjacentToSolidBlock(level, spawnAtPos)
                        && level instanceof ServerLevel serverLevel
                ) {
                    Utils.setBiomeAt(
                            serverLevel, spawnAtPos, ThaumcraftBiomeLookups.biomeHolderForLevel(
                                    level, ThaumcraftBiomeIDs.TAINT_KEY
                            )
                    );
                    level.setBlockAndUpdate(
                            spawnAtPos,
                            level.random.nextInt(4) == 0
                                    ? ThaumcraftBlocks.ThaumcraftBlockInstances.TAINTED_GRASS()
                                    .defaultBlockState()
                                    : ThaumcraftBlocks.ThaumcraftBlockInstances.FIBROUS_TAINT()
                                    .defaultBlockState()
                    );
                }
            }
        }
    }

    @Override
    public ResourceKey<DamageType> replaceDamageTypeWith() {
        return ThaumcraftDamageSources.TENTACLE;
    }

    @Override
    public void move(MoverType moverType, Vec3 vec3) {
        if (vec3.y > 0){
            super.move(moverType, Vec3.ZERO);
        }else {
            super.move(moverType, new  Vec3(0, vec3.y, 0));
        }
    }

    @Override
    public void tick() {
        super.tick();
        var level = level();
        if (!level.isClientSide){
            if (tickCount % 20 == 0 && !level.getBiome(blockPosition()).is(ThaumcraftBiomeIDs.TAINT_ID)){
                this.hurt(level.damageSources().starve(), 1.0F);
            }
        }else {
            ClientTickContext.clientTick(this);
        }
    }
    public static class ClientTickContext {

        public float flailIntensity;
        public static void clientTick(TaintacleEntity taintacle) {
            var ctx = ((TaintacleEntityClientAccessor)taintacle).opentc4$getClientTickContext();

            var height = taintacle.getBbHeight();
            var target = taintacle.getTarget();
            var attackGoal = taintacle.attackGoalRef.get();
            if ((float)taintacle.tickCount > taintacle.getBbHeight() * 10.0F
                    && (taintacle.hurtTime > 0 || (attackGoal != null && attackGoal.ticksUntilNextAttack > 0) || taintacle.getTarget() != null
                    && taintacle.distanceToSqr(target) < height*height)
            ) {
                if (ctx.flailIntensity < 3.0F) {
                    ctx.flailIntensity += 0.2F;
                }
            } else if (ctx.flailIntensity > 1.0F) {
                ctx.flailIntensity -= 0.2F;
            }

            if ((float)taintacle.tickCount < height * 10.0F && taintacle.onGround()) {
                ClientFXUtils.tentacleAriseFX(taintacle);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        var target = this.getTarget();
        if (target == null) {
            this.lookAt(target,30,30);
        }
    }

    public static boolean checkTaintacleSpawnRules(
            EntityType<? extends TaintacleEntity> entityType,
            ServerLevelAccessor level,
            MobSpawnType mobSpawnType,
            BlockPos blockPos, 
            RandomSource randomSource
    ) {
        var ents = level.getEntities(
                (Entity) null,
                new AABB(blockPos).inflate(24.0F, 8.0F, 24.0F),
                e -> e.getType().equals(entityType)
        );
        if (!ents.isEmpty()) {
            return false;
        }
        boolean onTaint = level.getBlockState(blockPos).is(ThaumcraftBlocks.Tags.TAINTACLE_CAN_SPAWN)
                && level.getBiome(blockPos).is(ThaumcraftBiomeIDs.TAINT_ID);
        if (!onTaint) {
            return false;
        }
        return checkMonsterSpawnRules(entityType, level, mobSpawnType, blockPos, randomSource);
    }
    public static class TaintacleAttackGoal extends Goal {
        protected final TaintacleEntity mob;
        public int ticksUntilNextAttack;
        public final int attackInterval = 20;
        private long lastCanUseCheck;
        private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;

        public TaintacleAttackGoal(TaintacleEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            long l = this.mob.level().getGameTime();
            if (l - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = l;
                LivingEntity livingEntity = this.mob.getTarget();
                if (livingEntity == null) {
                    return false;
                } else if (!livingEntity.isAlive()) {
                    return false;
                } else {
                    return this.getAttackReachSqr(livingEntity) >= this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
            } else {
                return this.mob.isWithinRestriction(livingEntity.blockPosition())
                        && !(livingEntity instanceof Player && !(!livingEntity.isSpectator() && !((Player) livingEntity).isCreative()));
            }
        }

        @Override
        public void start() {
            this.mob.setAggressive(true);
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public void stop() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.mob.setTarget(null);
            }

            this.mob.setAggressive(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity != null) {
                this.mob.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);

                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingEntity);
            }
        }

        protected void checkAndPerformAttack(LivingEntity target) {
            if (this.ticksUntilNextAttack <= 0 && getAgitationState()) {
                var distanceSqr = this.mob.distanceToSqr(target);
                var heightSqr = this.mob.getBbHeight() * this.mob.getBbHeight();

                var selfBb = this.mob.getBoundingBox();
                var targetBb = target.getBoundingBox();
                if (distanceSqr <= heightSqr && targetBb.maxY > selfBb.minY && targetBb.minY < selfBb.maxY) {
                    this.resetAttackCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    this.mob.doHurtTarget(target);
                    this.mob.playSound(ThaumcraftSounds.TENTACLE, this.mob.getSoundVolume(), this.mob.getVoicePitch());
                } else if (distanceSqr > heightSqr && target.onGround() && this.mob.getType().is(ThaumcraftEntities.EntityTags.CAN_SPAWN_SMALL_TAINTACLE)) {
                    this.mob.spawnTentacles(target.blockPosition());
                }
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(attackInterval);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return this.adjustedTickDelay(attackInterval);
        }

        protected double getAttackReachSqr(LivingEntity livingEntity) {
            return this.mob.getBbWidth() * 2.0F * (this.mob.getBbWidth() * 2.0F) + livingEntity.getBbWidth();
        }

        public boolean getAgitationState() {
            var target = this.mob.getTarget();
            var reachDistance = this.mob.getBbHeight()*7;
            return target != null && target.distanceToSqr(this.mob) < (reachDistance * reachDistance);
        }
    }

    @Override
    protected float getSoundVolume() {
        return getBbHeight()/8.F;
    }

    @Override
    public float getVoicePitch() {
        return 1.3F - getBbHeight()/10.F;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource damageSource) {
        return ThaumcraftSounds.TENTACLE;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ThaumcraftSounds.TENTACLE;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ThaumcraftSounds.ROOTS;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    protected @Nullable EntityType<?> getSmallTaintacleType(){
        return ThaumcraftEntities.ThaumcraftEntityTypeInstances.SMALL_TAINTACLE();
    }
}
