package thaumcraft.common.blocks.crafted.essentia;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaCrystallizerBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class EssentiaCrystallizerBlock
        extends SuppressedWarningBlock
        implements EntityBlock {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
//    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public EssentiaCrystallizerBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(POWERED, Boolean.FALSE));
//        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN));
    }
    public EssentiaCrystallizerBlock() {
        this(Properties.of()
                .sound(SoundType.METAL)
                .strength(0.5F,5.F)
                .noOcclusion()
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
        if (!level.isClientSide()) {
            var poweredBefore = blockState.getValue(POWERED);
            var poweredAfter = level.hasNeighborSignal(blockPos);
            if (poweredBefore != poweredAfter) {
                if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof EssentiaCrystallizerBlockEntity crystallizer){
                    crystallizer.setBlockStateAndUpdate(blockState.setValue(POWERED,poweredAfter));
                }
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return !(LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof IValueContainerBasedComparatorSignalProviderBlockEntity signalProvider)
                ? 0 : signalProvider.getComparatorSignal();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaCrystallizerBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide()) {
            return (level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof EssentiaCrystallizerBlockEntity crystallizer) {
                    crystallizer.serverTick();
                }
            };
        }
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof EssentiaCrystallizerBlockEntity crystallizer) {
                crystallizer.clientTick();
            }
        };
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j) {
        if (level.isClientSide() && i == 0 && j == 0) {
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, blockPos) instanceof EssentiaCrystallizerBlockEntity crystallizer) {
                EssentiaCrystallizerBlockEntity.ClientTickContext.doVenting(crystallizer);
            }
        }
        return super.triggerEvent(blockState, level, blockPos, i, j);
    }
}
