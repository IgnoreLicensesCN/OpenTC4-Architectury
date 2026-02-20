package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//guys i really have mental illness(anxiety disorder and depression due to fucking CN(rev.) education)
//so @SuppressWarnings("deprecation") here
public class SuppressedWarningBlock extends Block {
    public SuppressedWarningBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return super.getShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return super.getOcclusionShape(blockState, blockGetter, blockPos);
    }


    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return super.getInteractionShape(blockState, blockGetter, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return super.isCollisionShapeFullBlock(blockState, blockGetter, blockPos);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState blockState) {
        return super.getRenderShape(blockState);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return super.getVisualShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return super.getBlockSupportShape(blockState, blockGetter, blockPos);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOcclusionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return super.isOcclusionShapeFullBlock(blockState, blockGetter, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return super.useShapeForLightOcclusion(blockState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void updateIndirectNeighbourShapes(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, int i, int j) {
        super.updateIndirectNeighbourShapes(blockState, levelAccessor, blockPos, i, j);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return super.canSurvive(blockState, levelReader, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return super.rotate(blockState, rotation);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        super.entityInside(blockState, level, blockPos, entity);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return super.isPathfindable(blockState, blockGetter, blockPos, pathComputationType);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSignalSource(BlockState blockState) {
        return super.isSignalSource(blockState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return super.mirror(blockState, mirror);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return super.canBeReplaced(blockState, blockPlaceContext);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return super.canBeReplaced(blockState, fluid);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        return super.getDrops(blockState, builder);
    }

    @Override
    @SuppressWarnings("deprecation")
    public long getSeed(BlockState blockState, BlockPos blockPos) {
        return super.getSeed(blockState, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return super.getLightBlock(blockState, blockGetter, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        return super.getMenuProvider(blockState, level, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return super.getShadeBrightness(blockState, blockGetter, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return super.getAnalogOutputSignal(blockState, level, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getDestroyProgress(BlockState blockState, Player player, BlockGetter blockGetter, BlockPos blockPos) {
        return super.getDestroyProgress(blockState, player, blockGetter, blockPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void attack(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        super.attack(blockState, level, blockPos, player);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return super.getSignal(blockState, blockGetter, blockPos, direction);
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return super.getDirectSignal(blockState, blockGetter, blockPos, direction);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
        super.onProjectileHit(level, blockState, blockHitResult, projectile);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return super.hasAnalogOutputSignal(blockState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(BlockState blockState) {
        return super.getFluidState(blockState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        return super.triggerEvent(blockState, level, blockPos, i, j);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onPlace(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean skipRendering(BlockState blockState, BlockState blockState2, Direction direction) {
        return super.skipRendering(blockState, blockState2, direction);
    }
}
