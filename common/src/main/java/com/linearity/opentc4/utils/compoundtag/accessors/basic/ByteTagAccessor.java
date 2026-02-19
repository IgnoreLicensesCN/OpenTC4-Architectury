package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class ByteTagAccessor extends CompoundTagAccessor<Byte> {

    public ByteTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public Byte readFromCompoundTag(CompoundTag tag) {
        return tag.getByte(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Byte value) {
        tag.putByte(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_BYTE);
    }
}
