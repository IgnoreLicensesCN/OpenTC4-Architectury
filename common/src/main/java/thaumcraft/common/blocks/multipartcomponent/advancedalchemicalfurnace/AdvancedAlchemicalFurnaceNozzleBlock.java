package thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.IValueContainerBasedComparatorSignalProviderBlockEntity;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.tiles.crafted.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceNozzleBlockEntity;

import static thaumcraft.common.blocks.multipartcomponent.advancedalchemicalfurnace.AdvancedAlchemicalFurnaceBaseBlock.SELF_POS_1_0_1;

public class AdvancedAlchemicalFurnaceNozzleBlock extends AbstractAdvancedAlchemicalFurnaceComponent implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AdvancedAlchemicalFurnaceNozzleBlock(){
        super();
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH)
        );
    }

    public static final BlockPos POS_N = SELF_POS_1_0_1.relative(Direction.NORTH);
    public static final BlockPos POS_E = SELF_POS_1_0_1.relative(Direction.EAST);
    public static final BlockPos POS_W = SELF_POS_1_0_1.relative(Direction.WEST);
    public static final BlockPos POS_S = SELF_POS_1_0_1.relative(Direction.SOUTH);
    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        var dir = state.getValue(FACING);
        return switch (dir){
            case NORTH -> POS_N;
            case EAST -> POS_E;
            case WEST -> POS_W;
            case SOUTH -> POS_S;
            default -> throw new IllegalStateException("Direction Axis is Y:" + level + " " + state + " " + pos + " " + dir);
        };
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, ThaumcraftBlocks.ADVANCED_ALCHEMICAL_CONSTRUCT.defaultBlockState(), 3);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState arg) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return !(level.getBlockEntity(pos) instanceof IValueContainerBasedComparatorSignalProviderBlockEntity signalProvider)
                ? 0 : signalProvider.getComparatorSignal();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new AdvancedAlchemicalFurnaceNozzleBlockEntity(blockPos, blockState);
        }
        return null;
    }
}
