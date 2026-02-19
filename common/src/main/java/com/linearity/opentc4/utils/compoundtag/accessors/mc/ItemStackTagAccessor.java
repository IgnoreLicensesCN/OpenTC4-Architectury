package com.linearity.opentc4.utils.compoundtag.accessors.mc;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStackTagAccessor extends CompoundTagAccessor<ItemStack> {

    public ItemStackTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public ItemStack readFromCompoundTag(CompoundTag tag) {
        if (!compoundTagHasKey(tag)) {
            return ItemStack.EMPTY;
        }
        return ItemStack.of(tag.getCompound(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, ItemStack value) {
        if (value.isEmpty()) return;
        tag.put(tagKey, value.save(new CompoundTag()));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey);
    }

}
