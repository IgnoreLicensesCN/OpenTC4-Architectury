package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class IntTagAccessor extends CompoundTagAccessor<Integer> {

    public IntTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Integer readFromCompoundTag(CompoundTag tag) {
        return tag.getInt(tagKey);
    }
    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void writeToCompoundTag(CompoundTag tag, Integer value) {
        tag.putInt(tagKey, value);
    }

    public int readIntFromCompoundTag(CompoundTag tag) {
        return tag.getInt(tagKey);
    }
    public void writeIntToCompoundTag(CompoundTag tag, int value) {
        tag.putInt(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_INT);
    }
}
