package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.mixinaccessors.IAccessorCacheableTag;
import net.minecraft.nbt.CompoundTag;

//2026/10/18:if you want to access my tags,use this.I will cache some objects and save life with this.
//or cache would be invalid.
//if anyone see how i access tags and ignore them just #getCompound("someString") i may...spam in their github issue
//make sure what write inside wont be modified(or cache will be modified but tag wont)
public abstract class CacheableCompoundTagAccessor<T> extends CompoundTagAccessor<T> {

    protected CacheableCompoundTagAccessor(String tagKey) {
        super(tagKey);
    }

    public abstract T readFromCompoundTagWithoutCache(CompoundTag tag);
    public abstract void writeToCompoundTagWithoutCache(CompoundTag tag, T value);

    @Override
    public T readFromCompoundTag(CompoundTag tag) {
        return readFromCompoundTagWithCache(tag);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, T value) {
        writeToCompoundTagWithCache(tag, value);
    }

    protected void writeToCompoundTagWithCache(CompoundTag tag, T value){
        writeToCompoundTagWithoutCache(tag, value);
        var cacheableTag = (IAccessorCacheableTag)tag;
        cacheableTag.opentc4$setTagCache(this, value);
    };
    public T readFromCompoundTagWithCache(CompoundTag tag) {
        var cacheableTag = (IAccessorCacheableTag)tag;
        T result = cacheableTag.opentc4$getTagCache(this);
        if (result != null) {
            return result;
        }
        result = readFromCompoundTagWithoutCache(tag);

        cacheableTag.opentc4$setTagCache(this,result);
        return result;
    }
}
