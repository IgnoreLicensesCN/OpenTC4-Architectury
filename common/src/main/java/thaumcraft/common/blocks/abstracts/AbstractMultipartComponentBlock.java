package thaumcraft.common.blocks.abstracts;

import com.linearity.opentc4.VecTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMultipartComponentBlock extends Block implements IMultipartComponentBlock {
    public static final IntegerProperty ROTATION_X_AXIS = IntegerProperty.create("rotation_x_axis", 0, 3);
    public static final IntegerProperty ROTATION_Y_AXIS = IntegerProperty.create("rotation_y_axis", 0, 3);
    public static final IntegerProperty ROTATION_Z_AXIS = IntegerProperty.create("rotation_z_axis", 0, 3);
    public static final IntegerProperty ROTATION_XZ_AXIS = IntegerProperty.create("rotation_xz_axis", 0, 6);

    public static final int ROTATION_DEGREE_0 = 0;
    public static final int ROTATION_DEGREE_90 = 1;
    public static final int ROTATION_DEGREE_180 = 2;
    public static final int ROTATION_DEGREE_270 = 3;

    //use Rotation3D#ordinal() for this state
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, VecTransformations.Rotation3D.values().length-1);

    public AbstractMultipartComponentBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        var state = serverLevel.getBlockState(blockPos);
        this.checkMultipart(serverLevel, state, blockPos);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean bl) {
        level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 1);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState blockState2, boolean bl) {
        level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 1);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState();
    }

    public abstract VecTransformations.Rotation3D getRotation(BlockState state);
}
