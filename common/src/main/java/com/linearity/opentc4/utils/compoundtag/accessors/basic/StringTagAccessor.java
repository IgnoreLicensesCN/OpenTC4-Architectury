package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class StringTagAccessor extends CompoundTagAccessor<String> {

    public StringTagAccessor(String tagKey) {
        super(tagKey, String.class);
    }

    @Override
    public String readFromCompoundTag(CompoundTag tag) {
        return tag.getString(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, String value) {
        tag.putString(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
