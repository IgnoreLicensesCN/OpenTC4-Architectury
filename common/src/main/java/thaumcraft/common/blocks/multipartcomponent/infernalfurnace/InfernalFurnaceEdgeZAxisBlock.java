package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class InfernalFurnaceEdgeZAxisBlock extends AbstractInfernalFurnaceComponent {

    public static final IntegerProperty EDGE_TYPE_Z_AXIS = IntegerProperty.create("edge_type_z_axis", 0, 3);
    public static final int EDGE_TYPE_0_0_1 = 0;
    public static final int EDGE_TYPE_0_2_1 = 1;
    public static final int EDGE_TYPE_2_0_1 = 2;
    public static final int EDGE_TYPE_2_2_1 = 3;

    public static final BlockPos CENTER_POS_RELATED_FROM_0_0_1 = new BlockPos(1,1,0);
    public static final BlockPos CENTER_POS_RELATED_FROM_0_2_1 = new BlockPos(1,-1,0);
    public static final BlockPos CENTER_POS_RELATED_FROM_2_0_1 = new BlockPos(-1,1,0);
    public static final BlockPos CENTER_POS_RELATED_FROM_2_2_1 = new BlockPos(-1,-1,0);

    public InfernalFurnaceEdgeZAxisBlock(Properties properties) {
        super(properties);
    }
    public InfernalFurnaceEdgeZAxisBlock() {
        super(Properties
                .copy(Blocks.OBSIDIAN)
                .strength(10.0f,500.f)
                .lightLevel(s -> 1)
        );
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(EDGE_TYPE_Z_AXIS, EDGE_TYPE_0_0_1).setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_0);
    }

    @Override
    public BlockPos findMultipartCheckerPosRelatedToSelf(Level level, BlockState state, BlockPos pos) {
        int edge = state.getValue(EDGE_TYPE_Z_AXIS);
        if (edge == EDGE_TYPE_0_0_1) {
            return CENTER_POS_RELATED_FROM_0_0_1;
        }
        if (edge == EDGE_TYPE_0_2_1) {
            return CENTER_POS_RELATED_FROM_0_2_1;
        }
        if (edge == EDGE_TYPE_2_0_1) {
            return CENTER_POS_RELATED_FROM_2_0_1;
        }
        if (edge == EDGE_TYPE_2_2_1) {
            return CENTER_POS_RELATED_FROM_2_2_1;
        }

        throw new IllegalStateException("Unexpected edge type: " + edge);
    }

    public static final BlockPos SELF_POS_0_0_1 = new BlockPos(0,0,1);
    public static final BlockPos SELF_POS_0_2_1 = new BlockPos(0,2,1);
    public static final BlockPos SELF_POS_2_0_1 = new BlockPos(2,0,1);
    public static final BlockPos SELF_POS_2_2_1 = new BlockPos(2,2,1);
    @Override
    public BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        int edge = state.getValue(EDGE_TYPE_Z_AXIS);
        if (edge == EDGE_TYPE_0_0_1) {
            return SELF_POS_0_0_1;
        }
        if (edge == EDGE_TYPE_0_2_1) {
            return SELF_POS_0_2_1;
        }
        if (edge == EDGE_TYPE_2_0_1) {
            return SELF_POS_2_0_1;
        }
        if (edge == EDGE_TYPE_2_2_1) {
            return SELF_POS_2_2_1;
        }
        throw new IllegalStateException("Unexpected edge type: " + edge);
    }


    @Override
    public void recoverToOriginalBlock(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
        }
    }
}
