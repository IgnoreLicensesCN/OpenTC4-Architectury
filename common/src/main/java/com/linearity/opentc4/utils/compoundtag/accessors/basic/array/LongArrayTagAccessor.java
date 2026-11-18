package com.linearity.opentc4.utils.compoundtag.accessors.basic.array;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;

public class LongArrayTagAccessor extends CompoundTagAccessor<long[]> {
    public LongArrayTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public long[] readFromCompoundTag(CompoundTag tag) {
        return tag.getLongArray(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, long[] value) {
        tag.putLongArray(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, CompoundTag.TAG_LONG_ARRAY);
    }
}
