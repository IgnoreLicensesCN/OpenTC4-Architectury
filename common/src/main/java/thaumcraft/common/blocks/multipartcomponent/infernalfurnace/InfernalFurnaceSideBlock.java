package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class InfernalFurnaceSideBlock extends AbstractInfernalFurnaceComponent {
    public static final IntegerProperty SIDE_TYPE = IntegerProperty.create("side", 0, 3);
    public static final int SIDE_TYPE_1_1_0 = 0;
    public static final int SIDE_TYPE_1_1_2 = 1;
    public static final int SIDE_TYPE_0_1_1 = 2;
    public static final BlockPos CENTER_POS_RELATED_FROM_1_1_0 = new BlockPos(0,0,1);
    public static final BlockPos CENTER_POS_RELATED_FROM_1_1_2 = new BlockPos(0,0,-1);
    public static final BlockPos CENTER_POS_RELATED_FROM_0_1_1 = new BlockPos(1,0,0);
    public static final BlockPos SELF_POS_1_1_0 = new BlockPos(1,1,0);
    public static final BlockPos SELF_POS_1_1_2 = new BlockPos(1,1,2);
    public static final BlockPos SELF_POS_0_1_1 = new BlockPos(0,1,1);

    public InfernalFurnaceSideBlock(Properties properties) {
        super(properties);
    }
    public InfernalFurnaceSideBlock() {
        super(Properties
                .copy(Blocks.STONE)
                .strength(10.0f,500.f)
                .lightLevel(s -> 1)
        );
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(SIDE_TYPE, SIDE_TYPE_1_1_0).setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_0);
    }

    @Override
    public BlockPos findMultipartCheckerPosRelatedToSelf(Level level, BlockState state, BlockPos pos) {
        int side = state.getValue(SIDE_TYPE);
        if (side == SIDE_TYPE_1_1_0) {
            return CENTER_POS_RELATED_FROM_1_1_0;
        }
        if (side == SIDE_TYPE_1_1_2) {
            return CENTER_POS_RELATED_FROM_1_1_2;
        }
        if (side == SIDE_TYPE_0_1_1) {
            return CENTER_POS_RELATED_FROM_0_1_1;
        }

        throw new IllegalStateException("Unexpected side type: " + side);
    }

    @Override
    public BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
        int side = state.getValue(SIDE_TYPE);
        if (side == SIDE_TYPE_1_1_0) {
            return SELF_POS_1_1_0;
        }
        if (side == SIDE_TYPE_1_1_2) {
            return SELF_POS_1_1_2;
        }
        if (side == SIDE_TYPE_0_1_1) {
            return SELF_POS_0_1_1;
        }
        throw new IllegalStateException("Unexpected corner type: " + side);
    }

    @Override
    public void recoverToOriginalBlock(Level level, BlockState state, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            serverLevel.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof ItemEntity itemEntity){


            return;
        }
        entity.lavaHurt();//wtf
    }
}
