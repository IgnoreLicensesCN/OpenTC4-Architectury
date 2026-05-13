package thaumcraft.common.blocks.crafted.arcanebore;

import com.linearity.opentc4.mixinaccessors.ArcaneBoreBlockEntityClientAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.blockapi.IRedstoneWireConnectableBlock;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.AbstractExtendedMenuProviderContainerBlock;
import thaumcraft.common.tiles.crafted.ArcaneBoreBlockEntity;

import static thaumcraft.common.blocks.crafted.arcanebore.ArcaneBoreDrillBlock.DRILL_FACING;
import static thaumcraft.common.blocks.crafted.arcanebore.ArcaneBoreDrillBlock.FACING_TO_BASE;


public class ArcaneBoreBaseBlock extends AbstractExtendedMenuProviderContainerBlock
        implements IRedstoneWireConnectableBlock
{
    public static final DirectionProperty FACING_TO_DRILL = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty OUTPUT_FACING = DirectionProperty.create(
            "output_facing",Direction.values()
    );

    public ArcaneBoreBaseBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING_TO_DRILL, Direction.UP)
                        .setValue(POWERED, false)
                        .setValue(OUTPUT_FACING, Direction.NORTH)
        );
    }

    public ArcaneBoreBaseBlock() {
        this(
                Properties.of()
                        .sound(SoundType.WOOD)
                        .strength(2.5F, 10.0F)
                        .noOcclusion()
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING_TO_DRILL, POWERED, OUTPUT_FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var chosenFacing = blockPlaceContext.getClickedFace()
                .getOpposite();
        var level = blockPlaceContext.getLevel();
        var clickedPos = blockPlaceContext.getClickedPos();
        if (!level
                .getBlockState(
                        clickedPos.relative(chosenFacing)
                )
                .isAir()
        ) {
            return null;
        }
        return defaultBlockState()
                .setValue(FACING_TO_DRILL, chosenFacing)
                .setValue(POWERED, level.hasNeighborSignal(clickedPos))
                .setValue(OUTPUT_FACING, chosenFacing.getOpposite());
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (!level.isClientSide()) {

            var selfFacing = state.getValue(FACING_TO_DRILL);
            if (fromPos.equals(pos.relative(selfFacing))) {
                var probablyDrillState = level.getBlockState(fromPos);
                if (probablyDrillState.getBlock() != ThaumcraftBlocks.ARCANE_BORE_DRILL) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    return;
                } else if (probablyDrillState.getValue(FACING_TO_BASE)
                        .getOpposite() != selfFacing) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    return;
                }
            }
            boolean receivedSignal = level.hasNeighborSignal(pos);
            if (receivedSignal != state.getValue(POWERED)) {
                var newState = state.setValue(POWERED, receivedSignal);
                if (level.getBlockEntity(pos) instanceof ArcaneBoreBlockEntity arcaneBore) {
                    arcaneBore.setBlockStateAndUpdate(newState);
                }
            }
        } else {
            var selfFacing = state.getValue(FACING_TO_DRILL);
            if (fromPos.equals(pos.relative(selfFacing))) {
                var probablyDrillState = level.getBlockState(fromPos);
                if (probablyDrillState.getBlock() == ThaumcraftBlocks.ARCANE_BORE_DRILL
                        && probablyDrillState.getValue(FACING_TO_BASE)
                        .getOpposite() == selfFacing
                ) {
                    if (level.getBlockEntity(pos) instanceof ArcaneBoreBlockEntity arcaneBore) {
                        ((ArcaneBoreBlockEntityClientAccessor) arcaneBore).opentc4$getClientTickContext()
                                .updateRotationForDigDirection(probablyDrillState.getValue(DRILL_FACING));
                    }
                }
            }
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArcaneBoreBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return ((level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof ArcaneBoreBlockEntity arcaneBore) {
                    arcaneBore.serverTick();
                }
            });
        }
        return ((level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof ArcaneBoreBlockEntity arcaneBore) {
                arcaneBore.clientTick();
            }
        });
    }

}
