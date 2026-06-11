package com.linearity.opentc4.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@NotThreadSafe
public class FurthestBlockFinder {
    private final Level level;
    private final Predicate<BlockState> matcher;

    private final int maxHeight;
    private final int maxXZManhattanDistance;

    public FurthestBlockFinder(
            Level level,
            Predicate<BlockState> matcher,
            int maxHeight,
            int maxXZManhattanDistance
    ) {
        this.level = level;
        this.matcher = matcher;
        this.maxHeight = maxHeight;
        this.maxXZManhattanDistance = maxXZManhattanDistance;
    }

    public BlockPos find(BlockPos start) {
        Set<BlockPos> visited = new HashSet<>();

        BlockPos best = start;
        int bestY = start.getY();

        ArrayDeque<BlockPos> stack = new ArrayDeque<>();
        stack.push(start);

        while (!stack.isEmpty()) {

            BlockPos pos = stack.pop();

            if (!visited.add(pos))
                continue;

            if (pos.getY() > bestY) {
                best = pos;
                bestY = pos.getY();
            }
            else if (pos.getY() == bestY) {
                if (Math.abs(pos.getX() - start.getX()) + Math.abs(pos.getZ() - start.getZ())
                        > Math.abs(best.getX() - start.getX()) + Math.abs(best.getZ() - start.getZ())
                ) {
                    best = pos;
                    bestY = pos.getY();
                }
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = 1; dy >= -1; dy--) {
                    for (int dz = -1; dz <= 1; dz++) {

                        if (dx == 0 && dy == 0 && dz == 0)
                            continue;

                        BlockPos next = pos.offset(dx, dy, dz);

                        if (Math.abs(next.getX() - start.getX()) > maxXZManhattanDistance)
                            continue;

                        if (Math.abs(next.getZ() - start.getZ()) > maxXZManhattanDistance)
                            continue;

                        if (Math.abs(next.getY() - start.getY()) > maxHeight)
                            continue;

                        if (!matcher.test(level.getBlockState(next)))
                            continue;

                        stack.push(next);
                    }
                }
            }
        }

        return best;
    }
}
