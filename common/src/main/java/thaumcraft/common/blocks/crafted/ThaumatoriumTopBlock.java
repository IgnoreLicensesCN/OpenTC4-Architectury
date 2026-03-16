package thaumcraft.common.blocks.crafted;

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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

public class ThaumatoriumTopBlock extends SuppressedWarningBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public ThaumatoriumTopBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public ThaumatoriumTopBlock() {
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
        return Direction.DOWN;
    }
    public Block getCheckBlock() {
        return ThaumcraftBlocks.THAUMATORIUM_BOTTOM;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos selfPos, RandomSource randomSource) {
        super.tick(blockState, level, selfPos, randomSource);
        if (level.getBlockState(selfPos.relative(getCheckDirection())).getBlock() != this.getCheckBlock()){
            level.setBlockAndUpdate(selfPos,ThaumcraftBlocks.ALCHEMICAL_CONSTRUCT.defaultBlockState());
        }
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        var bottomPos = blockPos.relative(getCheckDirection());
        return ThaumcraftBlocks.THAUMATORIUM_BOTTOM.use(level.getBlockState(bottomPos), level, bottomPos, player, interactionHand, blockHitResult);
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
}
