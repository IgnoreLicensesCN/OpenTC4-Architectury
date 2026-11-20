package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

//2026/10/18:if you want to access my tags,use this.I will cache some objects and save life with this.
//or cache would be invalid.
//if anyone see how i access tags and ignore them just #getCompound("someString") i may...spam in their github issue
public abstract class CompoundTagAccessor<T> {
    public static final boolean AVOID_COMPOUND_TAG_KEY_COLLISION = true;
    public static final Map<String,Throwable> TEST_ONLY_COMPOUND_TAG_TRACER = new HashMap<>();
    protected CompoundTagAccessor(String tagKey) {
        this.tagKey = tagKey;
        if (AVOID_COMPOUND_TAG_KEY_COLLISION) {
            var stackTraceCurrent = new IllegalArgumentException("tagKey already exists");
            var stackTraceBefore = TEST_ONLY_COMPOUND_TAG_TRACER.put(tagKey, stackTraceCurrent);
            if (stackTraceBefore != null) {
                OpenTC4.LOGGER.error(stackTraceBefore);
                throw stackTraceCurrent;
            }
        }
    }

    public final String tagKey;

    public abstract T readFromCompoundTag(CompoundTag tag);

    public abstract void writeToCompoundTag(CompoundTag tag, T value);

    public abstract boolean compoundTagHasKey(CompoundTag tag);
}
