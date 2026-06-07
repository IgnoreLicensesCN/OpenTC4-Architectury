package com.linearity.opentc4.utils.compoundtag.accessors.basic.array;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;

public class IntArrayTagAccessor extends CompoundTagAccessor<int[]> {
    public IntArrayTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public int[] readFromCompoundTag(CompoundTag tag) {
        return tag.getIntArray(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, int[] value) {
        tag.putIntArray(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, CompoundTag.TAG_INT_ARRAY);
    }
}
