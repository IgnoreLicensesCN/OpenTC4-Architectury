package thaumcraft.common.entities.monster.zombies;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.linearity.opentc4.Consts.GiantBrainyZombieEntityTagAccessors.ANGER;
import static thaumcraft.common.entities.ThaumcraftEntities.ThaumcraftEntityTypeInstances.GIANT_BRAINY_ZOMBIE;

public class GiantBrainyZombieEntity extends BrainyZombieEntity {
    private static final EntityDataAccessor<Float> ID_ANGER = SynchedEntityData.defineId(GiantBrainyZombieEntity.class, EntityDataSerializers.FLOAT);
    public GiantBrainyZombieEntity(EntityType<? extends BrainyZombieEntity> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 15;
    }

    public GiantBrainyZombieEntity(Level level) {
        this(GIANT_BRAINY_ZOMBIE(),level);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        var oldDimensions = super.getDimensions(pose);
        return oldDimensions.scale((1.2F + this.getAnger()));
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.4F));
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return BrainyZombieEntity.createAttributes().add(Attributes.MAX_HEALTH, 60).add(Attributes.ATTACK_DAMAGE,7);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_ANGER, 1.f);
    }

    public float getAnger() {
        return this.entityData.get(ID_ANGER);
    }
    public void setAnger(float value) {
        this.entityData.set(ID_ANGER, value);
        refreshAnger();
    }

    @Override
    public void tick() {
        super.tick();
        var currentAnger = this.getAnger();
        if (currentAnger > 1){
            this.setAnger(currentAnger - 0.002F);
        }
    }

    public void refreshAnger() {
        reapplyPosition();
        refreshDimensions();
        refreshDamageForAnger();
    }

    public static final UUID GIANT_BRAINY_ZOMBIE_DAMAGE_MODIFIER_FROM_ANGER = UUID.fromString("b59a769f-5767-4095-916f-f5a1d1776f1f");
    public void refreshDamageForAnger(){
        var damageAttributeInstance = getAttribute(Attributes.ATTACK_DAMAGE);
        if (damageAttributeInstance != null) {
            damageAttributeInstance.removeModifier(GIANT_BRAINY_ZOMBIE_DAMAGE_MODIFIER_FROM_ANGER);
            damageAttributeInstance.addPermanentModifier(new AttributeModifier(
                    GIANT_BRAINY_ZOMBIE_DAMAGE_MODIFIER_FROM_ANGER,
                    "giant_zombie_damage_modifier_by_anger",
                    (this.getAnger() - 1.0F) * 5.0F,
                    AttributeModifier.Operation.ADDITION
                    )
            );
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        this.setAnger(Math.min(2.0F, this.getAnger() + 0.1F));
        return super.hurt(damageSource, f);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setAnger(ANGER.readFloatFromCompoundTag(compoundTag));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ANGER.writeFloatToCompoundTag(compoundTag, this.getAnger());
    }
}
