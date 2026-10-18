package com.linearity.opentc4.utils.compoundtag.accessors.mc;

import com.linearity.opentc4.mixinaccessors.IAccessorCacheableTag;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackTagAccessor extends CompoundTagAccessor<ItemStack> {

    public ItemStackTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public ItemStack readFromCompoundTag(CompoundTag tag) {
        var result = readFromCompoundTagWithoutCopyCache(tag);
        return result.isEmpty() ? ItemStack.EMPTY : result.copy();
    }
    //PROMISE TO ME you wont modify it.
    public ItemStack readFromCompoundTagWithoutCopyCache(CompoundTag tag) {
        if (!compoundTagHasKey(tag)) {
            return ItemStack.EMPTY;
        }
        if (tag instanceof IAccessorCacheableTag cacheableTag) {
            var result = cacheableTag.opentc4$getTagCache(this);
            if (result == null) {
                result = ItemStack.of(tag.getCompound(tagKey));
                cacheableTag.opentc4$setTagCache(this,result);
                return result;
            }
            return result.isEmpty()?result:result.copy();
        }
        return ItemStack.of(tag.getCompound(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, ItemStack value) {
        if (value.isEmpty()) return;
        tag.put(tagKey, value.save(new CompoundTag()));
        if (tag instanceof IAccessorCacheableTag cacheableTag){
            cacheableTag.opentc4$setTagCache(this,value);
        }
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey);
    }

}
