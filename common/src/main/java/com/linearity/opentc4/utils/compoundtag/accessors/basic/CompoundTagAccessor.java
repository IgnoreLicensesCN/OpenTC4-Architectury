package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import net.minecraft.nbt.CompoundTag;

public abstract class CompoundTagAccessor<T> {
    public final String tagKey;

    protected CompoundTagAccessor(String tagKey) {
        this.tagKey = tagKey;
    }

    public abstract T readFromCompoundTag(CompoundTag tag);

    public abstract void writeToCompoundTag(CompoundTag tag, T value);

    public abstract boolean compoundTagHasKey(CompoundTag tag);
}
