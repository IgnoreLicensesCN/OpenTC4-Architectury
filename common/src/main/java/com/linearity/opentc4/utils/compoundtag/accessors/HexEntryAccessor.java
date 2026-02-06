package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.lib.research.HexEntry;

public class HexEntryAccessor extends CompoundTagAccessor<HexEntry> {
    protected final AspectResourceLocationTagAccessor aspectResLocAccessorInternal;
    protected final HexTypeAccessor hexTypeAccessorInternal;

    public HexEntryAccessor(String tagKey) {
        super(tagKey, HexEntry.class);
        this.aspectResLocAccessorInternal = new AspectResourceLocationTagAccessor(tagKey + "_aspect_res");
        this.hexTypeAccessorInternal = new HexTypeAccessor(tagKey + "_hex_type");
    }

    @Override
    public HexEntry readFromCompoundTag(CompoundTag tag) {
        var aspectLoc = aspectResLocAccessorInternal.readFromCompoundTag(tag);
        var hexType = hexTypeAccessorInternal.readFromCompoundTag(tag);
        return new HexEntry(Aspect.getAspect(aspectLoc), hexType);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, HexEntry value) {
        aspectResLocAccessorInternal.writeToCompoundTag(tag, value.aspect().aspectKey);
        hexTypeAccessorInternal.writeToCompoundTag(tag, value.type());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return aspectResLocAccessorInternal.compoundTagHasKey(tag)
                && hexTypeAccessorInternal.compoundTagHasKey(tag);
    }
}
