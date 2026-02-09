package thaumcraft.api.listeners.worldgen.node;

import net.minecraft.core.BlockPos;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

import org.jetbrains.annotations.Nullable;



public class CreateNodeContext {

    public CreateNodeContext(NodeType nodeType, @Nullable NodeModifier nodeModifier, BlockPos pos, AspectList<Aspect>aspects) {
        this.nodeType = nodeType;
        this.nodeModifier = nodeModifier;
        this.pos = pos;
        this.aspects = aspects;
    }

    public NodeType nodeType;
    @Nullable
    public NodeModifier nodeModifier = null;
    public BlockPos pos;
    public AspectList<Aspect>aspects;
}
