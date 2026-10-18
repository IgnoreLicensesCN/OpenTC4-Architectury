package com.linearity.opentc4.utils.compoundtag.accessors;

import net.minecraft.nbt.CompoundTag;

//2026/10/18:if you want to access my tags,use this.I will cache some objects and save life with this.
//or cache would be invalid.
//if anyone see how i access tags and ignore them just #getCompound("someString") i may...spam in their github issue
public abstract class CompoundTagAccessor<T> {
    public final String tagKey;

    protected CompoundTagAccessor(String tagKey) {
        this.tagKey = tagKey;
    }

    public abstract T readFromCompoundTag(CompoundTag tag);

    public abstract void writeToCompoundTag(CompoundTag tag, T value);

    public abstract boolean compoundTagHasKey(CompoundTag tag);
}
