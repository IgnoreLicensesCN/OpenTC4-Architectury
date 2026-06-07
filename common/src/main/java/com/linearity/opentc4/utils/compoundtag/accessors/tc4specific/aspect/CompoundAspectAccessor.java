package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.AspectResourceLocationTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CompoundAspect;

import java.util.NoSuchElementException;

public class CompoundAspectAccessor extends CompoundTagAccessor<CompoundAspect> {
    protected final AspectResourceLocationTagAccessor resourceLocationAccessor;

    public CompoundAspectAccessor(String tagKey) {
        super(tagKey);
        resourceLocationAccessor = new AspectResourceLocationTagAccessor(tagKey);
    }

    @Override
    @NotNull
    public CompoundAspect readFromCompoundTag(CompoundTag tag) {
        var resLoc = resourceLocationAccessor.readFromCompoundTag(tag);
        if (resLoc.getPath().isEmpty() || resLoc.getNamespace().isEmpty()) {
            return Aspects.EMPTY_COMPOUND;
        }
        var result = Aspects.ALL_ASPECTS.get(resLoc);
        if (result == null) {
            OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", resLoc, tag, new NoSuchElementException());
            return Aspects.EMPTY_COMPOUND;
        }
        return result instanceof CompoundAspect ? (CompoundAspect) result : Aspects.EMPTY_COMPOUND;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull CompoundAspect value) {
        resourceLocationAccessor.writeToCompoundTag(tag, value.getAspectKey());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
