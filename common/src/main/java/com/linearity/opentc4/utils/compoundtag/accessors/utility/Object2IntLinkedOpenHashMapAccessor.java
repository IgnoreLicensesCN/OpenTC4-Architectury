package com.linearity.opentc4.utils.compoundtag.accessors.utility;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

//Obviously designed for AspectList
public class Object2IntLinkedOpenHashMapAccessor<K> extends CompoundTagAccessor<Object2IntLinkedOpenHashMap<K>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final CompoundTagAccessor<K> keyAccessor;
    protected final IntTagAccessor valueAccessor;

    public Object2IntLinkedOpenHashMapAccessor(String tagKey, CompoundTagAccessor<K> keyAccessor, IntTagAccessor valueAccessor) {
        super(tagKey);
        this.keyAccessor = keyAccessor;
        this.valueAccessor = valueAccessor;
        this.listTagAccessorInternal = new ListTagAccessor(tagKey);
    }

    @Override
    public Object2IntLinkedOpenHashMap<K> readFromCompoundTag(CompoundTag tag) {
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        Object2IntLinkedOpenHashMap<K> result = new Object2IntLinkedOpenHashMap<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var hexCoord = keyAccessor.readFromCompoundTag(compoundTag);
            var hexType = valueAccessor.readIntFromCompoundTag(compoundTag);
            result.put(hexCoord, hexType);
        }
        return result;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Object2IntLinkedOpenHashMap<K> value) {
        var listTag = new ListTag();
        for (var entry : value.object2IntEntrySet()) {
            var key = entry.getKey();
            var intValue = entry.getIntValue();
            var compound = new CompoundTag();
            keyAccessor.writeToCompoundTag(compound, key);
            valueAccessor.writeToCompoundTag(compound, intValue);
            listTag.add(compound);
        }
        listTagAccessorInternal.writeToCompoundTag(tag, listTag);
    }
    //copied but different cast for object2IntEntrySet return value
    public void writeToCompoundTag(CompoundTag tag, Object2IntMap<K> value) {
        var listTag = new ListTag();
        for (var entry : value.object2IntEntrySet()) {
            var key = entry.getKey();
            var intValue = entry.getIntValue();
            var compound = new CompoundTag();
            keyAccessor.writeToCompoundTag(compound, key);
            valueAccessor.writeToCompoundTag(compound, intValue);
            listTag.add(compound);
        }
        listTagAccessorInternal.writeToCompoundTag(tag, listTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listTagAccessorInternal.compoundTagHasKey(tag);
    }
}
