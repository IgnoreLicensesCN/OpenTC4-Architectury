package com.linearity.opentc4.utils.compoundtag.accessors.mc;

import com.linearity.opentc4.utils.compoundtag.accessors.CacheableCompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackTagAccessor extends CacheableCompoundTagAccessor<ItemStack> {

    public ItemStackTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public ItemStack readFromCompoundTag(CompoundTag tag) {
        var result = super.readFromCompoundTag(tag);
        return result.isEmpty() ? ItemStack.EMPTY : result.copy();
    }

    @Override
    public ItemStack readFromCompoundTagWithoutCache(CompoundTag tag) {
        return ItemStack.of(tag.getCompound(tagKey));
    }

    //PROMISE TO ME you wont modify it.
    public ItemStack readFromCompoundTagWithoutCopyCache(CompoundTag tag) {
        return super.readFromCompoundTag(tag);
    }

    @Override
    public void writeToCompoundTagWithoutCache(CompoundTag tag, ItemStack value) {
        tag.put(tagKey, value.save(new CompoundTag()));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey);
    }

}
