package thaumcraft.api.listeners.worldgen.node.listeners;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import thaumcraft.api.nodes.NodeType;

import org.jetbrains.annotations.Nullable;

public abstract class NodeTypePicker implements Comparable<NodeTypePicker> {
    public NodeTypePicker(int priority) {
        this.priority = priority;
    }

    public final int priority;


    @Override
    public int compareTo(NodeTypePicker o) {
        return Integer.compare(priority, o.priority);
    }

    //when in world-gen period
    public NodeType onPickingNodeType(
            WorldGenLevel world,
            BlockPos pos,
            RandomSource random,
            boolean silverwood,
            boolean eerie,
            boolean small,
            @Nullable NodeType previous
    ){
        return onPickingNodeType((LevelAccessor)world, pos, random, silverwood, eerie, small, previous);
    };
    //when not in world-gen period(like planting a silver tree)
    public NodeType onPickingNodeType(
            Level world,
            BlockPos pos,
            RandomSource random,
            boolean silverwood,
            boolean eerie,
            boolean small,
            @Nullable NodeType previous
    ){
        return onPickingNodeType((LevelAccessor)world, pos, random, silverwood, eerie, small, previous);
    };
    //for any situation
    public abstract NodeType onPickingNodeType(
            LevelAccessor world,
            BlockPos pos,
            RandomSource random,
            boolean silverwood,
            boolean eerie,
            boolean small,
            @Nullable NodeType previous
    );
}
