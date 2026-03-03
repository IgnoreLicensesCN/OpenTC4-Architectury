package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.node;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.NodeTypeResourceLocationTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.nodes.NodeType;

public class NodeTypeAccessor extends CompoundTagAccessor<NodeType> {
    private final NodeTypeResourceLocationTagAccessor resAccessor;
    protected NodeTypeAccessor(String tagKey) {
        super(tagKey);
        this.resAccessor = new NodeTypeResourceLocationTagAccessor(tagKey + "_res");
    }

    @Override
    public NodeType readFromCompoundTag(CompoundTag tag) {
        var nodeTypeRes = resAccessor.readFromCompoundTag(tag);
        return NodeType.valueOrEmpty(nodeTypeRes);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, NodeType value) {
        resAccessor.writeToCompoundTag(tag,value.name());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return resAccessor.compoundTagHasKey(tag);
    }
}
