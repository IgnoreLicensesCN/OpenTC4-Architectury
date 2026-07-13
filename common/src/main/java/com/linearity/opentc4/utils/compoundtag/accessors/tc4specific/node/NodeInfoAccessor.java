package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.node;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.StringTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.resourcelocation.ResourceLocationTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect.ModifiableAspectListAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.lib.NodeInfo;

public class NodeInfoAccessor extends CompoundTagAccessor<NodeInfo> {
    private final ResourceLocationTagAccessor nodeIdAccessor;
    private final NodeTypeAccessor nodeTypeAccessor;
    private final NodeModifierAccessor nodeModifierAccessor;
    private final ModifiableAspectListAccessor nodeAspectsAccessor;
    private final ModifiableAspectListAccessor nodeAspectsBaseAccessor;

    public NodeInfoAccessor(String tagKey) {
        super(tagKey);
        this.nodeIdAccessor = new ResourceLocationTagAccessor(tagKey + "_id");
        this.nodeTypeAccessor = new NodeTypeAccessor(tagKey + "_type");
        this.nodeModifierAccessor = new NodeModifierAccessor(tagKey + "_modifier");
        this.nodeAspectsAccessor = new ModifiableAspectListAccessor(tagKey + "_aspects");
        this.nodeAspectsBaseAccessor = new ModifiableAspectListAccessor(tagKey + "_aspects_base");
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
