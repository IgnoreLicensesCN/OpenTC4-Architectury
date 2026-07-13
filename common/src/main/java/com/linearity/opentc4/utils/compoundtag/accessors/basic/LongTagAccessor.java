package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class LongTagAccessor extends CompoundTagAccessor<Long> {

    public LongTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Long readFromCompoundTag(CompoundTag tag) {
        return tag.getLong(tagKey);
    }


    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void writeToCompoundTag(CompoundTag tag, Long value) {
        tag.putLong(tagKey, value);
    }
    public void writeLongToCompoundTag(CompoundTag tag, long value) {
        tag.putLong(tagKey, value);
    }
    public long readLongFromCompoundTag(CompoundTag tag) {
        return tag.getLong(tagKey);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_LONG);
    }
}
