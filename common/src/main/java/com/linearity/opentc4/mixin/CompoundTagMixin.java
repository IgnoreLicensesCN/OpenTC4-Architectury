package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinattachments.IAccessorCacheableTag;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

import static com.linearity.opentc4.OpenTC4.DEBUG_FLAG;
import static com.linearity.opentc4.OpenTC4.DebugFlags.CHECK_TAG_ACCESSOR_WHEN_DEBUG;

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
        opentc4$checkAccessorWhenDebug(accessor);
        return (T) opentc4$accessorCachedObjects.get(accessor.tagKey);
    }

    @Override
    public <T> void opentc4$setTagCache(CompoundTagAccessor<T> accessor, T value) {
        opentc4$checkAccessorWhenDebug(accessor);
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

    @Unique
    private final Map<String,CompoundTagAccessor<?>> opentc4$tagKeyOccupiedByAccessors = new HashMap<>();
    @Unique
    public void opentc4$checkAccessorWhenDebug(CompoundTagAccessor<?> accessor) {
        if (DEBUG_FLAG && CHECK_TAG_ACCESSOR_WHEN_DEBUG){
            var accessorBefore = opentc4$tagKeyOccupiedByAccessors.put(accessor.tagKey, accessor);
            if (accessorBefore != accessor) {
                throw new IllegalArgumentException("Accessor tag key collision for cache");
            }
        }
    }
}
