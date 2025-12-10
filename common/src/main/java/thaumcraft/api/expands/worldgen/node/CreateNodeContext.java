package thaumcraft.api.expands.worldgen.node;

import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CreateNodeContext {

    public CreateNodeContext(NodeType nodeType, @Nullable NodeModifier nodeModifier, int x, int y, int z, AspectList aspects) {
        this.nodeType = nodeType;
        this.nodeModifier = nodeModifier;
        this.x = x;
        this.y = y;
        this.z = z;
        this.aspects = aspects;
    }

    public NodeType nodeType;
    @Nullable
    public NodeModifier nodeModifier = null;
    public int x;
    public int y;
    public int z;
    public AspectList aspects;
}
