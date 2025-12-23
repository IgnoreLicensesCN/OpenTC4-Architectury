package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.function.Supplier;

public class HungryChestBlock extends ChestBlock {
    public HungryChestBlock(Properties properties, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(properties, supplier);
    }
    public HungryChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        this(BlockBehaviour.Properties.copy(Blocks.CHEST),supplier);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(ChestBlock.TYPE, ChestType.SINGLE);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2).setValue(TYPE, ChestType.SINGLE);
    }
}
