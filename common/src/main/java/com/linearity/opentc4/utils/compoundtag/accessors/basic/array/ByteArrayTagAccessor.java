package com.linearity.opentc4.utils.compoundtag.accessors.basic.array;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;

public class ByteArrayTagAccessor extends CompoundTagAccessor<byte[]> {
    public ByteArrayTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public byte[] readFromCompoundTag(CompoundTag tag) {
        return tag.getByteArray(tagKey);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, byte[] value) {
        tag.putByteArray(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, CompoundTag.TAG_BYTE_ARRAY);
    }
}
