package thaumcraft.common.blocks.technique;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.technique.HoleBlockEntity;

public class HoleBlock extends SuppressedWarningBlock implements EntityBlock {


    public HoleBlock(Properties properties) {
        super(properties);
    }
    public HoleBlock() {
        this(Properties.of().noOcclusion().noCollission().strength(-1,6000000.0F));
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HoleBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide){
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof HoleBlockEntity holeBlockEntity){
                    holeBlockEntity.serverTick();
                }
            });
        }
        return null;
    }
}
