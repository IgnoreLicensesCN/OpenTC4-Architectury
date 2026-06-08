package thaumcraft.common.blocks.crafted.essentia.pipes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportConnectableBlockEntity;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

public class AbstractEssentiaTubeBlock
        extends SuppressedWarningBlock{
    public AbstractEssentiaTubeBlock(Properties properties) {
        super(properties);
    }
    public AbstractEssentiaTubeBlock() {
        this(Properties.of()
                .sound(SoundType.METAL)
                .strength(0.5F,5.F)
                .noOcclusion()
        );
    }
    public static final VoxelShape[] SHAPES_FOR_CONNECTED_DIRECTION = new VoxelShape[1<<Direction.values().length];
    static {
        VoxelShape CENTER_SHAPE = Block.box(6,5,5,10,11,11);
        VoxelShape[] ADDITIONAL_SHAPE_FOR_CONNECTED_DIRECTION = new VoxelShape[Direction.values().length];
        for (int side = 0; side < Direction.values().length; ++side) {
            int minX = 6;
            int minY = 5;
            int minZ = 5;
            int maxX = 10;
            int maxY = 11;
            int maxZ = 11;
            var pickDirection = Direction.values()[side];
            if (pickDirection.getStepX() < 0){
                minX = 0;
            }
            if (pickDirection.getStepY() < 0){
                minY = 0;
            }
            if (pickDirection.getStepZ() < 0){
                minZ = 0;
            }
            if (pickDirection.getStepX() > 0){
                maxX = 16;
            }
            if (pickDirection.getStepY() > 0){
                maxY = 16;
            }
            if (pickDirection.getStepZ() > 0){
                maxZ = 16;
            }
            ADDITIONAL_SHAPE_FOR_CONNECTED_DIRECTION[side] = Block.box(minX, minY, minZ, maxX, maxY, maxZ);
        }
        for (int i=0;i<SHAPES_FOR_CONNECTED_DIRECTION.length;i++) {
            var finalShape = CENTER_SHAPE;
            for (int dirIndex = 0;dirIndex<Direction.values().length;dirIndex++) {
                if (((i>>dirIndex) & 1) == 1){
                    finalShape = Shapes.or(finalShape, ADDITIONAL_SHAPE_FOR_CONNECTED_DIRECTION[dirIndex]);
                }
            }
            SHAPES_FOR_CONNECTED_DIRECTION[i] = finalShape.optimize();
        }
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter atLevel, BlockPos blockPos, CollisionContext collisionContext) {
        int voxelShapeIndex = 0;
        for (var dir:Direction.values()) {
            if (canDirectionConnect(atLevel,blockPos, dir)) {
                voxelShapeIndex += (1<<dir.ordinal());
            }
        }
        return SHAPES_FOR_CONNECTED_DIRECTION[voxelShapeIndex];
    }

    protected boolean canDirectionConnect(BlockGetter atLevel, BlockPos selfBlockPos, Direction dir) {
        var tryConnectToPos = selfBlockPos.relative(dir);
        boolean canConnectTo =
                atLevel.getBlockEntity(tryConnectToPos) instanceof
                        IEssentiaTransportConnectableBlockEntity connectable
                && connectable.isConnectable(dir.getOpposite());
        if (!canConnectTo) {
            return false;
        }
        return atLevel.getBlockEntity(selfBlockPos) instanceof
                IEssentiaTransportConnectableBlockEntity connectable
                && connectable.isConnectable(dir);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return this.getShape(blockState, blockGetter, blockPos, collisionContext);
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
