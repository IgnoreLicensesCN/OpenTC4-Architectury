package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class LongTagAccessor extends CompoundTagAccessor<Long> {

    public LongTagAccessor(String tagKey) {
        super(tagKey, Long.class);
    }

    @Override
    public Long readFromCompoundTag(CompoundTag tag) {
        return tag.getLong(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Long value) {
        tag.putLong(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_LONG);
    }
}
