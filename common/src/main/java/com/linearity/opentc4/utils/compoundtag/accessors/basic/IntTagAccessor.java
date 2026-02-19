package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class IntTagAccessor extends CompoundTagAccessor<Integer> {

    public IntTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public Integer readFromCompoundTag(CompoundTag tag) {
        return tag.getInt(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Integer value) {
        tag.putInt(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_INT);
    }
}
