package com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.NodeModifierResourceLocation;

public class NodeModifierResourceLocationTagAccessor extends CompoundTagAccessor<NodeModifierResourceLocation> {

    public NodeModifierResourceLocationTagAccessor(String tagKey) {
        super(tagKey);
    }

    @Override
    public NodeModifierResourceLocation readFromCompoundTag(CompoundTag tag) {
        return NodeModifierResourceLocation.of(tag.getString(tagKey));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, @NotNull NodeModifierResourceLocation value) {
        tag.putString(tagKey, String.valueOf(value));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return tag.contains(tagKey, Tag.TAG_STRING);
    }
}
