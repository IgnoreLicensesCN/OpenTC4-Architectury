package thaumcraft.common.blocks.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.multiparts.matchers.AbstractMultipartMatcher;

public abstract class AbstractMultipartComponentBlock extends Block implements IMultipartComponentBlock {
    public static final IntegerProperty ROTATION_X_AXIS = IntegerProperty.create("rotation_x_axis", 0, 3);
    public static final IntegerProperty ROTATION_Y_AXIS = IntegerProperty.create("rotation_y_axis", 0, 3);
    public static final IntegerProperty ROTATION_Z_AXIS = IntegerProperty.create("rotation_z_axis", 0, 3);

    public static final int ROTATION_DEGREE_0 = 0;
    public static final int ROTATION_DEGREE_90 = 90;
    public static final int ROTATION_DEGREE_180 = 180;
    public static final int ROTATION_DEGREE_270 = 270;

    public AbstractMultipartComponentBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        var checkerPos = findMultipartCheckerPosRelatedToSelf(serverLevel, blockState, blockPos).offset(blockPos);
        var state = serverLevel.getBlockState(checkerPos);
        this.chechMultipart(serverLevel, state, checkerPos);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean bl) {
        var checkerPos = findMultipartCheckerPosRelatedToSelf(level, blockState, pos).offset(pos);
        level.scheduleTick(checkerPos, level.getBlockState(checkerPos).getBlock(), 1);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState blockState2, boolean bl) {
        var checkerPos = findMultipartCheckerPosRelatedToSelf(level, blockState, pos).offset(pos);
        level.scheduleTick(checkerPos, level.getBlockState(checkerPos).getBlock(), 1);
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState();
    }
}
