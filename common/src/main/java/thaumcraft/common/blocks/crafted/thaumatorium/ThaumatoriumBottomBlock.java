package thaumcraft.common.blocks.crafted.thaumatorium;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.ThaumatoriumBlockEntity;

import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;

public class ThaumatoriumBottomBlock extends SuppressedWarningBlock implements EntityBlock {
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
        return ThaumcraftBlocks.THAUMATORIUM_TOP;
    }
    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos selfPos, RandomSource randomSource) {
        super.tick(blockState, level, selfPos, randomSource);
        if (level.getBlockState(selfPos.relative(getCheckDirection())).getBlock() != this.getCheckBlock()){
            level.setBlockAndUpdate(selfPos,ThaumcraftBlocks.ALCHEMICAL_CONSTRUCT.defaultBlockState());
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
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (level.getBlockEntity(blockPos) instanceof ThaumatoriumBlockEntity thaumatorium) {
            Containers.dropContents(level, blockPos, thaumatorium);
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof ThaumatoriumBlockEntity thaumatorium && player instanceof ServerPlayer serverPlayer) {
                openExtendedMenu(serverPlayer,thaumatorium);
            }
            return InteractionResult.CONSUME;
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
        if (!level.isClientSide && blockEntityType == ThaumcraftBlockEntities.THAUMATORIUM) {
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof ThaumatoriumBlockEntity thaumatoriumBlockEntity) {
                    thaumatoriumBlockEntity.serverTick();
                }
            });
        }
        return null;
    }
}
