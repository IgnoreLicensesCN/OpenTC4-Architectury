package com.linearity.opentc4.utils.compoundtag.accessors.utility.collection;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ModifiableSetTagAccessor<T> extends CompoundTagAccessor<Set<T>> {
    protected final ModifiableListAccessor<T> accessorInternal;

    public ModifiableSetTagAccessor(String tagKey, CompoundTagAccessor<T> accessor) {
        super(tagKey);
        this.accessorInternal = new ModifiableListAccessor<>(tagKey+"_set",accessor);
    }

    @Override
    public Set<T> readFromCompoundTag(CompoundTag tag) {
        return new HashSet<>(accessorInternal.readFromCompoundTag(tag));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Set<T> value) {
        accessorInternal.writeToCompoundTag(tag,value);
    }

    public void writeToCompoundTag(CompoundTag tag, Collection<T> value) {
        accessorInternal.writeToCompoundTag(tag,value);
    }
    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return accessorInternal.compoundTagHasKey(tag);
    }
}
