package thaumcraft.api.expands.worldgen.node.listeners;

import net.minecraft.world.World;
import thaumcraft.api.nodes.NodeModifier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
@ParametersAreNonnullByDefault
public abstract class NodeModifierPicker implements Comparable<NodeModifierPicker> {
    public NodeModifierPicker(int priority) {
        this.priority = priority;
    }

    public final int priority;


    @Override
    public int compareTo(NodeModifierPicker o) {
        return Integer.compare(priority, o.priority);
    }

    public abstract NodeModifier onPickingNodeModifier(World world, int x, int y, int z, Random random, boolean silverwood, boolean eerie, boolean small,@Nullable NodeModifier previous);
}
