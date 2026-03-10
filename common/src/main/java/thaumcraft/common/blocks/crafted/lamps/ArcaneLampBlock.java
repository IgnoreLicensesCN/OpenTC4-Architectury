package thaumcraft.common.blocks.crafted.lamps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

public class ArcaneLampBlock extends SuppressedWarningBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final int LIGHT_RADIUS = 15;
    public static final VoxelShape LAMP_SHAPE = Block.box(4, 2, 4, 12, 14, 12);

    public ArcaneLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.DOWN));
    }

    public ArcaneLampBlock() {
        this(Properties.of()
                .sound(SoundType.METAL)
                .strength(3, 17)
                .lightLevel(s -> 15)
                .randomTicks()
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace());
    }

    // 5. 核心逻辑：检测邻居变化，如果没有支撑则掉落
    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        Direction attachedFace = state.getValue(FACING);
        if (direction == attachedFace && !this.canSurvive(state,level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos supportPos = pos.relative(direction);
        return canSupportCenter(level, supportPos, direction.getOpposite());
    }

    @Override
    public @NotNull VoxelShape getShape(
            BlockState blockState,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CollisionContext collisionContext) {
        return LAMP_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(
            BlockState blockState,
            BlockGetter blockGetter,
            BlockPos blockPos,
            CollisionContext collisionContext
    ) {
        return LAMP_SHAPE;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
        var pickX = randomSource.nextInt(LIGHT_RADIUS * 2 + 1) - LIGHT_RADIUS;
        var pickZ = randomSource.nextInt(LIGHT_RADIUS * 2 + 1) - LIGHT_RADIUS;
        var pickBlockPos = blockPos.offset(
                pickX,
                Math.max(
                        serverLevel.getMinBuildHeight(),//not too low
                        Math.min(
                                Math.min(
                                        serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE, pickX, pickZ) + 4,
//height limit for lamp
                                        randomSource.nextInt(33) - 16
                                ),//pickY
                                serverLevel.getMaxBuildHeight()//not too high
                        )
                ),
                pickZ
        );
        if (serverLevel.getBlockState(pickBlockPos)
                .isAir()) {
            serverLevel.setBlockAndUpdate(pickBlockPos, ThaumcraftBlocks.GLIMMER_OF_LIGHT.defaultBlockState());
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!level.isClientSide) {
            removeLight(level,blockPos,LIGHT_RADIUS);
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    protected void removeLight(Level level, BlockPos blockPos,int lightRadius) {
        for (int xOffset = -lightRadius; xOffset <= lightRadius; xOffset++) {
            for (int zOffset = -lightRadius; zOffset <= lightRadius; zOffset++) {
                for (int yOffset = -lightRadius; yOffset <= lightRadius; yOffset++) {
                    var pickPos = blockPos.offset(xOffset, yOffset, zOffset);
                    if (level.getBlockState(pickPos).is(ThaumcraftBlocks.GLIMMER_OF_LIGHT)) {
                        level.setBlockAndUpdate(pickPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }
}
