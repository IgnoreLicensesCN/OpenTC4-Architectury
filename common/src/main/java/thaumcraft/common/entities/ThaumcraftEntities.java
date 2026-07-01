package thaumcraft.common.entities;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.ai.goals.DelayControllableMeleeAttackGoal;
import thaumcraft.common.entities.monster.tainted.*;
import thaumcraft.common.entities.projectile.frostfocus.FrostShardEntity;
import thaumcraft.common.entities.projectile.hellbatfocus.FireBatEntity;
import thaumcraft.common.entities.projectile.pechfocus.PechBlastEntity;
import thaumcraft.common.entities.projectile.primalfocus.PrimalOrbEntity;
import thaumcraft.common.entities.projectile.thrownitem.AlumentumEntity;
import thaumcraft.common.entities.projectile.firefocus.EmberEntity;
import thaumcraft.common.entities.projectile.firefocus.ExplosiveOrbEntity;
import thaumcraft.common.entities.projectile.thrownitem.TaintBottleEntity;
import thaumcraft.common.entities.projectile.shockfocus.ShockOrbEntity;
import thaumcraft.common.lib.effects.ThaumcraftEffects;

import java.util.IdentityHashMap;

import static com.linearity.opentc4.mixin.DefaultAttributesAccessor.opentc4$getSuppliers;
import static com.linearity.opentc4.mixin.DefaultAttributesAccessor.opentc4$setSuppliers;
import static thaumcraft.common.entities.ThaumcraftEntities.Registry.ENTITIES;

public class ThaumcraftEntities {

    public static class ThaumcraftEntityTypeInstances {

        public static EntityType<AlumentumEntity> ALUMENTUM() {
            return Registry.SUPPLIER_ALUMENTUM.get();
        }
        public static EntityType<TaintBottleEntity> TAINT_BOTTLE() {
            return Registry.SUPPLIER_TAINT_BOTTLE.get();
        }
        public static EntityType<SpecialItemEntity> SPECIAL_ITEM() {
            return Registry.SUPPLIER_SPECIAL_ITEM.get();
        }
        public static EntityType<ExplosiveOrbEntity> EXPLOSIVE_ORB() {
            return Registry.SUPPLIER_EXPLOSIVE_ORB.get();
        }
        public static EntityType<EmberEntity> EMBER() {
            return Registry.SUPPLIER_EMBER.get();
        }
        public static EntityType<ShockOrbEntity> SHOCK_ORB() {
            return Registry.SUPPLIER_SHOCK_ORB.get();
        }
        public static EntityType<FrostShardEntity> FROST_SHARD() {
            return Registry.SUPPLIER_FROST_SHARD.get();
        }
        public static EntityType<FireBatEntity> FIRE_BAT() {
            return Registry.SUPPLIER_FIRE_BAT.get();
        }
        public static EntityType<PechBlastEntity> PECH_BLAST() {
            return  Registry.SUPPLIER_PECH_BLAST.get();
        }
        public static EntityType<PrimalOrbEntity> PRIMAL_ORB() {
            return Registry.SUPPLIER_PRIMAL_ORB.get();
        }
        public static EntityType<TaintedCreeperEntity> TAINTED_CREEPER() {
            return Registry.SUPPLIER_TAINTED_CREEPER.get();
        }
        public static EntityType<TaintedVillagerEntity> TAINTED_VILLAGER() {
            return Registry.SUPPLIER_TAINTED_VILLAGER.get();
        }
        public static EntityType<TaintedCowEntity> TAINTED_COW() {
            return Registry.SUPPLIER_TAINTED_COW.get();
        }
        public static EntityType<TaintedSheepEntity>  TAINTED_SHEEP() {
            return Registry.SUPPLIER_TAINTED_SHEEP.get();
        }
        public static EntityType<TaintedChickenEntity> TAINTED_CHICKEN() {
            return Registry.SUPPLIER_TAINTED_CHICKEN.get();
        }
        public static EntityType<TaintedPigEntity> TAINTED_PIG() {
            return Registry.SUPPLIER_TAINTED_PIG.get();
        }
        public static EntityType<TaintedSpiderEntity> TAINTED_SPIDER() {
            return Registry.SUPPLIER_TAINTED_SPIDER.get();
        }
    }

    public static class Registry {
        public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
                Thaumcraft.MOD_ID,
                Registries.ENTITY_TYPE
        );
        public static final RegistrySupplier<EntityType<AlumentumEntity>> SUPPLIER_ALUMENTUM = ENTITIES.register("alumentum",
                () -> EntityType.Builder.<AlumentumEntity>of(AlumentumEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("alumentum")
        );
        public static final RegistrySupplier<EntityType<TaintBottleEntity>> SUPPLIER_TAINT_BOTTLE = ENTITIES.register("taint_bottle",
                () -> EntityType.Builder.<TaintBottleEntity>of(TaintBottleEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("taint_bottle")
        );
        public static final RegistrySupplier<EntityType<SpecialItemEntity>> SUPPLIER_SPECIAL_ITEM = ENTITIES.register("special_item",
                () -> EntityType.Builder.<SpecialItemEntity>of(SpecialItemEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("special_item")
        );
        public static final RegistrySupplier<EntityType<ExplosiveOrbEntity>> SUPPLIER_EXPLOSIVE_ORB = ENTITIES.register("explosive_orb",
                () -> EntityType.Builder.<ExplosiveOrbEntity>of(ExplosiveOrbEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("explosive_orb")
        );
        public static final RegistrySupplier<EntityType<EmberEntity>> SUPPLIER_EMBER = ENTITIES.register("ember",
                () -> EntityType.Builder.<EmberEntity>of(EmberEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("ember")
        );
        public static final RegistrySupplier<EntityType<ShockOrbEntity>> SUPPLIER_SHOCK_ORB = ENTITIES.register("shock_orb",
                () -> EntityType.Builder.<ShockOrbEntity>of(ShockOrbEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("shock_orb")
        );
        public static final RegistrySupplier<EntityType<FrostShardEntity>> SUPPLIER_FROST_SHARD = ENTITIES.register("frost_shard",
                () -> EntityType.Builder.<FrostShardEntity>of(FrostShardEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("frost_shard")
        );
        public static final RegistrySupplier<EntityType<PechBlastEntity>> SUPPLIER_PECH_BLAST = ENTITIES.register("pech_blast",
                () -> EntityType.Builder.<PechBlastEntity>of(PechBlastEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("pech_blast")
        );
        public static final RegistrySupplier<EntityType<PrimalOrbEntity>> SUPPLIER_PRIMAL_ORB = ENTITIES.register("primal_orb",
                () -> EntityType.Builder.<PrimalOrbEntity>of(PrimalOrbEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(20)
                        .build("primal_orb")
        );
        public static final RegistrySupplier<EntityType<FireBatEntity>> SUPPLIER_FIRE_BAT = ENTITIES.register(
                "fire_bat",
                () -> EntityType.Builder.<FireBatEntity>of(FireBatEntity::new, MobCategory.MONSTER)
                        .sized(0.5F, 0.9F)
                        .clientTrackingRange(16)
                        .updateInterval(20)
                        .fireImmune()
                        .build("fire_bat")
        );
        public static final RegistrySupplier<EntityType<TaintedCreeperEntity>> SUPPLIER_TAINTED_CREEPER = ENTITIES.register(
                "tainted_creeper",
                () -> EntityType.Builder.<TaintedCreeperEntity>of(TaintedCreeperEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.7F).clientTrackingRange(8)
                        .build("tainted_creeper")
        );
        public static final RegistrySupplier<EntityType<TaintedVillagerEntity>> SUPPLIER_TAINTED_VILLAGER = ENTITIES.register(
                "tainted_villager",
                () -> EntityType.Builder.<TaintedVillagerEntity>of(TaintedVillagerEntity::new, MobCategory.MONSTER)
                        .sized(0.6F, 1.95F).clientTrackingRange(10)
                        .build("tainted_villager")
        );
        public static final RegistrySupplier<EntityType<TaintedCowEntity>> SUPPLIER_TAINTED_COW = ENTITIES.register(
                "tainted_cow",
                () -> EntityType.Builder.<TaintedCowEntity>of(TaintedCowEntity::new, MobCategory.CREATURE)
                        .sized(0.9F, 1.4F).clientTrackingRange(10)
                        .build("tainted_cow")
        );
        public static final RegistrySupplier<EntityType<TaintedSheepEntity>> SUPPLIER_TAINTED_SHEEP = ENTITIES.register(
                "tainted_sheep",
                () -> EntityType.Builder.<TaintedSheepEntity>of(TaintedSheepEntity::new, MobCategory.CREATURE)
                        .sized(0.9F, 1.3F).clientTrackingRange(10)
                        .build("tainted_sheep")
        );
        public static final RegistrySupplier<EntityType<TaintedChickenEntity>> SUPPLIER_TAINTED_CHICKEN = ENTITIES.register(
                "tainted_chicken",
                () -> EntityType.Builder.<TaintedChickenEntity>of(TaintedChickenEntity::new, MobCategory.CREATURE)
                        .sized(0.4F, 0.7F).clientTrackingRange(10)
                        .build("tainted_chicken")
        );
        public static final RegistrySupplier<EntityType<TaintedPigEntity>> SUPPLIER_TAINTED_PIG = ENTITIES.register(
                "tainted_pig",
                () -> EntityType.Builder.<TaintedPigEntity>of(TaintedPigEntity::new, MobCategory.CREATURE)
                        .sized(0.9F, 0.9F).clientTrackingRange(10)
                        .build("tainted_pig")
        );
        public static final RegistrySupplier<EntityType<TaintedSpiderEntity>> SUPPLIER_TAINTED_SPIDER = ENTITIES.register(
                "tainted_spider",
                () -> EntityType.Builder.<TaintedSpiderEntity>of(TaintedSpiderEntity::new, MobCategory.MONSTER)
                        .sized(0.4F, 0.3F).clientTrackingRange(8)
                        .build("tainted_spider")
        );
    }

    public static class EntityTags {
        public static final TagKey<EntityType<?>> UNDEAD = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation("minecraft","undead"));
        public static final TagKey<EntityType<?>> FERTILITY_LAMP_AFFECTIVE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"fertility_lamp_affective"));
        public static final TagKey<EntityType<?>> FERTILITY_LAMP_NOT_AFFECTIVE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"fertility_lamp_not_affective"));
        public static final TagKey<EntityType<?>> TAINTED = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"tainted_entity"));
        public static final TagKey<EntityType<?>> ELDRITCH = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"eldritch_entity"));
        public static final TagKey<EntityType<?>> NOT_TAINT_CONVERTABLE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"not_taint_convertable"));
    }

    public static void init(){
        ENTITIES.register();
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.FIRE_BAT(),FireBatEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_CREEPER(),TaintedCreeperEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_COW(),TaintedCowEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_CHICKEN(),TaintedChickenEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_SHEEP(),TaintedSheepEntity.createAttributes().build());
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.TAINTED_PIG(),TaintedPigEntity.createAttributes().build());
    }

    public static void registerDefaultAttribute(EntityType<? extends LivingEntity> entityType,AttributeSupplier attributeSupplier){
        ensureAttributeSuppliersModifiable();
        opentc4$getSuppliers().put(entityType, attributeSupplier);
    }

    public static void ensureAttributeSuppliersModifiable(){
        var gotMap = opentc4$getSuppliers();
        if (!(gotMap instanceof IdentityHashMap<EntityType<? extends LivingEntity>, AttributeSupplier>)){
            opentc4$setSuppliers(new IdentityHashMap<>(gotMap));
        }
    }

    public static boolean taintedMobWontAttack(LivingEntity entity){
        return entity.getType().is(EntityTags.TAINTED);
    }

    public static void handleTargetSelectorForTaintedMob(PathfinderMob mob, GoalSelector targetSelector){
        targetSelector.addGoal(1, new HurtByTargetGoal(mob));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, Player.class, true, living -> !taintedMobWontAttack(living)));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, IronGolem.class, true, living -> !taintedMobWontAttack(living)));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(mob, Villager.class, true, living -> !taintedMobWontAttack(living)));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(mob, Animal.class, true, living -> !taintedMobWontAttack(living)));
    }
    public static void handleGoalsForTaintedMob(PathfinderMob mob, GoalSelector goalSelector) {
        goalSelector.getAvailableGoals().removeIf(wrapped -> {
            var goal = wrapped.getGoal();
            return goal instanceof PanicGoal
                    || goal instanceof TemptGoal
                    || goal instanceof BreedGoal
                    || goal instanceof FollowParentGoal;
        });
        goalSelector.addGoal(2, new DelayControllableMeleeAttackGoal(mob, 1.0F, false).setAttackInterval(10));
    }

    public static boolean usualCanConvertToTaintedMob(LivingEntity living) {
        return living.hasEffect(ThaumcraftEffects.ThaumcraftEffectTypeInstances.FLUX_TAINT()) && !living.getType().is(EntityTags.TAINTED) && !living.getType().is(EntityTags.NOT_TAINT_CONVERTABLE);
    }
    public static <TaintedType extends NotTaintedType,NotTaintedType extends Entity> void  usualTaintedMobConversion(NotTaintedType notTainted,EntityType<TaintedType> taintedEntityType){

        var level = notTainted.level();
        var taintedSelf = taintedEntityType.create(level);
        var tags = new CompoundTag();

        notTainted.saveWithoutId(tags);
        tags.putUUID("UUID", taintedSelf.getUUID());
        taintedSelf.load(tags);
        if (taintedSelf instanceof LivingEntity living && notTainted instanceof LivingEntity notTaintedLiving){
            living.setHealth(living.getMaxHealth() *(notTaintedLiving.getHealth() / notTaintedLiving.getMaxHealth()));
        }

        level.addFreshEntity(taintedSelf);
        notTainted.discard();
    }
}
