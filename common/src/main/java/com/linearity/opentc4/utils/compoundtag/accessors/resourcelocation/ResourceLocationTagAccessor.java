package com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ResourceLocationTagAccessor extends CompoundTagAccessor<ResourceLocation> {

    public ResourceLocationTagAccessor(String tagKey) {
        super(tagKey, ResourceLocation.class);
    }

    @Override
    public ResourceLocation readFromCompoundTag(CompoundTag tag) {
        return new ResourceLocation(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull ResourceLocation value) {
        tag.putString(tagKey, String.valueOf(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
