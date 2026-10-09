package com.linearity.opentc4.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.function.Predicate;

//y then xz
//tried to reduce "new BlockPos"
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
        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();
//        Set<BlockPos> visited = new HashSet<>();
        Int2ObjectMap<LongSet> visitedY_XZ = new Int2ObjectRBTreeMap<>();

//        BlockPos best = start;
        int bestX = startX;
        int bestY = startY;
        int bestZ = startZ;

        BlockSearchDeque stack = new BlockSearchDeque();
        stack.push(startX, startY, startZ);
//        ArrayDeque<BlockPos> stack = new ArrayDeque<>();
//        stack.push(start);
        addChecked(startX,startY,startZ,visitedY_XZ);
        while (!stack.isEmpty()) {

            int posX = stack.popX();
            int posY = stack.popY();
            int posZ = stack.popZ();

//            if (!visitedY_XZ.computeIfAbsent(posY,ignored -> new LongOpenHashSet()).add(packInt(posX,posZ))) {
//                continue;
//            }

            if (posY > bestY) {
                bestX = posX;
                bestY = posY;
                bestZ = posZ;
            } else if (posY == bestY) {
                if (manhattanDistanceXZ(startX, startZ, posX, posZ) > manhattanDistanceXZ(startX, startZ, bestX, bestZ)
                ) {
                    bestX = posX;
//                    bestY = posY;
                    bestZ = posZ;
                }
            }

            addAroundBlocksToSearch(startX, startY, startZ, posX, posY, posZ, stack, visitedY_XZ);
        }

        return new BlockPos(bestX ,bestY, bestZ);
    }

    private void addAroundBlocksToSearch(int startX,int startY,int startZ, int posX,int posY,int posZ, BlockSearchDeque stack,Int2ObjectMap<LongSet> visitedY_XZ) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 1; dy >= -1; dy--) {
                for (int dz = -1; dz <= 1; dz++) {

                    if (dx == 0 && dy == 0 && dz == 0) {
                        continue;
                    }

                    int nextX = posX + dx;
                    int nextY = posY + dy;
                    int nextZ = posZ + dz;

                    if (Math.abs(nextX - startX) + Math.abs(nextZ - startZ) > maxXZManhattanDistance)
                        continue;

                    if (Math.abs(nextY - startY) > maxHeight)
                        continue;

                    if (!testBlockState(nextX, nextY, nextZ)){
                        continue;
                    }

                    if (addChecked(nextX, nextY, nextZ, visitedY_XZ)) {
                        stack.push(nextX, nextY, nextZ);
                    }
                }
            }
        }
    }

    private boolean addChecked(int posX, int posY, int posZ, Int2ObjectMap<LongSet> visitedY_XZ) {
        return visitedY_XZ.computeIfAbsent(posY, ignored -> new LongOpenHashSet()).add(packInt(posX, posZ));
    }

    private final BlockPos.MutableBlockPos cachePos = new BlockPos.MutableBlockPos();
    public boolean testBlockState(int x,int y,int z) {
        return matcher.test(level.getBlockState(cachePos.set(x, y, z)));
    }

    private int manhattanDistanceXZ(int xa,int za,int xb, int zb){
        return Math.abs(xa - xb) + Math.abs(za - zb);
    }
    public static class BlockSearchDeque {
        IntArrayList xCoords = new IntArrayList();
        IntArrayList zCoords = new IntArrayList();
        IntArrayList yCoords = new IntArrayList();
        public void push(int x,int y, int z) {
            xCoords.add(x);
            yCoords.add(y);
            zCoords.add(z);
        }
        public int popY(){
            return yCoords.removeInt(yCoords.size()-1);
        }
        public int popX(){
            return xCoords.removeInt(xCoords.size()-1);
        }
        public int popZ(){
            return zCoords.removeInt(zCoords.size()-1);
        }
        public boolean isEmpty(){
            return xCoords.isEmpty();
        }
    }

    private static long packInt(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }
}
