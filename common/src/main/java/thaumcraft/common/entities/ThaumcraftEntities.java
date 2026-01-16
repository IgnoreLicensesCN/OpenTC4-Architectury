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
                        .sized(0.1f, 0.1f)       // 实体大小
                        .clientTrackingRange(8)    // 客户端跟踪范围
                        .updateInterval(1)         // 客户端更新间隔
                        .build("alumentum")
        );
        static {
            ENTITIES.register();
        }
    }

    public static class Tags {
        public static final TagKey<EntityType<?>> UNDEAD = TagKey.create(Registries.ENTITY_TYPE,new ResourceLocation("minecraft","undead"));
    }

    public static void init(){

    }
}
