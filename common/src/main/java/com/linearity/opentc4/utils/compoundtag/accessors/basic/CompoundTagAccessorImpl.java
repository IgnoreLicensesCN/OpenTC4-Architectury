package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class CompoundTagAccessorImpl extends CompoundTagAccessor<CompoundTag> {

    public CompoundTagAccessorImpl(String tagKey) {
        super(tagKey);
    }

    @Override
    public CompoundTag readFromCompoundTag(CompoundTag tag) {
        return tag.getCompound(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, CompoundTag value) {
        tag.put(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_COMPOUND);
    }
}
