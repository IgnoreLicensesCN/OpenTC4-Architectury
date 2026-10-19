package com.linearity.opentc4.utils.compoundtag.accessors.utility.collection;

import com.linearity.opentc4.mixinaccessors.IAccessorCacheableTag;
import com.linearity.opentc4.utils.compoundtag.accessors.CacheableCompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UnmodifiableMapAccessor<K,V> extends CacheableCompoundTagAccessor<Map<K,V>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final CompoundTagAccessor<K> keyAccessor;
    protected final CompoundTagAccessor<V> valueAccessor;

    public UnmodifiableMapAccessor(String tagKey, CompoundTagAccessor<K> keyAccessor, CompoundTagAccessor<V> valueAccessor) {
        super(tagKey);
        this.keyAccessor = keyAccessor;
        this.valueAccessor = valueAccessor;
        this.listTagAccessorInternal = new ListTagAccessor(tagKey);
    }

    @Override
    public Map<K, V> readFromCompoundTagWithoutCache(CompoundTag tag) {
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        Map<K, V> result = new HashMap<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var hexCoord = keyAccessor.readFromCompoundTag(compoundTag);
            var hexType = valueAccessor.readFromCompoundTag(compoundTag);
            result.put(hexCoord, hexType);
        }
        result = Collections.unmodifiableMap(result);
        return result;
    }

    @Override
    public void writeToCompoundTagWithoutCache(CompoundTag tag, Map<K, V> value) {
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
