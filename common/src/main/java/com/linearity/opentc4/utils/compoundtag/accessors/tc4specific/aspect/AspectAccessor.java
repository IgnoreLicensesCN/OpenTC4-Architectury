package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResourceLocationTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;

public class AspectAccessor extends CompoundTagAccessor<Aspect> {
    protected final ResourceLocationTagAccessor resourceLocationAccessor;

    public AspectAccessor(String tagKey) {
        super(tagKey, Aspect.class);
        resourceLocationAccessor = new ResourceLocationTagAccessor(tagKey);
    }

    @Override
    @NotNull
    public Aspect readFromCompoundTag(CompoundTag tag) {
        var resLoc = resourceLocationAccessor.readFromCompoundTag(tag);
        if (resLoc.getPath()
                .isEmpty()) {
            return Aspects.EMPTY;
        }
        var result = Aspects.ALL_ASPECTS.get(resLoc);
        if (result == null) {
            OpenTC4.LOGGER.error("Couldn't find aspect {} in tag {}", resLoc, tag, new Exception());
            return Aspects.EMPTY;
        }
        return result;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull Aspect value) {
        resourceLocationAccessor.writeToCompoundTag(tag, value.getAspectKey());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
