package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.level.Level;
import thaumcraft.api.nodes.NodeModifier;

import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class NodeModifierPicker implements Comparable<NodeModifierPicker> {
    public NodeModifierPicker(int priority) {
        this.priority = priority;
    }

    public final int priority;


    @Override
    public int compareTo(NodeModifierPicker o) {
        return Integer.compare(priority, o.priority);
    }

    public abstract NodeModifier onPickingNodeModifier(Level world, int x, int y, int z, Random random, boolean silverwood, boolean eerie, boolean small,@Nullable NodeModifier previous);
}
