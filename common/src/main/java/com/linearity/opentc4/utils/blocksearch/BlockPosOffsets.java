package com.linearity.opentc4.utils.blocksearch;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.BlockPos;

public class BlockPosOffsets {
    public final BlockPos.MutableBlockPos posBase = new BlockPos.MutableBlockPos(0, 0, 0);
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

    public void getAsOffset(BlockPos.MutableBlockPos receiver, int index) {
        int x = xCoords.getInt(index);
        int y = yCoords.getInt(index);
        int z = zCoords.getInt(index);
        receiver.set(x, y, z);
    }

    public void getWithBase(BlockPos.MutableBlockPos receiver, int index) {
        getAsOffset(receiver, index);
        receiver.move(posBase);
    }

    public void getLastAsOffset(BlockPos.MutableBlockPos receiver) {
        getAsOffset(receiver, size() - 1);
    }

    public void getLastWithBase(BlockPos.MutableBlockPos receiver) {
        getLastAsOffset(receiver);
        receiver.move(posBase);
    }

    public int size() {
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
        return Long.compare(dist(x1, y1, z1), dist(x2, y2, z2));
    }

    private static long dist(long x1, long y1, long z1) {
        return Math.abs(x1) + Math.abs(y1) + Math.abs(z1);
    }
}
