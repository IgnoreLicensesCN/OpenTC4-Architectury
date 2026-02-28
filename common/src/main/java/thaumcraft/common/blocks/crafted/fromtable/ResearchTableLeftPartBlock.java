package thaumcraft.common.blocks.crafted.fromtable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.crafted.ResearchTableBlockEntity;

import java.util.Objects;

import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;

//
//        ↑N
//     ←W    E→
//        ↓S
//LeftPart(facing:E→,with BE  and real #use) RightPart(facing:←W)
//
public class ResearchTableLeftPartBlock extends SuppressedWarningBlock implements EntityBlock {
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

    public ResearchTableLeftPartBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public ResearchTableLeftPartBlock() {
        super(Properties.copy(Blocks.CRAFTING_TABLE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResearchTableBlockEntity(blockPos,blockState);
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (level.getBlockEntity(blockPos) instanceof ResearchTableBlockEntity researchTableBlockEntity) {
                openExtendedMenu(serverPlayer,researchTableBlockEntity);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!(level != null && !level.isClientSide)) return;
        if (!blockState.is(blockState2.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof Container container) {
                Containers.dropContents(level, blockPos, container);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    public BlockPos getRightPartPos(Direction thisFacing,BlockPos thisPos) {
        return thisPos.relative(thisFacing);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, level, blockPos, randomSource);
        var facing = blockState.getValue(FACING);
        var probablyRightPartBlockState = level.getBlockState(getRightPartPos(facing,blockPos));
        if (!probablyRightPartBlockState.is(ThaumcraftBlocks.RESEARCH_TABLE_RIGHT_PART)
                || !Objects.equals(probablyRightPartBlockState.getValue(ResearchTableRightPartBlock.FACING)
                .getOpposite(),facing)
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
