package thaumcraft.common.lib;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

public class NodeInfo {
    public static final NodeInfo EMPTY = new NodeInfo("",NodeType.EMPTY,NodeModifier.EMPTY,UnmodifiableAspectList.EMPTY,UnmodifiableAspectList.EMPTY);
    public @NotNull String nodeId;
    public @NotNull NodeType nodeType;
    public @NotNull NodeModifier nodeModifier;
    public @NotNull AspectList<Aspect> nodeAspects;
    public @NotNull AspectList<Aspect> nodeAspectsBase;
    public NodeInfo(
            @NotNull String nodeId,
            @NotNull NodeType nodeType,
            @NotNull NodeModifier nodeModifier,
            @NotNull AspectList<Aspect> nodeAspects,
            @NotNull AspectList<Aspect> nodeAspectsBase
    ) {
        this.nodeId = nodeId;
        this.nodeType = nodeType;
        this.nodeModifier = nodeModifier;
        this.nodeAspects = nodeAspects;
        this.nodeAspectsBase = nodeAspectsBase;
    }
}
