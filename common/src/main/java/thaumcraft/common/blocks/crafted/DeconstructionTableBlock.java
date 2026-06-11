package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.AbstractExtendedMenuProviderContainerBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.DeconstructionTableBlockEntity;

public class DeconstructionTableBlock extends AbstractExtendedMenuProviderContainerBlock {
    //TODO:BER
    public DeconstructionTableBlock(Properties properties) {
        super(properties);
    }

    public DeconstructionTableBlock() {
        this(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE));
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DeconstructionTableBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.BlockEntityTypeInstances.DECONSTRUCTION_TABLE) {
            return (level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof DeconstructionTableBlockEntity deconstructionTable) {
                    deconstructionTable.tick();
                }
            };
        }
        return null;
    }
}
