package com.linearity.opentc4.mixin;

import com.linearity.opentc4.clientrenderapi.IClientRandomTickableBlock;
import com.linearity.opentc4.mixinaccessors.ClientChunkCacheStorageAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientChunkCache.class)
public abstract class ClientChunkCacheMixin {
    @Shadow @Final ClientLevel level;
    @Shadow ClientChunkCacheStorageAccessor storage;

    //what i called "render util"
    @Inject(method = "tick", at = @At("HEAD"))
    private void opentc4$clientRandomTick(BooleanSupplier booleanSupplier, boolean bl, CallbackInfo ci) {
        if (bl){
            opentc4$tickChunks();
        }
    }

    @Unique
    private void opentc4$tickChunks(){
        if (Minecraft.getInstance().isPaused()){return;}
        if (!this.level.isDebug()) {
            var chunks = storage.opentc4$chunks();

            for (int i = 0; i < chunks.length(); i++) {
                LevelChunk chunk = chunks.get(i);
                if (chunk != null) {
                    ChunkPos chunkpos = chunk.getPos();

                    if (this.level.shouldTickBlocksAt(chunkpos.toLong())) {
                        opentc4$tickChunk(level,chunk, 50);
                    }
                }
            }
        }
    }


    @Unique
    private void opentc4$tickChunk(ClientLevel level,LevelChunk levelChunk, int tickCount) {
        if (tickCount > 0) {
            var chunkPos = levelChunk.getPos();
            int j = chunkPos.getMinBlockX();
            int k = chunkPos.getMinBlockZ();
            LevelChunkSection[] levelChunkSections = levelChunk.getSections();

            for (int n = 0; n < levelChunkSections.length; n++) {
                LevelChunkSection levelChunkSection = levelChunkSections[n];
                if (levelChunkSection.isRandomlyTicking()) {
                    int lx = levelChunk.getSectionYFromSectionIndex(n);
                    int o = SectionPos.sectionToBlockCoord(lx);

                    for (int m = 0; m < tickCount; m++) {
                        BlockPos blockPosToPlayRandomTick = level.getBlockRandomPos(j, o, k, 15);
                        var bState = level.getBlockState(blockPosToPlayRandomTick);
                        if (bState.getBlock() instanceof IClientRandomTickableBlock tickable){
                            tickable.onClientRandomTick(bState,level,blockPosToPlayRandomTick);
                        }
                    }
                }
            }
        }
    }
}

