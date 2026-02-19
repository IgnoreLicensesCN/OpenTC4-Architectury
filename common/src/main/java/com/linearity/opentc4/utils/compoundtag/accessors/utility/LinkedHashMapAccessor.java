package com.linearity.opentc4.utils.compoundtag.accessors.utility;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapAccessor<K,V> extends CompoundTagAccessor<LinkedHashMap<K,V>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final CompoundTagAccessor<K> keyAccessor;
    protected final CompoundTagAccessor<V> valueAccessor;

    public LinkedHashMapAccessor(String tagKey, CompoundTagAccessor<K> keyAccessor, CompoundTagAccessor<V> valueAccessor) {
        super(tagKey);
        this.keyAccessor = keyAccessor;
        this.valueAccessor = valueAccessor;
        this.listTagAccessorInternal = new ListTagAccessor(tagKey);
    }

    @Override
    public LinkedHashMap<K, V> readFromCompoundTag(CompoundTag tag) {
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        LinkedHashMap<K, V> result = new LinkedHashMap<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var hexCoord = keyAccessor.readFromCompoundTag(compoundTag);
            var hexType = valueAccessor.readFromCompoundTag(compoundTag);
            result.put(hexCoord, hexType);
        }
        return result;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, LinkedHashMap<K, V> value) {
        var listTag = new ListTag();
        for (var entry : value.entrySet()) {
            var coord = entry.getKey();
            var hexEntry = entry.getValue();
            var compound = new CompoundTag();
            keyAccessor.writeToCompoundTag(compound, coord);
            valueAccessor.writeToCompoundTag(compound, hexEntry);
            listTag.add(compound);
        }
        listTagAccessorInternal.writeToCompoundTag(tag, listTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listTagAccessorInternal.compoundTagHasKey(tag);
    }
}
