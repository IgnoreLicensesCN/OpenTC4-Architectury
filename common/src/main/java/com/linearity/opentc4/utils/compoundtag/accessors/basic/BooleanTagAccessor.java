package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class BooleanTagAccessor extends CompoundTagAccessor<Boolean> {

    public BooleanTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Boolean readFromCompoundTag(CompoundTag tag) {
        return tag.getBoolean(tagKey);
    }


    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void writeToCompoundTag(CompoundTag tag, Boolean value) {
        tag.putBoolean(tagKey, value);
    }
    public boolean readByteFromCompoundTag(CompoundTag tag) {
        return tag.getBoolean(tagKey);
    }
    public void writeBooleanToCompoundTag(CompoundTag tag, boolean value) {
        tag.putBoolean(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_BYTE);
    }
}
