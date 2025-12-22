package com.linearity.opentc4.mixinaccessors;

import net.minecraft.world.level.chunk.LevelChunk;

import java.util.concurrent.atomic.AtomicReferenceArray;

public interface ClientChunkCacheStorageAccessor {
    AtomicReferenceArray<LevelChunk> opentc4$chunks();
    int opentc4$chunkRadius();
    int opentc4$viewRange();
    int opentc4$viewCenterX();
    int opentc4$viewCenterZ();
    int opentc4$chunkCount();
}
