package com.linearity.opentc4.utils.compoundtag.accessors.utility.collection;

import com.linearity.opentc4.mixinaccessors.IAccessorCacheableTag;
import com.linearity.opentc4.utils.compoundtag.accessors.CacheableCompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.function.Consumer;

public class UnmodifiableObject2IntMapAccessor<K> extends CacheableCompoundTagAccessor<Object2IntMap<K>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final CompoundTagAccessor<K> keyAccessor;
    protected final IntTagAccessor valueAccessor;

    public UnmodifiableObject2IntMapAccessor(String tagKey, CompoundTagAccessor<K> keyAccessor) {
        super(tagKey);
        this.keyAccessor = keyAccessor;
        this.valueAccessor = new IntTagAccessor(tagKey + "_value");
        this.listTagAccessorInternal = new ListTagAccessor(tagKey);
    }

    @Override
    public Object2IntMap<K> readFromCompoundTagWithoutCache(CompoundTag tag) {
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        Object2IntMap<K> result = new Object2IntOpenHashMap<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var hexCoord = keyAccessor.readFromCompoundTag(compoundTag);
            var hexType = valueAccessor.readIntFromCompoundTag(compoundTag);
            result.put(hexCoord, hexType);
        }
        result = Object2IntMaps.unmodifiable(result);
        return result;
    }

    @Override
    public void writeToCompoundTagWithoutCache(CompoundTag tag, Object2IntMap<K> value) {
        var listTag = new ListTag();
        for (var entry : value.object2IntEntrySet()) {
            var coord = entry.getKey();
            var hexEntry = entry.getIntValue();
            var compound = new CompoundTag();
            keyAccessor.writeToCompoundTag(compound, coord);
            valueAccessor.writeIntToCompoundTag(compound, hexEntry);
            listTag.add(compound);
        }
        listTagAccessorInternal.writeToCompoundTag(tag, listTag);
    }

    public void updateCacheAndValue(CompoundTag tag, Consumer<Object2IntMap<K>> updateAction){
        var cacheableTag = (IAccessorCacheableTag)tag;
        var result = cacheableTag.opentc4$getTagCache(this);
        if (result == null) {
            result = new Object2IntOpenHashMap<>();
        } else {
            result = new Object2IntOpenHashMap<>(result);
        }
        updateAction.accept(result);
        writeToCompoundTag(tag, result);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listTagAccessorInternal.compoundTagHasKey(tag);
    }
}
