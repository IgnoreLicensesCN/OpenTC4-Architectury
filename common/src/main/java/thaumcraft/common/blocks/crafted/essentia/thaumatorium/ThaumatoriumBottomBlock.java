package thaumcraft.common.blocks.crafted.essentia.thaumatorium;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.AbstractExtendedMenuProviderContainerBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.essentiabe.ThaumatoriumBlockEntity;

public class ThaumatoriumBottomBlock extends AbstractExtendedMenuProviderContainerBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public ThaumatoriumBottomBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public ThaumatoriumBottomBlock() {
        this(Properties.of()
                .sound(SoundType.METAL)
                .strength(3, 17)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction dir = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, dir);
    }

    public Direction getCheckDirection() {
        return Direction.UP;
    }
    public Block getCheckBlock() {
        return ThaumcraftBlocks.ThaumcraftBlockInstances.THAUMATORIUM_TOP;
    }
    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos selfPos, RandomSource randomSource) {
        super.tick(blockState, level, selfPos, randomSource);
        if (level.getBlockState(selfPos.relative(getCheckDirection())).getBlock() != this.getCheckBlock()){
            level.setBlockAndUpdate(selfPos, ThaumcraftBlocks.ThaumcraftBlockInstances.ALCHEMICAL_CONSTRUCT.defaultBlockState());
        }
    }

    @Override
    public void neighborChanged(BlockState selfState,
                                Level level,
                                BlockPos selfPos,
                                Block neighborBlock,
                                BlockPos neighborPos,
                                boolean movedByPiston) {
        super.neighborChanged(selfState, level, selfPos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide()) {
            level.scheduleTick(selfPos, this, 1);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new ThaumatoriumBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide && blockEntityType == ThaumcraftBlockEntities.BlockEntityTypeInstances.THAUMATORIUM) {
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof ThaumatoriumBlockEntity thaumatoriumBlockEntity) {
                    thaumatoriumBlockEntity.serverTick();
                }
            });
        }
        return null;
    }
}
