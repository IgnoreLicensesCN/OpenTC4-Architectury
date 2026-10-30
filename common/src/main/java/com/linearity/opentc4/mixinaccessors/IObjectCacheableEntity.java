package com.linearity.opentc4.mixinaccessors;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * see {@link IObjectCacheableEntity}
 */
//TODO:[maybe wont finished]cut weak map with this
public interface IObjectCacheableEntity {
    void opentc4$removeCachedObjectFromEntity(Class<?> clazz);
    <T> @Nullable T opentc4$getCachedObjectFromEntity(Class<T> clazz);
    <T> void opentc4$putCachedObjectIntoEntity(Class<T> clazz,T object);
    default <T> T opentc4$getOrCreateCachedObjectFromEntity(Class<T> clazz, Supplier<T> supplier){
        var cached = opentc4$getCachedObjectFromEntity(clazz);
        if (cached == null) {
            cached = supplier.get();
            opentc4$putCachedObjectIntoEntity(clazz, cached);
        }
        return cached;
    }
    static <T> T opentc4$getOrCreateCachedObjectFromEntity(Entity entity, Class<T> clazz, Supplier<T> supplier) {
        return ((IObjectCacheableEntity)entity).opentc4$getOrCreateCachedObjectFromEntity(clazz, supplier);
    }
    static <T> @Nullable T opentc4$getCachedObjectFromEntity(Entity stack, Class<T> clazz) {
        return ((IObjectCacheableEntity)(Object)stack).opentc4$getCachedObjectFromEntity(clazz);
    }
    static void opentc4$removeCachedObjectFromEntity(Entity stack,Class<?> clazz) {
        ((IObjectCacheableEntity)(Object)stack).opentc4$removeCachedObjectFromEntity(clazz);
    }
}
