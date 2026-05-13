package thaumcraft.common.blocks.crafted.fromtable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import thaumcraft.common.blocks.abstracts.AbstractExtendedMenuProviderContainerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.crafted.ResearchTableBlockEntity;

import java.util.Objects;

//
//        ↑N
//     ←W    E→
//        ↓S
//LeftPart(facing:E→,with BE  and real #use) RightPart(facing:←W)
//
public class ResearchTableLeftPartBlock extends AbstractExtendedMenuProviderContainerBlock{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState()
                .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    public ResearchTableLeftPartBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public ResearchTableLeftPartBlock() {
        this(Properties.copy(Blocks.CRAFTING_TABLE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResearchTableBlockEntity(blockPos,blockState);
    }

    public BlockPos getRightPartPos(Direction thisFacing,BlockPos thisPos) {
        return thisPos.relative(thisFacing);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, level, blockPos, randomSource);
        var facing = blockState.getValue(FACING);
        var probablyRightPartBlockState = level.getBlockState(getRightPartPos(facing,blockPos));
        if (!probablyRightPartBlockState.is(ThaumcraftBlocks.RESEARCH_TABLE_RIGHT_PART)
                || !Objects.equals(probablyRightPartBlockState.getValue(ResearchTableRightPartBlock.FACING)
                .getOpposite(),facing)
        ) {
            level.setBlockAndUpdate(blockPos, ThaumcraftBlocks.TABLE.defaultBlockState().setValue(TableBlock.AXIS, facing.getAxis()));
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        level.scheduleTick(blockPos, this, 1);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide()) {
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof ResearchTableBlockEntity researchTable) {
                    researchTable.serverTick();
                }
            });
        }
        return super.getTicker(level, blockState, blockEntityType);
    }
}
