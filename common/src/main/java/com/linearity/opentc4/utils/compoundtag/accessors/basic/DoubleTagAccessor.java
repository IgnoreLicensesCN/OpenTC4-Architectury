package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class DoubleTagAccessor extends CompoundTagAccessor<Double> {

    public DoubleTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public Double readFromCompoundTag(CompoundTag tag) {
        return tag.getDouble(tagKey);
    }
    @Override
    @Deprecated(forRemoval = true,since = "boxing costs")
    public void writeToCompoundTag(CompoundTag tag, Double value) {
        tag.putDouble(tagKey, value);
    }
    public double readDoubleFromCompoundTag(CompoundTag tag) {
        return tag.getFloat(tagKey);
    }
    public void writeDoubleToCompoundTag(CompoundTag tag, double value) {
        tag.putDouble(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_FLOAT);
    }
}
