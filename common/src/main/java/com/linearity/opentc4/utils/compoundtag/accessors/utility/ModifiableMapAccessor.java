package com.linearity.opentc4.utils.compoundtag.accessors.utility;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModifiableMapAccessor<K,V> extends CompoundTagAccessor<Map<K,V>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final CompoundTagAccessor<K> keyAccessor;
    protected final CompoundTagAccessor<V> valueAccessor;

    public ModifiableMapAccessor(String tagKey, CompoundTagAccessor<K> keyAccessor, CompoundTagAccessor<V> valueAccessor) {
        super(tagKey, (Class<Map<K, V>>)(Class<?>)Map.class);
        this.keyAccessor = keyAccessor;
        this.valueAccessor = valueAccessor;
        this.listTagAccessorInternal = new ListTagAccessor(tagKey);
    }

    @Override
    public Map<K, V> readFromCompoundTag(CompoundTag tag) {
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        Map<K, V> result = new HashMap<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var hexCoord = keyAccessor.readFromCompoundTag(compoundTag);
            var hexType = valueAccessor.readFromCompoundTag(compoundTag);
            result.put(hexCoord, hexType);
        }
        return result;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Map<K, V> value) {
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
