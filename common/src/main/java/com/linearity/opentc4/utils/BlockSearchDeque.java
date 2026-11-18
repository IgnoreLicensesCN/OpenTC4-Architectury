package com.linearity.opentc4.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

public class BlockSearchDeque {
    Int2ObjectMap<LongSet> visitedY_XZ = new Int2ObjectOpenHashMap<>();
    IntArrayList xCoords = new IntArrayList();
    IntArrayList zCoords = new IntArrayList();
    IntArrayList yCoords = new IntArrayList();

    public void push(int x, int y, int z) {
        xCoords.add(x);
        yCoords.add(y);
        zCoords.add(z);
    }

    public int popY() {
        return yCoords.removeInt(yCoords.size() - 1);
    }

    public int popX() {
        return xCoords.removeInt(xCoords.size() - 1);
    }

    public int popZ() {
        return zCoords.removeInt(zCoords.size() - 1);
    }

    public boolean isEmpty() {
        return xCoords.isEmpty();
    }

    protected boolean addChecked(int posX, int posY, int posZ, Int2ObjectMap<LongSet> visitedY_XZ) {
        return visitedY_XZ.computeIfAbsent(posY, ignored -> new LongOpenHashSet()).add(packInt(posX, posZ));
    }

    public boolean addIfChecked(int x, int y, int z) {
        if (addChecked(x, y, z, visitedY_XZ)) {
            push(x, y, z);
            return true;
        }
        return false;
    }

    private static long packInt(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }
}
