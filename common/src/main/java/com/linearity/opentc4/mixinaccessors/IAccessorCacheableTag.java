package com.linearity.opentc4.mixinaccessors;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

//only for cacheing read-only object,unless you copy them before send them out of accessor
public interface IAccessorCacheableTag {
    <T> @Nullable T opentc4$getTagCache(CompoundTagAccessor<T> accessor);
    <T> void opentc4$setTagCache(CompoundTagAccessor<T> accessor, T value);

    @ApiStatus.Internal
    Map<String,Object> opentc4$getAccessorCachedObjects();
}
