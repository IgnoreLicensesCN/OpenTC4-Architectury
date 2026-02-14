package com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.NodeTypeResourceLocation;

public class NodeTypeResourceLocationTagAccessor extends CompoundTagAccessor<NodeTypeResourceLocation> {

    public NodeTypeResourceLocationTagAccessor(String tagKey) {
        super(tagKey, NodeTypeResourceLocation.class);
    }

    @Override
    public NodeTypeResourceLocation readFromCompoundTag(CompoundTag tag) {
        return NodeTypeResourceLocation.of(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull NodeTypeResourceLocation value) {
        tag.putString(tagKey, String.valueOf(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
