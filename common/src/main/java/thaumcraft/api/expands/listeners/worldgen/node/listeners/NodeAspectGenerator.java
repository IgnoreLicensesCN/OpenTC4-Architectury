package thaumcraft.api.expands.listeners.worldgen.node.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;


public abstract class NodeAspectGenerator implements Comparable<NodeAspectGenerator> {
    public final int priority;

    public NodeAspectGenerator(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(NodeAspectGenerator o) {
        return Integer.compare(priority, o.priority);
    }

    public AspectList<Aspect> getNodeAspects(Level world,
                                             BlockPos pos,
                                             RandomSource random,
                                             boolean silverwood,
                                             boolean eerie,
                                             boolean small,
                                             AspectList<Aspect> previous,
                                             NodeType type,
                                             @Nullable NodeModifier modifier) {
        return getNodeAspects((WorldGenLevel) world, pos, random, silverwood, eerie, small, previous, type, modifier);
    }

    public abstract AspectList<Aspect> getNodeAspects(WorldGenLevel world,
                                              BlockPos pos,
                                              RandomSource random,
                                              boolean silverwood,
                                              boolean eerie,
                                              boolean small,
                                              AspectList<Aspect> previous,
                                              NodeType type,
                                              @Nullable NodeModifier modifier);
}
