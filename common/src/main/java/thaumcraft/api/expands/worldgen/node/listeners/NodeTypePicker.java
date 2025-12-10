package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.level.Level;
import thaumcraft.api.nodes.NodeType;

import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class NodeTypePicker implements Comparable<NodeTypePicker> {
    public NodeTypePicker(int priority) {
        this.priority = priority;
    }

    public final int priority;


    @Override
    public int compareTo(NodeTypePicker o) {
        return Integer.compare(priority, o.priority);
    }

    public abstract NodeType onPickingNodeType(Level world, int x, int y, int z, Random random, boolean silverwood, boolean eerie, boolean small,@Nullable NodeType previous);
}
