package com.linearity.opentc4.utils.compoundtag.accessors.utility.collection;
import com.linearity.opentc4.mixinaccessors.IAccessorCacheableTag;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnmodifiableListAccessor<T> extends CompoundTagAccessor<List<T>> {
    protected final ListTagAccessor listAccessor;
    protected final CompoundTagAccessor<T> listItemAccessor;
    public UnmodifiableListAccessor(String tagKey, CompoundTagAccessor<T> listItemAccessor) {
        super(tagKey);
        this.listItemAccessor = listItemAccessor;
        this.listAccessor = new ListTagAccessor(tagKey + "_list");
    }

    @Override
    public List<T> readFromCompoundTag(CompoundTag tag) {
        var cacheableTag = (IAccessorCacheableTag)tag;
        var result = cacheableTag.opentc4$getTagCache(this);
        if (result != null) {
            return result;
        }
        var listTag = listAccessor.readFromCompoundTag(tag);
        var len = listTag.size();
        List<T> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            var compound = listTag.getCompound(i);
            list.add(listItemAccessor.readFromCompoundTag(compound));
        }
        result = Collections.unmodifiableList(list);
        cacheableTag.opentc4$setTagCache(this, result);
        return result;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag,List<T> value) {
        var listTag = new ListTag();
        for (T t : value) {
            var compound = new CompoundTag();
            listItemAccessor.writeToCompoundTag(compound,t);
            listTag.add(compound);
        }
        listAccessor.writeToCompoundTag(tag,listTag);
        var cacheableTag = (IAccessorCacheableTag)tag;
        List<T> unmodifiableCopy = List.copyOf(value);
        cacheableTag.opentc4$setTagCache(this,unmodifiableCopy);

    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listAccessor.compoundTagHasKey(tag);
    }
}
