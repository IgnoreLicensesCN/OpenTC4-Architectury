package com.linearity.opentc4.utils.compoundtag.accessors.utility.collection;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModifiableMapValueAccessorFromKeyTagAccessor<K> extends CompoundTagAccessor<Map<K,?>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final CompoundTagAccessor<K> keyAccessor;
    protected final Function<K,@Nullable CompoundTagAccessor<Object>> valueAccessorGetter;

    public ModifiableMapValueAccessorFromKeyTagAccessor(
            String tagKey,
            CompoundTagAccessor<K> keyAccessor,
            Function<K,CompoundTagAccessor<Object>> valueAccessorGetter) {
        super(tagKey);
        this.keyAccessor = keyAccessor;
        this.valueAccessorGetter = valueAccessorGetter;
        this.listTagAccessorInternal = new ListTagAccessor(tagKey);
    }

    @Override
    public Map<K, Object> readFromCompoundTag(CompoundTag tag) {
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        Map<K, Object> result = new HashMap<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var key = keyAccessor.readFromCompoundTag(compoundTag);
            var valueAccessor = valueAccessorGetter.apply(key);
            if (valueAccessor != null) {
                var value = valueAccessor.readFromCompoundTag(compoundTag);
                result.put(key, value);
            }
        }
        return result;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Map<K, ?> valueMap) {
        var listTag = new ListTag();
        for (var entry : valueMap.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var compound = new CompoundTag();
            var valueAccessor = valueAccessorGetter.apply(key);
            if (valueAccessor != null) {
                keyAccessor.writeToCompoundTag(compound, key);
                valueAccessor.writeToCompoundTag(compound,value);
                listTag.add(compound);
            }
        }
        listTagAccessorInternal.writeToCompoundTag(tag, listTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listTagAccessorInternal.compoundTagHasKey(tag);
    }
}