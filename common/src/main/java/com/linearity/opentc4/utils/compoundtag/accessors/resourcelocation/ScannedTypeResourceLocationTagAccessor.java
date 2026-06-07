package com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;

public class ScannedTypeResourceLocationTagAccessor extends CompoundTagAccessor<ScannedTypeResourceLocation> {

    public ScannedTypeResourceLocationTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public ScannedTypeResourceLocation readFromCompoundTag(CompoundTag tag) {
        return ScannedTypeResourceLocation.of(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull ScannedTypeResourceLocation value) {
        tag.putString(tagKey, String.valueOf(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
