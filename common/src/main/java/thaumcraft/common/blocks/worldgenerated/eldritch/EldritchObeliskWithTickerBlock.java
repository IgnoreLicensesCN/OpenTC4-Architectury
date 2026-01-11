package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.eldritch.EldritchObeliskWithTickerBlockEntity;

public class EldritchObeliskWithTickerBlock extends EldritchObeliskBlock {
    public EldritchObeliskWithTickerBlock(Properties properties) {
        super(properties);
    }
    public EldritchObeliskWithTickerBlock() {
        super(Properties.of()
                .strength(50F,20000F)
                .sound(SoundType.STONE)
                .mapColor(MapColor.COLOR_BLACK)
                .lightLevel(s -> 8)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() != this){
            return null;
        }
        return new EldritchObeliskWithTickerBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockState.getBlock() != this){
            return null;
        }
        if (blockEntityType != ThaumcraftBlockEntities.ELDRITCH_OBELISK_WITH_TICKER){
            return null;
        }
        if (level0.isClientSide()){
            return (level, pos, state, blockEntity) -> {
                if (blockEntity instanceof EldritchObeliskWithTickerBlockEntity withTickerBlockEntity) {
                    withTickerBlockEntity.clientTick();
                }
            };
        }
        return (level, pos, state, blockEntity) -> {
            if (blockEntity instanceof EldritchObeliskWithTickerBlockEntity withTickerBlockEntity) {
                withTickerBlockEntity.serverTick();
            }
        };
    }
}
