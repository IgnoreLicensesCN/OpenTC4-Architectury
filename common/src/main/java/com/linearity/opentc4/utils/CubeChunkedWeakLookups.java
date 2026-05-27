package com.linearity.opentc4.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.function.Consumer;

//maybe not best GC but i hope it works enough in daily life
//this idea first appear when i want to fake my position.
// i need to get some base station(for phone calling/positioning) from a database--
// i need to lookup which is near the location i've pickup but i dont want to "forEach" all of them
// so i want to separate them into parts and find in parts not all.
public class CubeChunkedWeakLookups<StoreItem> {
    private final byte chunkSizeBits;
    private final int chunkSize;
    private final Long2ObjectMap<Int2ObjectMap<Collection<StoreItem>>> itemsContaining
            = new Long2ObjectOpenHashMap<>();

    public CubeChunkedWeakLookups(byte chunkSizeBits) {
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

    public void store(Vec3i pos, StoreItem value) {
        var xzKey = packInt(
                compressIntIntoChunk(pos.getX()),
                compressIntIntoChunk(pos.getZ())
        );
        var yKey = compressIntIntoChunk(pos.getY());
        itemsContaining.computeIfAbsent(xzKey, k -> new Int2ObjectOpenHashMap<>())
                .computeIfAbsent(
                        yKey, k -> Collections.newSetFromMap(
                                new WeakHashMap<>()
                        )
                )
                .add(value);
    }

    public void remove(Vec3i pos, StoreItem value) {
        var xzKey = packInt(
                compressIntIntoChunk(pos.getX()),
                compressIntIntoChunk(pos.getZ())
        );
        var yKey = compressIntIntoChunk(pos.getY());
        var yMap = itemsContaining.get(xzKey);
        if (yMap != null) {
            var collection = yMap.get(yKey);
            if (collection != null) {
                collection.remove(value);
                if (collection.isEmpty()) {
                    yMap.remove(yKey);
                    if (yMap.isEmpty()) {
                        itemsContaining.remove(xzKey);
                    }
                }
            }
        }
    }

    protected int compressIntIntoChunk(int toCompress) {
        return (toCompress >> chunkSizeBits);
    }

    private static long packInt(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    //should i hope JIT optimizing instead of expanding?
    public void forItemsNearPos(Vec3i pos, Consumer<StoreItem> action) {
        forItemsNearPosWithRange(pos, action, chunkSize);
    }

    public void forItemsNearPosWithRange(Vec3i pos, Consumer<StoreItem> action, int rangeManhattan) {
        var posMin = pos.offset(-rangeManhattan, -rangeManhattan, -rangeManhattan);
        var posMax = pos.offset(rangeManhattan, rangeManhattan, rangeManhattan);
        for (int yCurrent = compressIntIntoChunk(posMin.getY()); yCurrent <= compressIntIntoChunk(posMax.getY()); yCurrent += 1) {
            for (int xCurrent = compressIntIntoChunk(posMin.getX()); xCurrent <= compressIntIntoChunk(posMax.getX()); xCurrent += 1) {
                for (int zCurrent = compressIntIntoChunk(posMin.getZ()); zCurrent <= compressIntIntoChunk(posMax.getZ()); zCurrent += 1) {
                    var items = getInChunkBasedCoord(xCurrent, yCurrent, zCurrent);
                    if (items != null) {
                        items.forEach(action);
                    }
                }
            }
        }
    }

    @FunctionalInterface
    public interface Object2BooleanFunction<StoreItem> {
        boolean apply(StoreItem storeItem);
    }

    //true if broken(function returned true,you can consider this as calling break in for loop)
    @SuppressWarnings("UnusedReturnValue")
    public boolean forItemsNearPosWithBreak(Vec3i pos, Object2BooleanFunction<StoreItem> action) {
        return forItemsNearPosWithBreakWithRange(pos, action, chunkSize);
    }

    public boolean forItemsNearPosWithBreakWithRange(Vec3i pos, Object2BooleanFunction<StoreItem> action, int rangeManhattan) {

        var posMin = pos.offset(-rangeManhattan, -rangeManhattan, -rangeManhattan);
        var posMax = pos.offset(rangeManhattan, rangeManhattan, rangeManhattan);

        for (int yCurrent = compressIntIntoChunk(posMin.getY()); yCurrent <= compressIntIntoChunk(posMax.getY()); yCurrent += 1) {
            for (int xCurrent = compressIntIntoChunk(posMin.getX()); xCurrent <= compressIntIntoChunk(posMax.getX()); xCurrent += 1) {
                for (int zCurrent = compressIntIntoChunk(posMin.getZ()); zCurrent <= compressIntIntoChunk(posMax.getZ()); zCurrent += 1) {
                    var items = getInChunkBasedCoord(xCurrent, yCurrent, zCurrent);
                    if (items != null) {
                        for (var item : items) {
                            if (action.apply(item)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private @Nullable("empty -> null") Collection<StoreItem> get(int x, int y, int z) {
        return getInChunkBasedCoord(compressIntIntoChunk(x), compressIntIntoChunk(y), compressIntIntoChunk(z));
    }

    private @Nullable("empty -> null") Collection<StoreItem> getInChunkBasedCoord(int x, int y, int z) {
        var xzKey = packInt(
                x,
                z
        );
        var yMap = itemsContaining.computeIfPresent(
                xzKey,
                (k, map) -> map.isEmpty() ? null : map
        );
        if (yMap == null) {
            return null;
        }

        var result = yMap.computeIfPresent(y, (k, collection) -> collection.isEmpty() ? null : collection);
        if (result != null && result.isEmpty()) {
            yMap.remove(y);
            if (yMap.isEmpty()) {
                itemsContaining.remove(xzKey);
            }
            return null;
        }
        return result;
    }


}
