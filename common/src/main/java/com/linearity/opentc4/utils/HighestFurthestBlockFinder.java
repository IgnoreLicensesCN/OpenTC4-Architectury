package com.linearity.opentc4.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongSet;
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
//        Set<BlockPos> visited = new HashSet<>();
        Int2ObjectMap<LongSet> visitedY_XZ = new Int2ObjectRBTreeMap<>();

//        BlockPos best = start;
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

                    if (Math.abs(nextX - startX) > maxXManhattanDistance
                            || Math.abs(nextZ - startZ) > maxZManhattanDistance
                    ) {
                        continue;
                    }

                    if (Math.abs(nextY - startY) > maxHeight) {
                        continue;
                    }

                    if (!testBlockState(nextX, nextY, nextZ)){
                        continue;
                    }

                    if (stack.addIfNotChecked(nextX, nextY, nextZ)){
                        validPosConsumer.accept(nextX, nextY, nextZ);
                    }
                }
            }
        }
    }


    private final BlockPos.MutableBlockPos cachePos = new BlockPos.MutableBlockPos();
    public boolean testBlockState(int x,int y,int z) {
        return matcher.test(level,cachePos.set(x, y, z),level.getBlockState(cachePos));
    }

    private int manhattanDistanceXZ(int xa,int za,int xb, int zb){
        return Math.abs(xa - xb) + Math.abs(za - zb);
    }

    public static class BlockPosOffsets {
        public final BlockPos.MutableBlockPos posBase = new BlockPos.MutableBlockPos(0,0,0);
        IntArrayList xCoords = new IntArrayList();
        IntArrayList zCoords = new IntArrayList();
        IntArrayList yCoords = new IntArrayList();

        public void add(BlockPos pos) {
            xCoords.add(pos.getX() - posBase.getX());
            yCoords.add(pos.getY() - posBase.getY());
            zCoords.add(pos.getZ() - posBase.getZ());
        }

        public void add(int x, int y, int z) {
            xCoords.add(x - posBase.getX());
            yCoords.add(y - posBase.getY());
            zCoords.add(z - posBase.getZ());
        }

        public void removeLastAsOffset(BlockPos.MutableBlockPos receiver) {
            int x = xCoords.removeInt(xCoords.size() - 1);
            int y = yCoords.removeInt(yCoords.size() - 1);
            int z = zCoords.removeInt(zCoords.size() - 1);
            receiver.set(x, y, z);
        }
        public void removeLastWithBase(BlockPos.MutableBlockPos receiver) {
            removeLastAsOffset(receiver);
            receiver.move(posBase);
        }

        public void getAsOffset(BlockPos.MutableBlockPos receiver,int index) {
            int x = xCoords.getInt(index);
            int y = yCoords.getInt(index);
            int z = zCoords.getInt(index);
            receiver.set(x, y, z);
        }
        public void getWithBase(BlockPos.MutableBlockPos receiver,int index){
            getAsOffset(receiver,index);
            receiver.move(posBase);
        }

        public void getLastAsOffset(BlockPos.MutableBlockPos receiver) {
            getAsOffset(receiver,size()-1);
        }
        public void getLastWithBase(BlockPos.MutableBlockPos receiver){
            getLastAsOffset(receiver);
            receiver.move(posBase);
        }

        public int size(){
            return xCoords.size();
        }

        public boolean isEmpty() {
            return xCoords.isEmpty();
        }

        public void clear() {
            xCoords.clear();
            yCoords.clear();
            zCoords.clear();
        }

        public void sort() {
            it.unimi.dsi.fastutil.Arrays.quickSort(0, xCoords.size(),
                    (a, b) -> distSubtractionSign(xCoords.getInt(a), yCoords.getInt(a), zCoords.getInt(a), xCoords.getInt(b), yCoords.getInt(b), zCoords.getInt(b)),
                    (a, b) -> {
                        swap(xCoords, a, b);
                        swap(yCoords, a, b);
                        swap(zCoords, a, b);
                    }
            );
        }

        private static void swap(IntArrayList list, int i, int j) {
            int tmp = list.getInt(i);
            list.set(i, list.getInt(j));
            list.set(j, tmp);
        }

        private static int distSubtractionSign(int x1, int y1, int z1, int x2, int y2, int z2) {
            return Long.compare(dist(x1, y1, z1),dist(x2, y2, z2));
        }
        private static long dist(long x1, long y1, long z1) {
            return Math.abs(x1) + Math.abs(y1) + Math.abs(z1);
        }
    }
}
