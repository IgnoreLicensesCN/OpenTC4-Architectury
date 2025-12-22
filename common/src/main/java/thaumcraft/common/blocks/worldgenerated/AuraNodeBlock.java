package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.ClientFXUtils;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.node.NodeBlockEntity;

public class AuraNodeBlock extends Block {
    private static final VoxelShape SELECT_SHAPE =
            Block.box(0.3 * 16, 0.3 * 16, 0.3 * 16,
                    0.7 * 16, 0.7 * 16, 0.7 * 16);

    private static final VoxelShape COLLISION_SHAPE = Shapes.empty();

    public static final SoundType NODE_SOUND = new SoundType(
            1.0F, // volume
            1.0F, // pitch
            ThaumcraftSounds.CRAFT_FAIL, // break
            SoundEvents.WOOL_STEP, // step
            SoundEvents.WOOL_PLACE, // place
            SoundEvents.WOOL_HIT, // hit
            SoundEvents.WOOL_FALL  // fall
    );

    public AuraNodeBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(2.f)
                .explosionResistance(200.f)
                .lightLevel(state -> 8)
                .sound(NODE_SOUND)
                .randomTicks()
                .noOcclusion()
                .noCollission()
                .pushReaction(PushReaction.BLOCK)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide() && level.random.nextBoolean()) {
            UtilsFX.infusedStoneSparkle(level, blockPos.getX(),blockPos.getY(),blockPos.getZ(), 0);
        }
//        super.spawnDestroyParticles(level, player, blockPos, blockState);
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
        if (level instanceof ServerLevel serverLevel) {
            //TODO:wispEssences
        }
    }


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
        return COLLISION_SHAPE;
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
//    @Override
//    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
//        var bEntity = serverLevel.getBlockEntity(blockPos);
//        if (bEntity instanceof NodeBlockEntity node){
//            node.serverRandomTickByBlockHandle();
//        }
//    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        var bEntity = level.getBlockEntity(blockPos);
        if (bEntity instanceof NodeBlockEntity node){
            node.clientTickByBlockHandle();
        }
    }
}
