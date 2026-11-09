package com.linearity.opentc4.utils.collectionlike.coordlookups;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

//maybe not best GC but i hope it works enough in daily life
//this idea first appear when i want to fake my position.
// i need to get some base station(for phone calling/positioning) from a database--
// i need to lookup which is near the location i've pickup but i dont want to "forEach" all of them
// so i want to separate them into parts and find in parts not all.
public class CubeChunkedLookups<StoreItem> implements CoordLookup<StoreItem> {
    private final byte chunkSizeBits;
    private final int chunkSize;
    private final Long2ObjectMap<Int2ObjectMap<Collection<StoreItem>>> itemsContaining
            = new Long2ObjectOpenHashMap<>();

    public CubeChunkedLookups(byte chunkSizeBits) {
        if (chunkSizeBits < 0) {
            throw new IllegalArgumentException("chunkSizeBits " + chunkSizeBits + " is negative");
        }
        if (chunkSizeBits > 28) {
            //since Integer.MAX is (2^31-1), size greater than ((2^31-1)/3) is not so meaningful
            throw new IllegalArgumentException("chunkSizeBits " + chunkSizeBits + " is too large");
        }
        this.chunkSizeBits = chunkSizeBits;
        this.chunkSize = 1 << chunkSizeBits;
    }

    @Override
    public void store(Vec3i pos, StoreItem value) {
        var xzKey = packInt(
                compressIntIntoChunk(pos.getX()),
                compressIntIntoChunk(pos.getZ())
        );
        var yKey = compressIntIntoChunk(pos.getY());
        itemsContaining.computeIfAbsent(xzKey, k -> new Int2ObjectOpenHashMap<>())
                .computeIfAbsent(
                        yKey, k -> ConcurrentHashMap.newKeySet()
                )
                .add(value);
    }

    @Override
    public void remove(Vec3i pos, StoreItem value) {
        BiFunction<Integer,Collection<StoreItem>,Collection<StoreItem>> removeSingleItemFunction = (y,values) -> {
            values.remove(value);
            if (values.isEmpty()){
                return null;
            }
            return values;
        };
        BiFunction<Long,Int2ObjectMap<Collection<StoreItem>>,Int2ObjectMap<Collection<StoreItem>>> removeInXZFunction = (xz,yMap) -> {
            yMap.computeIfPresent(compressIntIntoChunk(pos.getY()),removeSingleItemFunction);
            if (yMap.isEmpty()) {
                return null;
            }
            return yMap;
        };
        var xzKey = packInt(
                compressIntIntoChunk(pos.getX()),
                compressIntIntoChunk(pos.getZ())
        );
        itemsContaining.computeIfPresent(xzKey,removeInXZFunction);
//        var yMap = itemsContaining.get(xzKey);
//        if (yMap != null) {
//            var yKey = compressIntIntoChunk(pos.getY());
//            var collection = yMap.get(yKey);
//            if (collection != null) {
//                collection.remove(value);
//                if (collection.isEmpty()) {
//                    yMap.remove(yKey);
//                    if (yMap.isEmpty()) {
//                        itemsContaining.remove(xzKey);
//                    }
//                }
//            }
//        }
    }

    protected int compressIntIntoChunk(int toCompress) {
        return (toCompress >>> chunkSizeBits);
    }

    private static long packInt(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    //should i hope JIT optimizing instead of expanding?
    @Override
    public void forItemsNearPos(Vec3i pos, Consumer<StoreItem> action) {
        forItemsNearPosWithRange(pos, action, chunkSize);
    }

    @Override
    public void forItemsNearPosWithRange(Vec3i cubeCenterPos, Consumer<StoreItem> action, int halfLengthOfCube) {
        int centerX = cubeCenterPos.getX();
        int centerY = cubeCenterPos.getY();
        int centerZ = cubeCenterPos.getZ();
        int xIndexStart = compressIntIntoChunk(centerY - halfLengthOfCube);
        int yIndexStart = compressIntIntoChunk(centerY - halfLengthOfCube);
        int zIndexStart = compressIntIntoChunk(centerZ - halfLengthOfCube);
        int xIndexEnd = compressIntIntoChunk(centerX + halfLengthOfCube);
        int yIndexEnd = compressIntIntoChunk(centerY + halfLengthOfCube);
        int zIndexEnd = compressIntIntoChunk(centerZ + halfLengthOfCube);
        Predicate<StoreItem> wrappedConsumer = i -> {
            action.accept(i);
            return false;
        };
        for (int yCurrent = yIndexStart; yCurrent <= yIndexEnd; yCurrent += 1) {
            for (int xCurrent = xIndexStart; xCurrent <= xIndexEnd; xCurrent += 1) {
                for (int zCurrent = zIndexStart; zCurrent <= zIndexEnd; zCurrent += 1) {
                    applyInChunkBasedCoord(xCurrent, yCurrent, zCurrent,wrappedConsumer);
                }
            }
        }
    }

    public Iterator<Collection<StoreItem>> forItemsNearPosWithRangeIterator(Vec3i cubeCenterPos){
        return forItemsNearPosWithRangeIterator(cubeCenterPos, chunkSize);
    }
    public Iterator<Collection<StoreItem>> forItemsNearPosWithRangeIterator(Vec3i cubeCenterPos,int halfLengthOfCube){
        return new CubeChunkedLookupsIterator<>(this,cubeCenterPos,halfLengthOfCube);
    }
    public static class CubeChunkedLookupsIterator<I> implements Iterator<Collection<I>> {
        private final CubeChunkedLookups<I> lookups;
        public CubeChunkedLookupsIterator(CubeChunkedLookups<I> lookups, Vec3i cubeCenterPos, int halfLengthOfCube) {
            this.lookups = lookups;
            int centerX = cubeCenterPos.getX();
            int centerY = cubeCenterPos.getY();
            int centerZ = cubeCenterPos.getZ();
            xIndexStart = lookups.compressIntIntoChunk(centerY - halfLengthOfCube);
            int yIndexStart = lookups.compressIntIntoChunk(centerY - halfLengthOfCube);
            zIndexStart = lookups.compressIntIntoChunk(centerZ - halfLengthOfCube);
            xIndexEnd = lookups.compressIntIntoChunk(centerX + halfLengthOfCube);
            yIndexEnd = lookups.compressIntIntoChunk(centerY + halfLengthOfCube);
            zIndexEnd = lookups.compressIntIntoChunk(centerZ + halfLengthOfCube);
            xIndexLast = xIndexStart;
            yIndexLast = yIndexStart;
            zIndexLast = zIndexStart;
        }
        private final int xIndexStart;
        private final int zIndexStart;
        private final int xIndexEnd;
        private final int yIndexEnd;
        private final int zIndexEnd;
        private int xIndexLast;
        private int yIndexLast;
        private int zIndexLast;
        public Collection<I> next = null;
        @Override
        public boolean hasNext() {
            if (next != null) {
                return true;
            }

            while (yIndexLast <= yIndexEnd) {
                while (xIndexLast <= xIndexEnd) {
                    while (zIndexLast <= zIndexEnd) {
                        next = lookups.getInChunkBasedCoord(xIndexLast,yIndexLast,zIndexLast);
                        if (next != null) {
                            return true;
                        }
                        zIndexLast++;
                    }
                    zIndexLast = zIndexStart;
                    xIndexLast += 1;
                }
                xIndexLast = xIndexStart;
                yIndexLast += 1;
            }
            return false;
        }

        @Override
        public Collection<I> next() {
            if (next == null && !hasNext()){
                throw new IllegalStateException("call hasNext() first");
            }
            var result = next;
            next = null;
            return result;
        }
    }

    //true if broken(function returned true,you can consider this as calling break in for loop)
    @Override
    @SuppressWarnings("UnusedReturnValue")
    public boolean forItemsNearPosWithBreak(Vec3i cubeCenterPos, Predicate<StoreItem> action) {
        return forItemsNearPosWithBreakWithRange(cubeCenterPos, action, chunkSize);
    }

    @Override
    public boolean forItemsNearPosWithBreakWithRange(Vec3i cubeCenterPos, Predicate<StoreItem> action, int rangeManhattan) {
        int centerX = cubeCenterPos.getX();
        int centerY = cubeCenterPos.getY();
        int centerZ = cubeCenterPos.getZ();
        int xIndexStart = compressIntIntoChunk(centerY - rangeManhattan);
        int yIndexStart = compressIntIntoChunk(centerY - rangeManhattan);
        int zIndexStart = compressIntIntoChunk(centerZ - rangeManhattan);
        int xIndexEnd = compressIntIntoChunk(centerX + rangeManhattan);
        int yIndexEnd = compressIntIntoChunk(centerY + rangeManhattan);
        int zIndexEnd = compressIntIntoChunk(centerZ + rangeManhattan);
        for (int yCurrent = yIndexStart; yCurrent <= yIndexEnd; yCurrent += 1) {
            for (int xCurrent = xIndexStart; xCurrent <= xIndexEnd; xCurrent += 1) {
                for (int zCurrent = zIndexStart; zCurrent <= zIndexEnd; zCurrent += 1) {
                    if (applyInChunkBasedCoord(xCurrent, yCurrent, zCurrent,action)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    private @Nullable("empty -> null") Collection<StoreItem> get(int x, int y, int z) {
//        return applyInChunkBasedCoord(compressIntIntoChunk(x), compressIntIntoChunk(y), compressIntIntoChunk(z));
//    }

    private boolean applyInChunkBasedCoord(int x, int y, int z, Predicate<StoreItem> toApply) {
        var xzKey = packInt(
                x,
                z
        );
        var yMap = itemsContaining.computeIfPresent(
                xzKey,
                (k, map) -> map.isEmpty() ? null : map
        );
        if (yMap == null) {
            return false;
        }

        var result = yMap.computeIfPresent(y, (k, collection) -> collection.isEmpty() ? null : collection);
        if (result != null && result.isEmpty()) {
            yMap.remove(y);
            if (yMap.isEmpty()) {
                itemsContaining.remove(xzKey);
            }
            return false;
        }
        if (result != null) {
            for (var item : result) {
                if (item != null && toApply.test(item)) {
                    return true;
                }
            }
        }
        return false;
    }
    private @Nullable Collection<StoreItem> getInChunkBasedCoord(int x, int y, int z) {
        var xzKey = packInt(
                x,
                z
        );
        var yMap = itemsContaining.get(xzKey);
        if (yMap == null) {
            return null;
        }

        return yMap.get(y);
    }
}
