package com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import thaumcraft.common.lib.resourcelocations.CrucibleRecipeResourceLocation;

public class CrucibleRecipeResourceLocationTagAccessor extends CompoundTagAccessor<CrucibleRecipeResourceLocation> {
    public CrucibleRecipeResourceLocationTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public CrucibleRecipeResourceLocation readFromCompoundTag(CompoundTag tag) {
        return CrucibleRecipeResourceLocation.of(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, CrucibleRecipeResourceLocation value) {
        tag.putString(tagKey,value.toString());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
