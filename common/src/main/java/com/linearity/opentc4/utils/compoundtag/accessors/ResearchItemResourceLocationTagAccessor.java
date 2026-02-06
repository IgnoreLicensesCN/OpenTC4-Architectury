package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

public class ResearchItemResourceLocationTagAccessor extends CompoundTagAccessor<ResearchItemResourceLocation> {

    public ResearchItemResourceLocationTagAccessor(String tagKey) {
        super(tagKey, ResearchItemResourceLocation.class);
    }

    @Override
    public ResearchItemResourceLocation readFromCompoundTag(CompoundTag tag) {
        return new ResearchItemResourceLocation(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull ResearchItemResourceLocation value) {
        tag.putString(tagKey, String.valueOf(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
