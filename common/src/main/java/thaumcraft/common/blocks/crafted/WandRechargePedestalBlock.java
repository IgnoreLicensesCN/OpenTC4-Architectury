package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.WandRechargePedestalBlockBlockEntity;

public class WandRechargePedestalBlock extends AbstractPedestalBlock{
    public WandRechargePedestalBlock(Properties properties) {
        super(properties);
    }
    public WandRechargePedestalBlock() {
        this(
                Properties.of()
                        .strength(3,25)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WandRechargePedestalBlockBlockEntity(blockPos,blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide){
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof WandRechargePedestalBlockBlockEntity rechargePedestal) {
                    rechargePedestal.serverTick();
                }
            });
        }
        return ;
    }
}
