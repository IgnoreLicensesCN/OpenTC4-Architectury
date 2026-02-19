package com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;

public class ClueResourceLocationTagAccessor extends CompoundTagAccessor<ClueResourceLocation> {

    public ClueResourceLocationTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public ClueResourceLocation readFromCompoundTag(CompoundTag tag) {
        return ClueResourceLocation.of(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull ClueResourceLocation value) {
        tag.putString(tagKey, String.valueOf(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
