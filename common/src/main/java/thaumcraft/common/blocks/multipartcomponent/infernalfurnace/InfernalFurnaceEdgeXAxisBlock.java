package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class InfernalFurnaceEdgeXAxisBlock extends AbstractInfernalFurnaceComponent {

    public static final IntegerProperty EDGE_TYPE_X_AXIS = IntegerProperty.create("edge_type_x_axis", 0, 3);
    public static final int EDGE_TYPE_1_0_0 = 0;
    public static final int EDGE_TYPE_1_0_2 = 1;
    public static final int EDGE_TYPE_1_2_0 = 2;
    public static final int EDGE_TYPE_1_2_2 = 3;

    public InfernalFurnaceEdgeXAxisBlock(Properties properties) {
        super(properties);
    }
    public InfernalFurnaceEdgeXAxisBlock() {
        super(Properties
                .copy(Blocks.STONE)
                .strength(10.0f,500.f)
                .lightLevel(s -> 1)
        );
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(EDGE_TYPE_X_AXIS, EDGE_TYPE_1_0_0).setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_0);
    }


    public static final BlockPos SELF_POS_1_0_0 = new BlockPos(1,0,0);
    public static final BlockPos SELF_POS_1_0_2 = new BlockPos(1,0,2);
    public static final BlockPos SELF_POS_1_2_0 = new BlockPos(1,2,0);
    public static final BlockPos SELF_POS_1_2_2 = new BlockPos(1,2,2);
    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        int edge = state.getValue(EDGE_TYPE_X_AXIS);
        if (edge == EDGE_TYPE_1_0_0) {
            return SELF_POS_1_0_0;
        }
        if (edge == EDGE_TYPE_1_0_2) {
            return SELF_POS_1_0_2;
        }
        if (edge == EDGE_TYPE_1_2_0) {
            return SELF_POS_1_2_0;
        }
        if (edge == EDGE_TYPE_1_2_2) {
            return SELF_POS_1_2_2;
        }
        throw new IllegalStateException("Unexpected edge type: " + edge);
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EDGE_TYPE_X_AXIS);
    }
}
