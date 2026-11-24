package com.linearity.opentc4.mixin.client.accessors;

import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.atomic.AtomicReferenceArray;

@Mixin(targets = "net.minecraft.client.multiplayer.ClientChunkCache$Storage")
public interface ClientChunkCacheStorageAccessor {
    @Accessor("chunks")
    AtomicReferenceArray<LevelChunk> getChunks();
    @Accessor("chunkRadius")
    int getChunkRadius();
    @Accessor("viewRange")
    int getViewRange();
    @Accessor("viewCenterX")
    int getViewCenterX();
    @Accessor("viewCenterZ")
    int getViewCenterZ();
    @Accessor("chunkCount")
    int getChunkCount();
}
