package com.linearity.opentc4.utils.compoundtag.accessors;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.List;
public class ArrayListAccessor<T> extends CompoundTagAccessor<List<T>> {
    protected final ListTagAccessor listAccessor;
    protected final CompoundTagAccessor<T> listItemAccessor;
    public ArrayListAccessor(String tagKey, CompoundTagAccessor<T> listItemAccessor) {
        super(tagKey, (Class<List<T>>) (Class<?>) List.class);
        this.listItemAccessor = listItemAccessor;
        this.listAccessor = new ListTagAccessor(tagKey + "_list");
    }

    @Override
    public List<T> readFromCompoundTag(CompoundTag tag) {
        var listTag = listAccessor.readFromCompoundTag(tag);
        var len = listTag.size();
        List<T> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            var compound = listTag.getCompound(i);
            list.add(listItemAccessor.readFromCompoundTag(compound));
        }
        return list;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, List<T> value) {
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
