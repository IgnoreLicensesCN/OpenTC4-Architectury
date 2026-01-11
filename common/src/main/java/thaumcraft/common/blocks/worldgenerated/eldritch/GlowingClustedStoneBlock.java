package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.IShapeConnectNearBlock;

public class GlowingClustedStoneBlock extends Block implements IShapeConnectNearBlock {
    public GlowingClustedStoneBlock(Properties properties) {
        super(properties);
    }
    public GlowingClustedStoneBlock() {
        super(
                BlockBehaviour.Properties.copy(Blocks.STONE)
                        .requiresCorrectToolForDrops()
                        .lightLevel(s -> 12)
                        .strength(2,30)
        );
    }

    //template
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return getShapeConnectNear(blockState, blockGetter, blockPos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return getShapeConnectNear(blockState, blockGetter, blockPos);
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return getShapeConnectNear(blockState, blockGetter, blockPos);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return getShapeConnectNear(blockState, blockGetter, blockPos);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return getShapeConnectNear(blockState, blockGetter, blockPos);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return getShapeConnectNear(blockState, blockGetter, blockPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        addStateDefinition(builder);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var blockGetter = blockPlaceContext.getLevel();
        var blockPos = blockPlaceContext.getClickedPos();
        var blockState = super.getStateForPlacement(blockPlaceContext);
        if (blockState == null) {
            blockState = this.defaultBlockState();
        }
        return getState(blockState,blockGetter,blockPos);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level blockGetter, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        blockGetter.setBlock(blockPos, getState(blockState,blockGetter,blockPos), 3);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel blockGetter, BlockPos blockPos, RandomSource randomSource) {
        blockGetter.setBlock(blockPos, getState(blockState,blockGetter,blockPos), 3);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor blockGetter, BlockPos blockPos, BlockPos blockPos2) {
        return getState(blockState,blockGetter,blockPos);
    }
}
