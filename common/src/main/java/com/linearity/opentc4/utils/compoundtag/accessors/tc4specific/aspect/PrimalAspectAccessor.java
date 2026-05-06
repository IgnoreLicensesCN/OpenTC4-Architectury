package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.AspectResourceLocationTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.PrimalAspect;

import java.util.NoSuchElementException;

public class PrimalAspectAccessor extends CompoundTagAccessor<PrimalAspect> {
    protected final AspectResourceLocationTagAccessor resourceLocationAccessor;

    public PrimalAspectAccessor(String tagKey) {
        super(tagKey);
        resourceLocationAccessor = new AspectResourceLocationTagAccessor(tagKey);
    }

    @Override
    @NotNull
    public PrimalAspect readFromCompoundTag(CompoundTag tag) {
        var resLoc = resourceLocationAccessor.readFromCompoundTag(tag);
        if (resLoc.getPath().isEmpty() || resLoc.getNamespace().isEmpty()) {
            return Aspects.EMPTY_PRIMAL;
        }
        var result = Aspects.ALL_ASPECTS.get(resLoc);
        if (result == null) {
            OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", resLoc, tag, new NoSuchElementException());
            return Aspects.EMPTY_PRIMAL;
        }
        return result instanceof PrimalAspect ? (PrimalAspect) result : Aspects.EMPTY_PRIMAL;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull PrimalAspect value) {
        resourceLocationAccessor.writeToCompoundTag(tag, value.getAspectKey());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
