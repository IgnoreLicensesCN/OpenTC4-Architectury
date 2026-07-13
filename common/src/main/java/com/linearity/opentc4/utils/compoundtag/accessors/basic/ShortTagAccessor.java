package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class ShortTagAccessor extends CompoundTagAccessor<Short> {

    public ShortTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Short readFromCompoundTag(CompoundTag tag) {
        return tag.getShort(tagKey);
    }


    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void writeToCompoundTag(CompoundTag tag, Short value) {
        tag.putLong(tagKey, value);
    }
    public short readShortFromCompoundTag(CompoundTag tag) {
        return tag.getShort(tagKey);
    }
    public void writeShortToCompoundTag(CompoundTag tag, short value) {
        tag.putLong(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_LONG);
    }
}
