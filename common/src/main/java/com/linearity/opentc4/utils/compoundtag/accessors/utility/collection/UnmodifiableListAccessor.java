package com.linearity.opentc4.utils.compoundtag.accessors.utility.collection;
import com.linearity.opentc4.mixinaccessors.IAccessorCacheableTag;
import com.linearity.opentc4.utils.compoundtag.accessors.CacheableCompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnmodifiableListAccessor<T> extends CacheableCompoundTagAccessor<List<T>> {
    protected final ListTagAccessor listAccessor;
    protected final CompoundTagAccessor<T> listItemAccessor;
    public UnmodifiableListAccessor(String tagKey, CompoundTagAccessor<T> listItemAccessor) {
        super(tagKey);
        this.listItemAccessor = listItemAccessor;
        this.listAccessor = new ListTagAccessor(tagKey + "_list");
    }


    @Override
    public List<T> readFromCompoundTagWithoutCache(CompoundTag tag) {
        var listTag = listAccessor.readFromCompoundTag(tag);
        var len = listTag.size();
        List<T> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            var compound = listTag.getCompound(i);
            list.add(listItemAccessor.readFromCompoundTag(compound));
        }

        return Collections.unmodifiableList(list);
    }

    @Override
    public void writeToCompoundTagWithoutCache(CompoundTag tag, List<T> value) {
        var listTag = new ListTag();
        for (T t : value) {
            var compound = new CompoundTag();
            listItemAccessor.writeToCompoundTag(compound,t);
            listTag.add(compound);
        }
        listAccessor.writeToCompoundTag(tag,listTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listAccessor.compoundTagHasKey(tag);
    }
}
