package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

//mainly stole from MC source(decompiled and deobf)
public class ItemCrateBlock extends SuppressedWarningBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final VoxelShape TOP_AABB = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);//trapdoor
    public ItemCrateBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(OPEN, false)
                .setValue(WATERLOGGED, false)
                .setValue(POWERED, false)
        );
    }
    public ItemCrateBlock(){
        this(Properties.of()
                .sound(SoundType.METAL)
                .strength(3,17)
        );
    }



    @Override
    public @NotNull InteractionResult use(BlockState arg, Level arg2, BlockPos arg3, Player arg4, InteractionHand arg5, BlockHitResult arg6) {
        arg = arg.cycle(OPEN);
        arg2.setBlock(arg3, arg, 2);
        if (arg.getValue(WATERLOGGED)) {
            arg2.scheduleTick(arg3, Fluids.WATER, Fluids.WATER.getTickDelay(arg2));
        }

        this.playSound(arg4, arg2, arg3, arg.getValue(OPEN));
        return InteractionResult.sidedSuccess(arg2.isClientSide);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        BlockState blockstate = this.defaultBlockState();
        FluidState fluidstate = arg.getLevel().getFluidState(arg.getClickedPos());
        if (arg.getLevel().hasNeighborSignal(arg.getClickedPos())) {
            blockstate = blockstate.setValue(OPEN, true).setValue(POWERED, true);
        }

        return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN,WATERLOGGED,POWERED);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState arg) {
        return arg.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(arg);
    }
    @Override
    public @NotNull BlockState updateShape(BlockState arg, Direction arg2, BlockState arg3, LevelAccessor arg4, BlockPos arg5, BlockPos arg6) {
        if (arg.getValue(WATERLOGGED)) {
            arg4.scheduleTick(arg5, Fluids.WATER, Fluids.WATER.getTickDelay(arg4));
        }

        return super.updateShape(arg, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (collisionContext instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof ItemEntity){
            return Shapes.empty();
        }
        return TOP_AABB;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (collisionContext instanceof EntityCollisionContext entityCollisionContext && entityCollisionContext.getEntity() instanceof ItemEntity){
            return Shapes.empty();
        }
        return TOP_AABB;
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return TOP_AABB;
    }

    protected void playSound(@Nullable Player arg, Level arg2, BlockPos arg3, boolean bl) {
        arg2.playSound(arg, arg3, bl ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE,
                SoundSource.BLOCKS, 1.0F, arg2.getRandom().nextFloat() * 0.1F + 0.9F);
        arg2.gameEvent(arg, bl ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, arg3);
    }
    @Override
    public void neighborChanged(BlockState selfState, Level level, BlockPos arg3, Block arg4, BlockPos arg5, boolean bl) {
        if (!level.isClientSide) {
            boolean receivedSignal = level.hasNeighborSignal(arg3);
            if (receivedSignal != selfState.getValue(POWERED)) {
                if (selfState.getValue(OPEN) != receivedSignal) {
                    selfState = selfState.setValue(OPEN, receivedSignal);
                    this.playSound(null, level, arg3, receivedSignal);
                }

                level.setBlock(arg3, selfState.setValue(POWERED, receivedSignal), 2);
                if (selfState.getValue(WATERLOGGED)) {
                    level.scheduleTick(arg3, Fluids.WATER, Fluids.WATER.getTickDelay(level));
                }
            }
        }
    }
}
