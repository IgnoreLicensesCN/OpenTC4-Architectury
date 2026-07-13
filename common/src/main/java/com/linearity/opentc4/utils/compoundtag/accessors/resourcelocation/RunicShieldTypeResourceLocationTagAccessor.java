package com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;

public class RunicShieldTypeResourceLocationTagAccessor extends CompoundTagAccessor<RunicShieldTypeResourceLocation> {
    public RunicShieldTypeResourceLocationTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public RunicShieldTypeResourceLocation readFromCompoundTag(CompoundTag tag) {
        return RunicShieldTypeResourceLocation.of(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull RunicShieldTypeResourceLocation value) {
        tag.putString(tagKey, String.valueOf(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
