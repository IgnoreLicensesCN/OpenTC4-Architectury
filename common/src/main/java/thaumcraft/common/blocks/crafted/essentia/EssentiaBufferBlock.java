package thaumcraft.common.blocks.crafted.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.crafted.essentia.pipes.EssentiaTubeBlock;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaBufferBlockEntity;

public class EssentiaBufferBlock
        extends EssentiaTubeBlock
        implements EntityBlock {
    public EssentiaBufferBlock(Properties properties) {
        super(properties);
    }
    public EssentiaBufferBlock() {
        super();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaBufferBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;
        return ((level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof EssentiaBufferBlockEntity buffer) {
                buffer.serverTick();
            }
        });
    }
}
