package thaumcraft.common.blocks.crafted.visnet;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.lib.UtilsFXMigrated;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.EnergizedAuraNodeBlockEntity;

import static thaumcraft.common.blocks.worldgenerated.AuraNodeBlock.NODE_SOUND;

public class EnergizedAuraNodeBlock extends Block implements EntityBlock {
    private static final VoxelShape SELECT_SHAPE =
            Block.box(0.3 * 16, 0.3 * 16, 0.3 * 16,
                    0.7 * 16, 0.7 * 16, 0.7 * 16);

    private static final VoxelShape COLLISION_SHAPE = Shapes.empty();
    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }
    @Override
    public VoxelShape getVisualShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext ctx
    ) {
        return Shapes.empty();
    }


    @Override
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return SELECT_SHAPE;
    }
    @Override
    public @NotNull VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext ctx
    ) {
        return COLLISION_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext ctx
    ) {
        return COLLISION_SHAPE;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return true;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide() && level.random.nextBoolean()) {
            UtilsFXMigrated.infusedStoneSparkle(level, blockPos.getX(),blockPos.getY(),blockPos.getZ(), 0);
        }
    }
    @Override
    public void onRemove(
            BlockState state,
            Level level,
            BlockPos pos,
            BlockState newState,
            boolean isMoving
    ) {
        super.onRemove(state, level, pos, newState, isMoving);
        if (level instanceof ClientLevel clientLevel && state.getBlock() != newState.getBlock()) {
            var x = pos.getX();
            var y = pos.getY();
            var z = pos.getZ();
            // 粒子
            ClientFXUtils.burst(clientLevel, (double)x + (double)0.5F, (double)y + (double)0.5F, (double)z + (double)0.5F, 1.0F);
        }
        if (level instanceof ServerLevel serverLevel && newState.isAir()) {
            explode(serverLevel,pos);
        }
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        //TODO:Check above and below,if not satisfied,boom
    }

    public void explode(Level world, BlockPos atPos) {
        if (Platform.getEnvironment() != Env.CLIENT) {
            world.setBlockAndUpdate(atPos, Blocks.AIR.defaultBlockState());
            var explodePos = atPos.getCenter();
            world.explode(null,
                    explodePos.x,
                    explodePos.y,
                    explodePos.z,
                    3.0F,                      // 威力
                    Level.ExplosionInteraction.BLOCK);

            for(int a = 0; a < 50; ++a) {
                var pickPos = atPos.offset(
                        world.getRandom().nextInt(15)-7,
                        world.getRandom().nextInt(15)-7,
                        world.getRandom().nextInt(15)-7
                );
                if (world.getBlockState(pickPos).isAir()) {
                    if (pickPos.getY() < atPos.getY()) {
                        world.setBlockAndUpdate(pickPos, ThaumcraftBlocks.FLUX_GOO.defaultBlockState());
                    }else {
                        world.setBlockAndUpdate(pickPos, ThaumcraftBlocks.FLUX_GAS.defaultBlockState());
                    }
                }
            }
        }
    }
    public EnergizedAuraNodeBlock(Properties properties) {
        super(properties);
    }
    public EnergizedAuraNodeBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(2.f,200.f)
                .lightLevel(state -> 8)
                .sound(NODE_SOUND)
                .noOcclusion()
                .noCollission()
                .pushReaction(PushReaction.BLOCK)
        );
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new EnergizedAuraNodeBlockEntity(blockPos,blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.ENERGIZED_NODE && blockState.getBlock() == this){
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof EnergizedAuraNodeBlockEntity energizedNode){
                    energizedNode.tick();
                }
            });
        }
        return null;
    }
}
