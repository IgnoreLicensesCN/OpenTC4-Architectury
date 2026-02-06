package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StringSetTagAccessor extends CompoundTagAccessor<Set<String>> {
    private final ListTagAccessor LIST_TAG_ACCESSOR;

    public StringSetTagAccessor(String tagKey) {
        super(tagKey, (Class<Set<String>>) (Class<?>) Set.class);
        this.LIST_TAG_ACCESSOR = new ListTagAccessor(tagKey);
    }

    @Override
    public Set<String> readFromCompoundTag(CompoundTag tag) {
        ListTag listTag = LIST_TAG_ACCESSOR.readFromCompoundTag(tag);
        List<String> list = new ArrayList<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            String element = listTag.getString(i);
            list.add(element);
        }
        return Set.of(list.toArray(new String[0]));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Set<String> value) {
        ListTag listTag = new ListTag();
        value.forEach(s -> listTag.add(StringTag.valueOf(s)));
        LIST_TAG_ACCESSOR.writeToCompoundTag(tag, listTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey);
    }
}
