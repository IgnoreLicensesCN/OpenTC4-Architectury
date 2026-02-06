package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class ShortTagAccessor extends CompoundTagAccessor<Short> {

    public ShortTagAccessor(String tagKey) {
        super(tagKey, Short.class);
    }

    @Override
    public Short readFromCompoundTag(CompoundTag tag) {
        return tag.getShort(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Short value) {
        tag.putLong(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_LONG);
    }
}
