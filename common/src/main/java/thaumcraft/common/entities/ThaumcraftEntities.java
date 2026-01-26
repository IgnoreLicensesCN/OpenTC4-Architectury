package thaumcraft.common.entities;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.EntityAlumentum;

public class ThaumcraftEntities {

    public static EntityType<EntityAlumentum> ALUMENTUM = Registry.SUPPLIER_ALUMENTUM.get();

    public static class Registry {
        public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Thaumcraft.MOD_ID,
                Registries.ENTITY_TYPE
        );
        public static final RegistrySupplier<EntityType<EntityAlumentum>> SUPPLIER_ALUMENTUM = ENTITIES.register("alumentum",
                () -> EntityType.Builder.<EntityAlumentum>of(EntityAlumentum::new, MobCategory.MISC)
                        .sized(0.1f, 0.1f)
                        .clientTrackingRange(8)
                        .updateInterval(1)
                        .build("alumentum")
        );
        static {
            ENTITIES.register();
        }
    }

    public static class EntityTags {
        public static final TagKey<EntityType<?>> UNDEAD = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation("minecraft","undead"));
    }

    public static void init(){

    }
}
