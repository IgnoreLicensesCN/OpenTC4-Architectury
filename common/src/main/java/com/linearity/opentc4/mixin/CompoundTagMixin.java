package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.IAccessorCacheableTag;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

@Mixin(CompoundTag.class)
public class CompoundTagMixin implements IAccessorCacheableTag {
    @Unique
    private final Map<String,Object> opentc4$accessorCachedObjects = new HashMap<>();

    @Override
    public Map<String, Object> opentc4$getAccessorCachedObjects() {
        return opentc4$accessorCachedObjects;
    }

    @Override
    public <T> T opentc4$getTagCache(CompoundTagAccessor<T> accessor) {
        return (T) opentc4$accessorCachedObjects.get(accessor.tagKey);
    }

    @Override
    public <T> void opentc4$setTagCache(CompoundTagAccessor<T> accessor, T value) {
        opentc4$accessorCachedObjects.put(accessor.tagKey, value);
    }

    @ModifyReturnValue(
            method = "copy()Lnet/minecraft/nbt/CompoundTag;",
            at = @At("RETURN")
    )
    public CompoundTag opentc4$modifyCopy(CompoundTag tag) {
        if (tag instanceof IAccessorCacheableTag cacheableTag) {
            cacheableTag.opentc4$getAccessorCachedObjects().putAll(this.opentc4$accessorCachedObjects);
        }
        return tag;
    }
}
