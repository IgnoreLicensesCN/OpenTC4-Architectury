package com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

public class AspectResourceLocationTagAccessor extends CompoundTagAccessor<AspectResourceLocation> {
    public AspectResourceLocationTagAccessor(String tagKey) {
        super(tagKey, AspectResourceLocation.class);
    }

    @Override
    public AspectResourceLocation readFromCompoundTag(CompoundTag tag) {
        return AspectResourceLocation.of(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull AspectResourceLocation value) {
        tag.putString(tagKey, String.valueOf(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
