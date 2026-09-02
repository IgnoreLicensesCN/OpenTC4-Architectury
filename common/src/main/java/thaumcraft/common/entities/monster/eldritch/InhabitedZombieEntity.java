package thaumcraft.common.entities.monster.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.ITaintConvertableEntity;
import thaumcraft.common.entities.abstracts.ITaintRecoverableMob;
import thaumcraft.common.entities.monster.cultists.CultistEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

public class InhabitedZombieEntity extends Zombie implements ITaintConvertableEntity, ITaintRecoverableMob {
    public InhabitedZombieEntity(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    public InhabitedZombieEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.INHABITED_ZOMBIE(),level);
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, CultistEntity.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE,0);
    }
    @Override
    public boolean canConvertToTaintedMob() {
        return false;
    }

    @Override
    public void convertToTaintedMob() {

    }

    @Override
    public boolean canBeRecoveredFromTaintedMob() {
        return false;
    }

    @Override
    public void recoverFromTaintedMob() {

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ThaumcraftSounds.CRAB_TALK;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.HOSTILE_HURT;
    }

    public static boolean checkSpawnRules(EntityType<? extends Monster> arg, ServerLevelAccessor arg2, MobSpawnType arg3, BlockPos arg4, RandomSource arg5){
        return arg2.getEntitiesOfClass(
                InhabitedZombieEntity.class,
                AABB.ofSize(arg4.getCenter(),32,16,32)
        ).isEmpty() && checkMonsterSpawnRules(arg, arg2, arg3, arg4, arg5);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        float diff = difficultyInstance.getDifficulty() == Difficulty.HARD ? 0.9F : 0.6F;
        this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_PLATE_HELMET()));
        if (this.random.nextFloat() <= diff) {
            this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_PLATE_CHESTPLATE()));
        }

        if (this.random.nextFloat() <= diff) {
            this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_PLATE_LEGGINGS()));
        }
    }

    @Override
    protected void tickDeath() {
        var level = this.level();
        var selfX = getX();
        var selfY = getY();
        var selfZ = getZ();
        if (!level.isClientSide) {
            var crab = new EldritchCrabEntity(level);
            crab.setPos(selfX, selfY + (double)this.getEyeHeight(), selfZ);
            crab.setXRot(getXRot());
            crab.setYRot(getYRot());
            crab.setHelm(true);
            level.addFreshEntity(crab);
            if ((this.isAlwaysExperienceDropper() || this.lastHurtByPlayerTime > 0)
                    && this.shouldDropLoot()
                    && level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                int i = this.getExperienceReward();

                while(i > 0) {
                    int j = ExperienceOrb.getExperienceValue(i);
                    i -= j;
                    level.addFreshEntity(new ExperienceOrb(level, selfX,selfY,selfZ, j));
                }
            }
        }

        for(int i = 0; i < 20; ++i) {
            double d2 = this.random.nextGaussian() * 0.02;
            double d0 = this.random.nextGaussian() * 0.02;
            double d1 = this.random.nextGaussian() * 0.02;
            var width = getBbWidth();
            var height = getBbHeight();
            level.addParticle(
                    ParticleTypes.EXPLOSION,
                    selfX + (this.random.nextFloat() * 2.0F -1) * width,
                    selfY + (this.random.nextFloat() * height),
                    selfZ + (this.random.nextFloat() * 2.0F -1) * width,
                    d2, d0, d1);
        }

        if (!level.isClientSide() && !this.isRemoved()) {
            level.broadcastEntityEvent(this, (byte)60);
            this.remove(RemovalReason.KILLED);
        }
    }
}
