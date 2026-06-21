package thaumcraft.common.entities;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.frostfocus.FrostShardEntity;
import thaumcraft.common.entities.projectile.hellbatfocus.FireBatEntity;
import thaumcraft.common.entities.projectile.thrownitem.AlumentumEntity;
import thaumcraft.common.entities.projectile.firefocus.EmberEntity;
import thaumcraft.common.entities.projectile.firefocus.ExplosiveOrbEntity;
import thaumcraft.common.entities.projectile.thrownitem.TaintBottleEntity;
import thaumcraft.common.entities.projectile.shockfocus.ShockOrbEntity;

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
        public static final RegistrySupplier<EntityType<FireBatEntity>> SUPPLIER_FIRE_BAT = ENTITIES.register(
                "fire_bat",
                () -> EntityType.Builder.<FireBatEntity>of(FireBatEntity::new, MobCategory.MISC)
                        .sized(0.5F, 0.9F)
                        .clientTrackingRange(16)
                        .updateInterval(20)
                        .fireImmune()
                        .build("fire_bat")
        );
    }

    public static class EntityTags {
        public static final TagKey<EntityType<?>> UNDEAD = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation("minecraft","undead"));
        public static final TagKey<EntityType<?>> FERTILITY_LAMP_AFFECTIVE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"fertility_lamp_affective"));
        public static final TagKey<EntityType<?>> FERTILITY_LAMP_NOT_AFFECTIVE = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"fertility_lamp_not_affective"));
        public static final TagKey<EntityType<?>> TAINTED = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation(Thaumcraft.MOD_ID,"tainted_entity"));
    }

    public static void init(){
        ENTITIES.register();
        registerDefaultAttribute(ThaumcraftEntityTypeInstances.FIRE_BAT(),FireBatEntity.createAttributes().build());
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
}
