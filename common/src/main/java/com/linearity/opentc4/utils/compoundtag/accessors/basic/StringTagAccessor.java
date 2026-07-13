package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class StringTagAccessor extends CompoundTagAccessor<String> {

    public StringTagAccessor(String tagKey) {
        super(tagKey);
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
