package thaumcraft.common.lib;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

public class NodeInfo {
    public static final ResourceLocation EMPTY_ID = new ResourceLocation("a_b","c_d");
    public static final NodeInfo EMPTY = new NodeInfo(EMPTY_ID,NodeType.EMPTY,NodeModifier.EMPTY,UnmodifiableAspectList.EMPTY,UnmodifiableAspectList.EMPTY);
    public @NotNull ResourceLocation nodeId;
    public @NotNull NodeType nodeType;
    public @NotNull NodeModifier nodeModifier;
    public @NotNull AspectList<Aspect> nodeAspects;
    public @NotNull AspectList<Aspect> nodeAspectsBase;
    public NodeInfo(
            @NotNull ResourceLocation nodeId,
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
