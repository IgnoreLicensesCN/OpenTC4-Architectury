package thaumcraft.common.entities.monster.cultists;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.abstracts.CultistClericLikeRangedAttackMob;
import thaumcraft.common.entities.ai.goals.CultistClericFocusAltarGoal;
import thaumcraft.common.entities.ai.goals.CultistClericHurtByTargetGoal;
import thaumcraft.common.entities.ai.goals.NearestAttackableTargetGoalWithExclude;
import thaumcraft.common.entities.ai.goals.ZombieLikeAttackGoal;
import thaumcraft.common.items.ThaumcraftItemInstances;

import static com.linearity.opentc4.Consts.CultistClericEntityTagAccessors.IS_RITUALIST;

public class CultistClericEntity extends CultistEntity implements CultistClericLikeRangedAttackMob {
    private static final EntityDataAccessor<Boolean> DATA_RITUALIST_FLAG = SynchedEntityData.defineId(CultistClericEntity.class, EntityDataSerializers.BOOLEAN);
    public CultistClericEntity(EntityType<? extends CultistClericEntity> entityType, Level level) {
        super(entityType, level);
    }

    public CultistClericEntity(Level level) {
        this(ThaumcraftEntities.ThaumcraftEntityTypeInstances.CULTIST_CLERIC(),level);
    }
    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return CultistEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 30);
    }
    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new CultistClericFocusAltarGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0F, 20, 40, 24.0F));
        this.goalSelector.addGoal(2, new ZombieLikeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.targetSelector.addGoal(1, new CultistClericHurtByTargetGoal(this ));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoalWithExclude<>(this, Monster.class, CultistEntity.class, true));
    }

    protected void populateDefaultEquipmentEnchantments(RandomSource randomSource, DifficultyInstance difficultyInstance) {
        this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_ROBE_HELMET()));
        this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_ROBE_CHESTPLATE()));
        this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_ROBE_LEGGINGS()));
        if (randomSource.nextFloat() < (difficultyInstance.getDifficulty() == Difficulty.HARD ? 0.3F : 0.1F)) {
            this.equipItemIfPossible(new ItemStack(ThaumcraftItemInstances.CULTIST_BOOTS()));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && this.getIsRitualist() && homePos != null) {

            double d0 = homePos.getX() + 0.5F - this.getX();
            double d1 = homePos.getY() + 1.5F - (this.getY() + (double)this.getEyeHeight());
            double d2 = homePos.getZ() + 0.5F - this.getZ();
            double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
            float f = (float)(Math.atan2(d2, d0) * (double)180.0F / Math.PI) - 90.0F;
            float f1 = (float)(-(Math.atan2(d1, d3) * (double)180.0F / Math.PI));
            this.setXRot(this.updateRotation(this.getXRot(), f1, 10.0F));
            this.setYRot(this.updateRotation(this.getYRot(), f1, 40));
        }

    }

    private float updateRotation(float p_75652_1_, float p_75652_2_, float p_75652_3_) {
        float f3 = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);
        if (f3 > p_75652_3_) {
            f3 = p_75652_3_;
        }

        if (f3 < -p_75652_3_) {
            f3 = -p_75652_3_;
        }

        return p_75652_1_ + f3;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ThaumcraftSounds.CHANT;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 500;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.getIsRitualist();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_RITUALIST_FLAG, false);
    }

    public boolean getIsRitualist(){
        return this.entityData.get(DATA_RITUALIST_FLAG);
    }
    public void setIsRitualist(boolean flag){
        this.entityData.set(DATA_RITUALIST_FLAG, flag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setIsRitualist(IS_RITUALIST.readBooleanFromCompoundTag(compoundTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        IS_RITUALIST.writeBooleanToCompoundTag(compoundTag,getIsRitualist());
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        boolean result = super.hurt(damageSource, f);
        if (result) {
            this.setIsRitualist(false);
        }
        return result;
    }

    @Override
    public void performRangedAttack(LivingEntity living, float f) {
        this.swing(InteractionHand.MAIN_HAND);
        CultistClericLikeRangedAttackMob.super.performRangedAttack(living,f);
    }

    @Override
    public LivingEntity getProjectileThrower() {
        return this;
    }
}
