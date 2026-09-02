package thaumcraft.common.entities.monster.cultists;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.DoorBreakingMonster;
import thaumcraft.common.entities.ai.goals.CultistHurtByTargetGoal;
import thaumcraft.common.entities.ai.goals.NearestAttackableTargetGoalWithExclude;
import thaumcraft.common.entities.ai.goals.ZombieLikeAttackGoal;

import static com.linearity.opentc4.Consts.CultistEntityTagAccessors.HOME_POS;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

public class CultistEntity extends DoorBreakingMonster {
    protected int restrictAreaSize = 8;
    public CultistEntity(EntityType<? extends CultistEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 10;
        this.setCanPickUpLoot(false);
        this.breakDoorGoal = new BreakDoorGoal(this, _ignored -> true);
        setCanBreakDoors(true);
    }
    public CultistEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.CULTIST(), level);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.ATTACK_DAMAGE,4)
                .add(MOVEMENT_SPEED,0.3);
    }

    @Override
    protected void registerGoals() {
        addLookingAtGoals();
        addBehaviourGoals();
    }

    private void addLookingAtGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new ZombieLikeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        addTargetGoals();
    }

    protected void addTargetGoals() {
        this.targetSelector.addGoal(1, new CultistHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoalWithExclude<>(this, Monster.class, CultistEntity.class, true));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        restrictTo(HOME_POS.readFromCompoundTag(compoundTag),restrictAreaSize);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        HOME_POS.writeToCompoundTag(compoundTag, getRestrictCenter());
    }

    public int getRestrictAreaSize() {
        return restrictAreaSize;
    }

    public void setRestrictAreaSize(int restrictAreaSize) {
        this.restrictAreaSize = restrictAreaSize;
        restrictTo(getRestrictCenter(),restrictAreaSize);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance) {
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(RandomSource randomSource, DifficultyInstance difficultyInstance) {
    }

}
