package thaumcraft.common.entities.monster.tainted;

import com.linearity.opentc4.mixinaccessors.cliententity.TaintedSwarmEntityClientAccessor;
import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.fx.migrated.particles.FXSwarm;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.lib.world.biomes.ThaumcraftBiomeIDs;

import java.util.ArrayList;
import java.util.List;

import static thaumcraft.common.entities.ThaumcraftEntities.handleTargetSelectorForTaintedMob;

public class TaintedSwarmEntity extends Monster {
    protected final boolean isClientSide;
    public TaintedSwarmEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.TAINTED_SWARM(), level);
    }
    public TaintedSwarmEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        isClientSide = level.isClientSide;
        this.moveControl = new TaintedSwarmEntityMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2,new TaintedSwarmReachEntityGoal(this));
        this.goalSelector.addGoal(4,new TaintedSwarmReachRandomPosGoal(this));
        handleTargetSelectorForTaintedMob(this,this.targetSelector);
    }

    public static class ClientTickContext {

        public final List<FXSwarm> swarm = new ArrayList<>();
        public static void tickEntity(TaintedSwarmEntity entity) {
            var level = entity.level();
            if (!(level instanceof ClientLevel clientLevel)) {
                return;
            }
            var ctx = ((TaintedSwarmEntityClientAccessor)entity).opentc4$getClientTickContext();
            for(int a = 0; a < ctx.swarm.size(); ++a) {
                if (ctx.swarm.get(a) == null || !(ctx.swarm.get(a)).isAlive()) {
                    ctx.swarm.remove(a);
                    break;
                }
            }

            if (ctx.swarm.size() < Math.max(ClientFXUtils.particleCount(25), 10)) {
                ctx.swarm.add(ClientFXUtils.swarmParticleFX(clientLevel, entity, 0.22F, 15.0F, 0.08F));
            }
        }
    }
    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0.6, 1));
        if (isClientSide) {
            ClientTickContext.tickEntity(this);
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 0.1F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 2);
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(DamageSource damageSource) {
        return ThaumcraftSounds.SWARM_ATTACK;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ThaumcraftSounds.SWARM_ATTACK;
    }
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    public Fallsounds getFallSounds() {
        return super.getFallSounds();
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return super.getSwimSplashSound();
    }

    @Override
    protected SoundEvent getSwimSound() {
        return super.getSwimSound();
    }

    @Override
    protected SoundEvent getDrinkingSound(ItemStack itemStack) {
        return super.getDrinkingSound(itemStack);
    }

    @Override
    protected SoundEvent getSwimHighSpeedSplashSound() {
        return super.getSwimHighSpeedSplashSound();
    }

    @Override
    public SoundEvent getEatingSound(ItemStack itemStack) {
        return super.getEatingSound(itemStack);
    }

    @Override
    public SoundSource getSoundSource() {
        return super.getSoundSource();
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    public static class TaintedSwarmEntityMoveControl extends MoveControl {
        public boolean isWandering = false;

        public TaintedSwarmEntityMoveControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (hasWanted()) {
                movingToPos(new Vec3(wantedX,wantedY,wantedZ),0.25 - (isWandering?0.1:0));
            }
        }

        public void movingToPos(Vec3 pos,double horizontalSpeedFactor){
            var selfPos = mob.position();
            var vecToPos = pos.subtract(selfPos);
            if (vecToPos.lengthSqr() < 0.1){
                this.operation = Operation.WAIT;
                return;
            }
            var newSpeed = this.mob.getDeltaMovement().scale(1-horizontalSpeedFactor)
                    .add(
                            Math.signum(vecToPos.x)*0.5*horizontalSpeedFactor,
                            Math.signum(vecToPos.y)*0.7*horizontalSpeedFactor,
                            Math.signum(vecToPos.z)*0.5*horizontalSpeedFactor
                    );
            mob.setDeltaMovement(
                    newSpeed
            );
            var currentYRot = this.mob.getYRot();
            var yRot = MathHelper.wrapAngleTo180_double(
                    (Math.atan2(newSpeed.z, newSpeed.x) * (double)180.0F / Math.PI) - 90.0F - currentYRot
            );
            this.mob.setYRot((float) (yRot + currentYRot));
        }
    }

    public static class TaintedSwarmReachEntityGoal extends Goal {
        public final TaintedSwarmEntity swarm;
        protected int attackTime = 0;
        public TaintedSwarmReachEntityGoal(TaintedSwarmEntity swarm){
            this.swarm = swarm;
        }
        @Override
        public boolean canUse() {
            return !this.swarm.isPassenger();
        }

        @Override
        public void tick() {
            super.tick();
            attackTime -= 1;
            if (swarm.moveControl instanceof TaintedSwarmEntityMoveControl swarmMoveControl) {
                var target = swarm.getTarget();
                if (target != null){
                    var targetPos = target.getEyePosition();
                    swarmMoveControl.isWandering = false;
                    swarmMoveControl.setWantedPosition(targetPos.x,targetPos.y,targetPos.z,1);
                    if (attackTime <= 0 && (
                            targetPos.distanceToSqr(swarm.position()) < 9
                            )
                    && target.getBoundingBox().minY < swarm.getBoundingBox().maxY
                            && target.getBoundingBox().maxY > swarm.getBoundingBox().minY
                    ){
                        attackTime = 10 + swarm.random.nextInt(5);


                        var motion = target.getDeltaMovement();
                        if (swarm.doHurtTarget(target)) {
                            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                        }
                        target.setDeltaMovement(motion);
                        target.setOnGround(true);
                        swarm.playSound(ThaumcraftSounds.SWARM_ATTACK, 0.3F, 0.9F + swarm.random.nextFloat() * 0.2F);
                    }
                }
            }
        }
    }
    public static class TaintedSwarmReachRandomPosGoal extends Goal {
        public final TaintedSwarmEntity swarm;
        public TaintedSwarmReachRandomPosGoal(TaintedSwarmEntity swarm){
            this.swarm = swarm;
        }
        @Override
        public boolean canUse() {
            return !this.swarm.isPassenger();
        }

        @Override
        public void tick() {
            super.tick();
            if (swarm.moveControl instanceof TaintedSwarmEntityMoveControl swarmMoveControl && !swarmMoveControl.hasWanted()) {
                var target = swarm.getTarget();
                if (target == null){
                    var targetPos = swarm.blockPosition().offset(
                            swarm.random.nextInt(15)-7,
                            swarm.random.nextInt(6)-2,
                            swarm.random.nextInt(15)-7
                    );
                    var level = swarm.level();
                    if (level.getBiome(targetPos).is(ThaumcraftBiomeIDs.TAINT_ID)){
                        swarmMoveControl.isWandering = true;
                        swarmMoveControl.setWantedPosition(targetPos.getX(),targetPos.getY(),targetPos.getZ(),1);
                    }
                }
            }
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor levelAccessor, MobSpawnType mobSpawnType) {
        int var4 = level().getLightEmission(blockPosition());
        return var4 <= this.random.nextInt(7) && super.checkSpawnRules(levelAccessor, mobSpawnType);
    }
}
