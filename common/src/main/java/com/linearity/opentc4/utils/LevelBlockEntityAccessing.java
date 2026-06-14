package com.linearity.opentc4.utils;

import com.linearity.opentc4.annotations.StoleFrom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public class LevelBlockEntityAccessing {
    public static @Nullable BlockEntity getExistingBlockEntity(Level level, BlockPos pos) {
        if (!level.hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())))
        {
            return null;
        }
        var levelChunk = (LevelChunk) level.getChunk(
                SectionPos.blockToSectionCoord(pos.getX()),
                SectionPos.blockToSectionCoord(pos.getZ()),
                ChunkStatus.FULL,
                false
                );
        return getExistingBlockEntity(levelChunk, pos);
    }
    public static @Nullable BlockEntity getExistingBlockEntity(LevelChunk levelChunk, BlockPos pos) {
        if (levelChunk == null) return null;
        return levelChunk.getBlockEntities().get(pos);
    }
    /**
     * Get the {@link BlockEntity} at the given position if it exists.
     * <p>
     * {@link Level#getBlockEntity(BlockPos)} would create a new {@link BlockEntity} if the
     * {@link net.minecraft.world.level.block.Block} has one, but it has not been placed in the world yet
     * (This can happen on world load).
     * @return The BlockEntity at the given position or null if it doesn't exist
     */
    @StoleFrom("forge")
    public static @Nullable BlockEntity getExistingBlockEntity(BlockGetter blockGetter, BlockPos pos) {
        if (blockGetter instanceof Level level){
            return getExistingBlockEntity(level, pos);
        }
        if (blockGetter instanceof LevelChunk levelChunk){
            return getExistingBlockEntity(levelChunk, pos);
        }
        if (blockGetter instanceof ImposterProtoChunk chunk)
        {
            return getExistingBlockEntity(chunk.getWrapped(),pos);
        }
        return blockGetter.getBlockEntity(pos);
    }
}
