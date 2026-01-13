package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class InfernalFurnaceCornerBlock extends AbstractInfernalFurnaceComponent {
    public static final int CORNER_0_0_0 = 0;
    public static final int CORNER_0_0_2 = 1;
    public static final int CORNER_0_2_0 = 2;
    public static final int CORNER_0_2_2 = 3;
    public static final int CORNER_2_0_0 = 4;
    public static final int CORNER_2_0_2 = 5;
    public static final int CORNER_2_2_0 = 6;
    public static final int CORNER_2_2_2 = 7;
    public static final BlockPos SELF_POS_0_0_0 = new BlockPos(0,0,0);
    public static final BlockPos SELF_POS_0_0_2 = new BlockPos(0,0,2);
    public static final BlockPos SELF_POS_0_2_0 = new BlockPos(0,2,0);
    public static final BlockPos SELF_POS_0_2_2 = new BlockPos(0,2,2);
    public static final BlockPos SELF_POS_2_0_0 = new BlockPos(2,0,0);
    public static final BlockPos SELF_POS_2_0_2 = new BlockPos(2,0,2);
    public static final BlockPos SELF_POS_2_2_0 = new BlockPos(2,2,0);
    public static final BlockPos SELF_POS_2_2_2 = new BlockPos(2,2,2);

    public static final IntegerProperty CORNER_TYPE = IntegerProperty.create("corner", 0, 7);
    public InfernalFurnaceCornerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(CORNER_TYPE, 0)
                        .setValue(ROTATION_Y_AXIS, 0)
        );
    }
    public InfernalFurnaceCornerBlock() {
        super(BlockBehaviour.Properties
                .copy(Blocks.STONE)
                .strength(10.0f,500.f)
                .lightLevel(s -> 1)
                .requiresCorrectToolForDrops()
        );
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(CORNER_TYPE, 0)
                        .setValue(ROTATION_Y_AXIS, 0)
        );
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(CORNER_TYPE, CORNER_2_2_0);
    }

    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        int corner = state.getValue(CORNER_TYPE);
        if (corner == CORNER_0_0_0) {
            return SELF_POS_0_0_0;
        }
        if (corner == CORNER_0_0_2) {
            return SELF_POS_0_0_2;
        }
        if (corner == CORNER_0_2_0) {
            return SELF_POS_0_2_0;
        }
        if (corner == CORNER_0_2_2) {
            return SELF_POS_0_2_2;
        }
        if (corner == CORNER_2_0_0) {
            return SELF_POS_2_0_0;
        }
        if (corner == CORNER_2_0_2) {
            return SELF_POS_2_0_2;
        }
        if (corner == CORNER_2_2_0) {
            return SELF_POS_2_2_0;
        }
        if (corner == CORNER_2_2_2) {
            return SELF_POS_2_2_2;
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
        builder.add(CORNER_TYPE);
    }
}
