package thaumcraft.common.entities.abstracts.boss;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.entities.abstracts.ICustomSpecialDropEntity;
import thaumcraft.common.items.ThaumcraftItemInstances;

import java.util.List;

import static com.linearity.opentc4.Consts.ThaumcraftBossTagAccessors.*;
import static thaumcraft.common.entities.ThaumcraftEntities.EntityTags.ELDRITCH;
import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.DMG_BUFF_UUIDS;
import static thaumcraft.common.lib.utils.EntityUtils.ThaumcraftAttributeCategoryInstances.HP_BUFF_UUIDS;

@UtilityLikeAbstraction
public interface IThaumcraftBossEntity extends ICustomSpecialDropEntity,IBossEntity {


    default int thaumcraftBoss$getAnger() {
        return getEntityData().get(thaumcraftBoss$getAngerDataID());
    }

    default void thaumcraftBoss$setAnger(int value) {
        getEntityData().set(thaumcraftBoss$getAngerDataID(), value);
    }

    @Override
    default int thaumcraftBoss$getInvulnerableTicks() {
        return getEntityData().get(thaumcraftBoss$getInvulnerableTicksDataID());
    }

    @Override
    default void thaumcraftBoss$setInvulnerableTicks(int i) {
        getEntityData().set(thaumcraftBoss$getInvulnerableTicksDataID(), i);
    }

    @SuppressWarnings("resource")
    //return modified damage
    default float beforeHurt(DamageSource source, float damage) {
        if (!level().isClientSide) {
            thaumcraftBoss$updateAggroForDamage(source, damage);

            if (damage > 35.0F) {
                if (this.thaumcraftBoss$getAnger() == 0) {
                    this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, (int)(damage / 15.0F)));
                    this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, (int)(damage / 40.0F)));
                    this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, (int)(damage / 40.0F)));
                    this.thaumcraftBoss$setAnger(200);

                    if (source.getEntity() != null && source.getEntity() instanceof Player player) {
                        player.sendSystemMessage(this.getDisplayName().copy().append( Component.translatable("tc.boss.enrage")));
                    }
                }

                damage = 35.0F;
            }
        }
        return damage;
    }

    default void thaumcraftBoss$updateAggroForDamage(DamageSource source, float damage) {
        if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
            int targetID = source.getEntity().getId();
            this.thaumcraftBoss$getAggroRecords().merge(targetID, damage,Float::sum);
        }
    }

    default boolean thaumcraftBoss$isAlliedTo(Entity entity) {
        return entity.getType().is(ELDRITCH);
    }

    default void thaumcraftBoss$customDeathLoot(DamageSource source, int i, boolean bl) {
        this.spawnAtLocation(ThaumcraftItemInstances.RARE_LOOT_BAG().getDefaultInstance(), 1.5F);
    }

    default @Unmodifiable List<ItemStack> generateSpecialDrops() {
        return List.of(new ItemStack(ThaumcraftItemInstances.PRIME_PEARL()));
    }

    @Override
    default void thaumcraftBoss$invulnerableTick() {
        IBossEntity.super.thaumcraftBoss$invulnerableTick();
        if (this.thaumcraftBoss$getTickCount() % 10 == 0) {
            this.heal(10.01F * (10.F / (float) thaumcraftBoss$getInvulnerableTickLimit()));
        }
    }

    @Override
    @SuppressWarnings("resource")
    default void thaumcraftBoss$vulnerableTick() {
        IBossEntity.super.thaumcraftBoss$vulnerableTick();
        var random = getRandom();
        var level = level();

        if (this.thaumcraftBoss$getAnger() > 0) {
            this.thaumcraftBoss$setAnger(this.thaumcraftBoss$getAnger() - 1);
        }
        if (level.isClientSide) {
            if (random.nextInt(15) == 0 && this.thaumcraftBoss$getAnger() > 0) {
                double d0 = random.nextGaussian() * 0.02;
                double d1 = random.nextGaussian() * 0.02;
                double d2 = random.nextGaussian() * 0.02;
                level.addParticle(ParticleTypes.ANGRY_VILLAGER,
                        this.getX() + (double) (random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / (double) 2.0F,
                        this.getBoundingBox().minY + (double) this.getBbHeight() + (double) random.nextFloat() * (double) 0.5F,
                        this.getZ() + (double) (random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / (double) 2.0F,
                        d0, d1, d2);
            }
        } else {

            if (this.thaumcraftBoss$getTickCount() % 30 == 0) {
                this.heal(1.0F);
            }
            var target = this.getTarget();
            if (target != null && this.thaumcraftBoss$getTickCount() % 20 == 0) {
                IntList entityIDNotPickedNewTarget = new IntArrayList();
                int players = 0;
                int currentTargetID = target.getId();
                var aggroForCurrentTarget = this.thaumcraftBoss$getAggroRecords().getOrDefault(currentTargetID, 0);
                var lastPickedAggro = aggroForCurrentTarget;
                LivingEntity newTarget = null;

                for (var aggroMarkedTargetEntry : this.thaumcraftBoss$getAggroRecords().int2FloatEntrySet()) {
                    int aggroMarkedTargetID = aggroMarkedTargetEntry.getIntKey();
                    float targetedAggro = aggroMarkedTargetEntry.getFloatValue();
                    if (targetedAggro > aggroForCurrentTarget + 25
                            && (double) targetedAggro > (double) aggroForCurrentTarget * 1.1
                            && targetedAggro > lastPickedAggro) {
                        var couldBeNewTarget = level.getEntity(currentTargetID);
                        if (couldBeNewTarget != null) {
                            if (couldBeNewTarget instanceof LivingEntity livingEntity) {
                                newTarget = livingEntity;
                            }
                        }
                        if (newTarget != null
                                && newTarget.isAlive()
                                && !(this.distanceToSqr(newTarget) >  16384)
                        ) {
                            currentTargetID = aggroMarkedTargetID;
                            lastPickedAggro = targetedAggro;
                            if (newTarget instanceof Player) {
                                players += 1;
                            }
                        } else {
                            entityIDNotPickedNewTarget.add(aggroMarkedTargetID);
                        }
                    }
                }

                this.thaumcraftBoss$getAggroRecords().keySet().removeAll(entityIDNotPickedNewTarget);

                if (newTarget != null && currentTargetID != target.getId()) {
                    this.setTarget(newTarget);
                }

                float om = this.getMaxHealth();
                var healthAttributeInstance = this.getAttribute(Attributes.MAX_HEALTH);
                var damageAttributeInstance = this.getAttribute(Attributes.ATTACK_DAMAGE);

                if (healthAttributeInstance != null){
                    for (int a = 0; a < Math.min(5, players - 1); ++a) {
                        healthAttributeInstance.removeModifier(HP_BUFF_UUIDS[a]);
                        healthAttributeInstance.addPermanentModifier(new AttributeModifier(
                                HP_BUFF_UUIDS[a],
                                "HEALTH BUFF "+a, 50.0, AttributeModifier.Operation.ADDITION
                        ));
                    }
                }
                if (damageAttributeInstance != null){
                    for (int a = 0; a < Math.min(5, players - 1); ++a) {
                        damageAttributeInstance.removeModifier(DMG_BUFF_UUIDS[a]);
                        damageAttributeInstance.addPermanentModifier(new AttributeModifier(
                                DMG_BUFF_UUIDS[a],
                                "DAMAGE BUFF "+a, 0.5, AttributeModifier.Operation.ADDITION
                        ));
                    }
                }
                double mm = this.getMaxHealth() / om;
                this.setHealth((float) ((double) this.getHealth() * mm));
            }
        }
    }

    default void thaumcraftBoss$beforeFinalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        if (compoundTag != null) {
            HOME_POS.writeToCompoundTag(compoundTag, blockPosition());
            HOME_SIZE.writeIntToCompoundTag(compoundTag, getDefaultRestrictionSize());
        } else {
            restrictTo(blockPosition(), getDefaultRestrictionSize());
        }
    }

    default void thaumcraftBoss$readAdditionalSaveData(CompoundTag tag) {
        if (this.hasCustomName()) {
            this.thaumcraftBoss$getBossEvent().setName(this.getDisplayName());
        }
        this.thaumcraftBoss$setInvulnerableTicks(INVULNERABLE_TICKS.readIntFromCompoundTag(tag));
        restrictTo(HOME_POS.readFromCompoundTag(tag), HOME_SIZE.readIntFromCompoundTag(tag));
    }
    default void thaumcraftBoss$addAdditionalSaveData(CompoundTag tag) {
        INVULNERABLE_TICKS.writeIntToCompoundTag(tag, thaumcraftBoss$getInvulnerableTicks());
        HOME_POS.writeToCompoundTag(tag, getRestrictCenter());
        HOME_SIZE.writeIntToCompoundTag(tag,(int) getRestrictRadius());
    }

    default void thaumcraftBoss$defineSynchedData(){
        getEntityData().define(thaumcraftBoss$getAngerDataID(),0);
        getEntityData().define(thaumcraftBoss$getInvulnerableTicksDataID(),0);
    }

    default int getDefaultRestrictionSize(){
        return 24;
    }

    int thaumcraftBoss$getInvulnerableTickLimit();
    //entity hit this->aggro value increase for entity
    //attack entity with highest aggro
    Int2FloatMap thaumcraftBoss$getAggroRecords();
    EntityDataAccessor<Integer> thaumcraftBoss$getInvulnerableTicksDataID();
    EntityDataAccessor<Integer> thaumcraftBoss$getAngerDataID();

    BlockPos blockPosition();
    float getRestrictRadius();
    BlockPos getRestrictCenter();
    void restrictTo(BlockPos pos,int size);
    boolean hasCustomName();
    Level level();
    SynchedEntityData getEntityData();
    boolean addEffect(MobEffectInstance effect);
    @SuppressWarnings("UnusedReturnValue")
    ItemEntity spawnAtLocation(ItemStack stack, float f);
    RandomSource getRandom();
    void heal(float f);
    float getBbWidth();
    float getBbHeight();
    double getX();
    double getY();
    double getZ();
    AABB getBoundingBox();
    LivingEntity getTarget();
    void setTarget(LivingEntity target);
    void setHealth(float f);
    double distanceToSqr(Entity entity);
    AttributeInstance getAttribute(Attribute attribute);
}
