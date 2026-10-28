package com.linearity.opentc4.mixin;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.linearity.opentc4.mixinaccessors.IObjectCacheableItemStack;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IObjectCacheableItemStack {
    @Unique
    private final ClassToInstanceMap<Object> opentc4$objectCacheMap = MutableClassToInstanceMap.create();

    @Override
    public <T> @Nullable T opentc4$getCachedObjectFromItemStack(Class<T> clazz) {
        return opentc4$objectCacheMap.getInstance(clazz);
    }

    @Override
    public <T> void opentc4$putCachedObjectIntoItemStack(Class<T> clazz, T object) {
        opentc4$objectCacheMap.putInstance(clazz, object);
    }

    @Override
    public void opentc4$removeCachedObjectFromItemStack(Class<?> clazz) {
        opentc4$objectCacheMap.remove(clazz);
    }
}

