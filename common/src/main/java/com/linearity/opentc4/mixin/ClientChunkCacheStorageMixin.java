package com.linearity.opentc4.mixin;

import com.linearity.opentc4.mixinaccessors.ClientChunkCacheStorageAccessor;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(targets = "net.minecraft.client.multiplayer.ClientChunkCache$Storage")
public class ClientChunkCacheStorageMixin implements ClientChunkCacheStorageAccessor {
    @Shadow @Final AtomicReferenceArray<LevelChunk> chunks;
    @Shadow @Final int chunkRadius;
    @Shadow private @Final int viewRange;
    @Shadow volatile int viewCenterX;
    @Shadow volatile int viewCenterZ;
    @Shadow int chunkCount;

    @Override
    public AtomicReferenceArray<LevelChunk> opentc4$chunks() {
        return chunks;
    }

    @Override
    public int opentc4$chunkRadius() {
        return chunkRadius;
    }

    @Override
    public int opentc4$viewRange() {
        return viewRange;
    }

    @Override
    public int opentc4$viewCenterX() {
        return viewCenterX;
    }

    @Override
    public int opentc4$viewCenterZ() {
        return viewCenterZ;
    }

    @Override
    public int opentc4$chunkCount() {
        return chunkCount;
    }
}

