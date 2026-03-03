package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.node;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.StringTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect.AspectListAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.lib.NodeInfo;

public class NodeInfoAccessor extends CompoundTagAccessor<NodeInfo> {
    private final StringTagAccessor nodeIdAccessor;
    private final NodeTypeAccessor nodeTypeAccessor;
    private final NodeModifierAccessor nodeModifierAccessor;
    private final AspectListAccessor nodeAspectsAccessor;
    private final AspectListAccessor nodeAspectsBaseAccessor;

    public NodeInfoAccessor(String tagKey) {
        super(tagKey);
        this.nodeIdAccessor = new StringTagAccessor(tagKey + "_id");
        this.nodeTypeAccessor = new NodeTypeAccessor(tagKey + "_type");
        this.nodeModifierAccessor = new NodeModifierAccessor(tagKey + "_modifier");
        this.nodeAspectsAccessor = new AspectListAccessor(tagKey + "_aspects");
        this.nodeAspectsBaseAccessor = new AspectListAccessor(tagKey + "_aspects_base");
    }

    @Override
    public NodeInfo readFromCompoundTag(CompoundTag tag) {
        return new NodeInfo(
                nodeIdAccessor.readFromCompoundTag(tag),
                nodeTypeAccessor.readFromCompoundTag(tag),
                nodeModifierAccessor.readFromCompoundTag(tag),
                nodeAspectsAccessor.readFromCompoundTag(tag),
                nodeAspectsBaseAccessor.readFromCompoundTag(tag)
        );
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, NodeInfo value) {
        nodeIdAccessor.writeToCompoundTag(tag, value.nodeId);
        nodeTypeAccessor.writeToCompoundTag(tag, value.nodeType);
        nodeModifierAccessor.writeToCompoundTag(tag, value.nodeModifier);
        nodeAspectsAccessor.writeToCompoundTag(tag, value.nodeAspects);
        nodeAspectsBaseAccessor.writeToCompoundTag(tag, value.nodeAspectsBase);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return
                nodeIdAccessor.compoundTagHasKey(tag)
                        && nodeTypeAccessor.compoundTagHasKey(tag)
                        && nodeModifierAccessor.compoundTagHasKey(tag)
                        && nodeAspectsAccessor.compoundTagHasKey(tag)
                        && nodeAspectsBaseAccessor.compoundTagHasKey(tag);

    }
}
