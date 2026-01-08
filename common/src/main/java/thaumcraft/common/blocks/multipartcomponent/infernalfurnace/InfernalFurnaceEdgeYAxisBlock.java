package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class InfernalFurnaceEdgeYAxisBlock extends AbstractInfernalFurnaceComponent {

    public static final IntegerProperty EDGE_TYPE_Y_AXIS = IntegerProperty.create("edge_type_y_axis", 0, 3);
    public static final int EDGE_TYPE_0_1_0 = 0;
    public static final int EDGE_TYPE_0_1_2 = 1;
    public static final int EDGE_TYPE_2_1_0 = 2;
    public static final int EDGE_TYPE_2_1_2 = 3;

    public InfernalFurnaceEdgeYAxisBlock(Properties properties) {
        super(properties);
    }
    public InfernalFurnaceEdgeYAxisBlock() {
        super(BlockBehaviour.Properties
                .copy(Blocks.STONE)
                .strength(10.0f,500.f)
                .lightLevel(s -> 1)
        );
    }

    public static final BlockPos SELF_POS_0_1_0 = new BlockPos(0,1,0);
    public static final BlockPos SELF_POS_0_1_2 = new BlockPos(0,1,2);
    public static final BlockPos SELF_POS_2_1_0 = new BlockPos(2,1,0);
    public static final BlockPos SELF_POS_2_1_2 = new BlockPos(2,1,2);
    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        int corner = state.getValue(EDGE_TYPE_Y_AXIS);
        if (corner == EDGE_TYPE_0_1_0) {
            return SELF_POS_0_1_0;
        }
        if (corner == EDGE_TYPE_0_1_2) {
            return SELF_POS_0_1_2;
        }
        if (corner == EDGE_TYPE_2_1_0) {
            return SELF_POS_2_1_0;
        }
        if (corner == EDGE_TYPE_2_1_2) {
            return SELF_POS_2_1_2;
        }
        throw new IllegalStateException("Unexpected corner type: " + corner);
    }

    @Override
    public void onMultipartDestroyed(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, Blocks.NETHER_BRICKS.defaultBlockState(), 3);
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EDGE_TYPE_Y_AXIS);
    }
}
