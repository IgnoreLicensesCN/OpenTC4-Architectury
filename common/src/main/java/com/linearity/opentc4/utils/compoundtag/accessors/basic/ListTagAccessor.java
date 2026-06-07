package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class ListTagAccessor extends CompoundTagAccessor<ListTag> {

    public ListTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public ListTag readFromCompoundTag(CompoundTag tag) {
        return tag.getList(tagKey, Tag.TAG_LIST);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, ListTag value) {
        tag.put(tagKey, value);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_LIST);
    }
}
