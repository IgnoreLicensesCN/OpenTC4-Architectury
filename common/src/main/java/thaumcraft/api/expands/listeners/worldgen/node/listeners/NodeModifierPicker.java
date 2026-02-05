package thaumcraft.api.expands.listeners.worldgen.node.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.api.nodes.NodeModifier;

import org.jetbrains.annotations.Nullable;

public abstract class NodeModifierPicker implements Comparable<NodeModifierPicker> {
    public NodeModifierPicker(int priority) {
        this.priority = priority;
    }

    public final int priority;


    @Override
    public int compareTo(NodeModifierPicker o) {
        return Integer.compare(priority, o.priority);
    }

    public abstract NodeModifier onPickingNodeModifier(WorldGenLevel world, BlockPos pos, RandomSource random, boolean silverwood, boolean eerie, boolean small, @Nullable NodeModifier previous);
    public NodeModifier onPickingNodeModifier(Level world, BlockPos pos, RandomSource random, boolean silverwood, boolean eerie, boolean small, @Nullable NodeModifier previous){
        return onPickingNodeModifier((WorldGenLevel)world, pos, random, silverwood, eerie, small, previous);
    };
}
