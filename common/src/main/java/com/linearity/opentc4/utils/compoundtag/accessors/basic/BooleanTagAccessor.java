package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class BooleanTagAccessor extends CompoundTagAccessor<Boolean> {

    public BooleanTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public Boolean readFromCompoundTag(CompoundTag tag) {
        return tag.getBoolean(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Boolean value) {
        tag.putBoolean(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_BYTE); // boolean 实际存 byte
    }
}
