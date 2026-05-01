package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class FloatTagAccessor extends CompoundTagAccessor<Float> {

    public FloatTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Float readFromCompoundTag(CompoundTag tag) {
        return tag.getFloat(tagKey);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void writeToCompoundTag(CompoundTag tag, Float value) {
        tag.putFloat(tagKey, value);
    }
    public float readFloatFromCompoundTag(CompoundTag tag) {
        return tag.getFloat(tagKey);
    }
    public void writeFloatToCompoundTag(CompoundTag tag, float value) {
        tag.putFloat(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_FLOAT);
    }
}
