package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public abstract class GenericListTagAccessor<T extends Tag> extends CompoundTagAccessor<ListTag> {
    public final int elementType;

    public GenericListTagAccessor(String tagKey, int elementType) {
        super(tagKey, ListTag.class);
        this.elementType = elementType;
    }

    @Override
    public ListTag readFromCompoundTag(CompoundTag tag) {
        return tag.getList(tagKey, elementType);
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
