package thaumcraft.common.blocks.crafted.fromtable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import java.util.Objects;

//
//        ↑N
//     ←W    E→
//        ↓S
//LeftPart(facing:E→,with BE and real #use) RightPart(facing:←W)
//
public class ResearchTableRightPartBlock extends Block {
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

    public ResearchTableRightPartBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public ResearchTableRightPartBlock() {
        super(Properties.copy(Blocks.CRAFTING_TABLE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        var facing = blockState.getValue(FACING);
        var leftPartPos = getLeftPartPos(facing, blockPos);
        var leftState = level.getBlockState(leftPartPos);
        return leftState.use(level,player,interactionHand,blockHitResult);
    }

    public BlockPos getLeftPartPos(Direction thisFacing,BlockPos thisPos) {
        return thisPos.relative(thisFacing);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, level, blockPos, randomSource);
        var facing = blockState.getValue(FACING);
        var probablyLeftPartBlockState = level.getBlockState(getLeftPartPos(facing,blockPos));
        if (!probablyLeftPartBlockState.is(ThaumcraftBlocks.RESEARCH_TABLE_LEFT_PART)
                || !Objects.equals(probablyLeftPartBlockState.getValue(ResearchTableLeftPartBlock.FACING).getOpposite(),facing)
        ) {
            level.setBlockAndUpdate(blockPos, ThaumcraftBlocks.TABLE.defaultBlockState().setValue(TableBlock.AXIS, facing.getAxis()));
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        level.scheduleTick(blockPos, this, 1);
    }
}
