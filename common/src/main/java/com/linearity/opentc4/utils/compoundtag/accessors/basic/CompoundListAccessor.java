package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.GenericListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class CompoundListAccessor extends GenericListTagAccessor<CompoundTag> {
    public CompoundListAccessor(String tagKey) {
        super(tagKey, Tag.TAG_COMPOUND);
    }
}
