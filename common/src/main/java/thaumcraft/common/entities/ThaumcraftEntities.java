package thaumcraft.common.entities;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.AlumentumEntity;
import thaumcraft.common.entities.projectile.TaintBottleEntity;

import static thaumcraft.common.entities.ThaumcraftEntities.Registry.ENTITIES;

public class ThaumcraftEntities {

    public static class ThaumcraftEntityTypeInstances {

        public static EntityType<AlumentumEntity> ALUMENTUM() {
            return Registry.SUPPLIER_ALUMENTUM.get();
        }
        public static EntityType<TaintBottleEntity> TAINT_BOTTLE() {
            return Registry.SUPPLIER_TAINT_BOTTLE.get();
        }

    }

    public static class Registry {
        public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Thaumcraft.MOD_ID,
                Registries.ENTITY_TYPE
        );
        public static final RegistrySupplier<EntityType<AlumentumEntity>> SUPPLIER_ALUMENTUM = ENTITIES.register("alumentum",
                () -> EntityType.Builder.<AlumentumEntity>of(AlumentumEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(1)
                        .build("alumentum")
        );
        public static final RegistrySupplier<EntityType<TaintBottleEntity>> SUPPLIER_TAINT_BOTTLE = ENTITIES.register("taint_bottle",
                () -> EntityType.Builder.<TaintBottleEntity>of(TaintBottleEntity::new, MobCategory.MISC)
                        .sized(0.25F, 0.25F)
                        .clientTrackingRange(10)
                        .updateInterval(1)
                        .build("taint_bottle")
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
    }
}
