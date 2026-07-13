package com.linearity.opentc4.utils.compoundtag.accessors.utility.collection;
import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModifiableListAccessor<T> extends CompoundTagAccessor<List<T>> {
    protected final ListTagAccessor listAccessor;
    protected final CompoundTagAccessor<T> listItemAccessor;
    public ModifiableListAccessor(String tagKey, CompoundTagAccessor<T> listItemAccessor) {
        super(tagKey);
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

    public void readFromCompoundTagInto(CompoundTag tag, @Modifiable List<T> list) {
        var listTag = listAccessor.readFromCompoundTag(tag);
        var len = listTag.size();
        for (int i = 0; i < len; i++) {
            var compound = listTag.getCompound(i);
            list.add(listItemAccessor.readFromCompoundTag(compound));
        }
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag,@Modifiable List<T> value) {
        var listTag = new ListTag();
        for (T t : value) {
            var compound = new CompoundTag();
            listItemAccessor.writeToCompoundTag(compound,t);
            listTag.add(compound);
        }
        listAccessor.writeToCompoundTag(tag,listTag);
    }

    public void writeToCompoundTag(CompoundTag tag, Collection<T> collection) {
        var listTag = new ListTag();
        for (T t : collection) {
            var compound = new CompoundTag();
            listItemAccessor.writeToCompoundTag(compound,t);
        }
        listAccessor.writeToCompoundTag(tag,listTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listAccessor.compoundTagHasKey(tag);
    }
}
