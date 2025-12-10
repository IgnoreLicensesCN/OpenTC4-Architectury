package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

import org.jetbrains.annotations.Nullable;

import java.util.Random;


public abstract class NodeAspectGenerator implements Comparable<NodeAspectGenerator> {
    public final int priority;
    public NodeAspectGenerator(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(NodeAspectGenerator o) {
        return Integer.compare(priority, o.priority);
    }

    public abstract AspectList getNodeAspects(Level world, int x, int y, int z, Random random, boolean silverwood, boolean eerie, boolean small, AspectList previous, NodeType type,@Nullable NodeModifier modifier);
}
