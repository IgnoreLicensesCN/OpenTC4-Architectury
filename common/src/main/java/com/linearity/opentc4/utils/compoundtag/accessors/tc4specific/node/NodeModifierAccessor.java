package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.node;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.NodeModifierResourceLocationTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.nodes.NodeModifier;

public class NodeModifierAccessor extends CompoundTagAccessor<NodeModifier> {
    private final NodeModifierResourceLocationTagAccessor resAccessor;
    protected NodeModifierAccessor(String tagKey) {
        super(tagKey);
        this.resAccessor = new NodeModifierResourceLocationTagAccessor(tagKey + "_res");
    }

    @Override
    public NodeModifier readFromCompoundTag(CompoundTag tag) {
        var nodeTypeRes = resAccessor.readFromCompoundTag(tag);
        return NodeModifier.valueOrEmpty(nodeTypeRes);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, NodeModifier value) {
        resAccessor.writeToCompoundTag(tag,value.name());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return resAccessor.compoundTagHasKey(tag);
    }
}
