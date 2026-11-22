package com.linearity.opentc4.utils.blocksearch;

import com.linearity.opentc4.utils.BlockSearchDeque;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import oshi.annotation.concurrent.NotThreadSafe;

//TODO:Result cache in some situations to reduce lag

//y then xz
//tried to reduce "new BlockPos"
//searches upper first
@NotThreadSafe
public class HighestFurthestBlockFinder {
    protected final Level level;
    protected final ValidFindingBlockPredicate matcher;

    protected final int maxHeight;
    protected final int maxXManhattanDistance;
    protected final int maxZManhattanDistance;

    public interface TriIntConsumer {
        void accept(int x, int y, int z);
    }
    public interface ValidFindingBlockPredicate {
        boolean test(Level level, BlockPos pos,BlockState state);
    }
    protected final TriIntConsumer validPosConsumer;
    public HighestFurthestBlockFinder(Level level,ValidFindingBlockPredicate matcher,int maxHeight,int maxXZManhattanDistance) {
        this(level,matcher,maxHeight,maxXZManhattanDistance,(_ignored,_what,_ever) -> {});
    }
    public HighestFurthestBlockFinder(
            Level level,
            ValidFindingBlockPredicate matcher,
            int maxHeight,
            int maxXZManhattanDistance,
            TriIntConsumer validPosConsumer
    ){
        this(level,matcher,maxHeight,maxXZManhattanDistance,maxXZManhattanDistance,validPosConsumer);
    }
    public HighestFurthestBlockFinder(
            Level level,
            ValidFindingBlockPredicate matcher,
            int maxHeight,
            int maxXManhattanDistance,
            int maxZManhattanDistance,
            TriIntConsumer validPosConsumer
    ) {
        this.level = level;
        this.matcher = matcher;
        this.maxHeight = maxHeight;
        this.maxXManhattanDistance = maxXManhattanDistance;
        this.maxZManhattanDistance = maxZManhattanDistance;
        this.validPosConsumer = validPosConsumer;
    }

    public BlockPos find(BlockPos start) {
        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();
        int bestX = startX;
        int bestY = startY;
        int bestZ = startZ;

        BlockSearchDeque stack = new BlockSearchDeque();
        stack.addIfNotChecked(startX, startY, startZ);
//        ArrayDeque<BlockPos> stack = new ArrayDeque<>();
//        stack.push(start);
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

            addAroundBlocksToSearch(startX, startY, startZ, posX, posY, posZ, stack);
        }

        return new BlockPos(bestX ,bestY, bestZ);
    }

    protected void addAroundBlocksToSearch(int startX,int startY,int startZ, int posX,int posY,int posZ, BlockSearchDeque stack) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 1; dy >= -1; dy--) {
                for (int dz = -1; dz <= 1; dz++) {
                    checkSingleBlock(startX, startY, startZ, posX, posY, posZ, stack, dx, dy, dz);
                }
            }
        }
    }

    protected void checkSingleBlock(int startX, int startY, int startZ, int posX, int posY, int posZ, BlockSearchDeque stack, int dx, int dy, int dz) {
        if (dx == 0 && dy == 0 && dz == 0) {
            return;
        }

        int nextX = posX + dx;
        int nextY = posY + dy;
        int nextZ = posZ + dz;

        if (Math.abs(nextX - startX) > maxXManhattanDistance
                || Math.abs(nextZ - startZ) > maxZManhattanDistance
        ) {
            return;
        }

        if (Math.abs(nextY - startY) > maxHeight) {
            return;
        }

        if (!testBlockState(nextX, nextY, nextZ)){
            return;
        }

        if (stack.addIfNotChecked(nextX, nextY, nextZ)){
            validPosConsumer.accept(nextX, nextY, nextZ);
        }
    }


    private final BlockPos.MutableBlockPos cachePos = new BlockPos.MutableBlockPos();
    public boolean testBlockState(int x,int y,int z) {
        return matcher.test(level,cachePos.set(x, y, z),level.getBlockState(cachePos));
    }

    private int manhattanDistanceXZ(int xa,int za,int xb, int zb){
        return Math.abs(xa - xb) + Math.abs(za - zb);
    }

}
