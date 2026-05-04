package thaumcraft.common.blocks.crafted.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.pipes.EssentiaTubeOnewayBlockEntity;

public class EssentiaTubeOnewayBlock extends EssentiaTubeBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public EssentiaTubeOnewayBlock(Properties properties) {
        super(properties);
        registerDefaultState();
    }
    public EssentiaTubeOnewayBlock() {
        super();
        registerDefaultState();
    }
    private void registerDefaultState() {
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getClickedFace());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaTubeOnewayBlockEntity(blockPos, blockState);
    }
}
