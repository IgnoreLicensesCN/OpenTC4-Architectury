package thaumcraft.common.blocks.crafted.essentia;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.essentiabe.EssentiaCentrifugeBlockEntity;

public class EssentiaCentrifugeBlock
        extends SuppressedWarningBlock
        implements EntityBlock {
    public EssentiaCentrifugeBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    public EssentiaCentrifugeBlock() {
        this(
                Properties.of()
                    .sound(SoundType.METAL)
                    .strength(0.5F,5.F)
                    .noOcclusion()
        );
    }
    public static final VoxelShape SHAPE = Block.box(4, 0.0D, 4, 12, 16.0D, 12);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaCentrifugeBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()){
            return (level1, blockPos, blockState1, blockEntity) -> {
                if (blockEntity instanceof EssentiaCentrifugeBlockEntity centrifuge){
                    centrifuge.clientTick();
                }
            };
        }
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof EssentiaCentrifugeBlockEntity centrifuge){
                centrifuge.serverTick();
            }
        };
    }


    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return !(level.getBlockEntity(pos) instanceof IValueContainerBasedComparatorSignalProviderBlockEntity signalProvider)
                ? 0 : signalProvider.getComparatorSignal();
    }
}
