package thaumcraft.common.blocks.multipartcomponent.infernalfurnace;

import com.linearity.opentc4.VecTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.infernalfurnace.InfernalFurnaceNozzleBlockEntity;

import static com.linearity.opentc4.VecTransformations.transformRelatedDirection;
import static thaumcraft.common.tiles.ThaumcraftBlockEntities.INFERNAL_FURNACE_NOZZLE;

public class InfernalFurnaceSideBlock extends AbstractInfernalFurnaceComponent implements EntityBlock {
    public static final IntegerProperty SIDE_TYPE = IntegerProperty.create("side", 0, 3);
    public static final int SIDE_TYPE_1_1_0 = 0;
    public static final int SIDE_TYPE_1_1_2 = 1;
    public static final int SIDE_TYPE_0_1_1 = 2;
    public static final int SIDE_TYPE_1_0_1 = 3;
    public static final BlockPos SELF_POS_1_1_0 = new BlockPos(1,1,0);//N
    public static final BlockPos SELF_POS_1_1_2 = new BlockPos(1,1,2);//S
    public static final BlockPos SELF_POS_0_1_1 = new BlockPos(0,1,1);//W
    public static final BlockPos SELF_POS_1_0_1 = new BlockPos(1,0,1);//bottom

    public InfernalFurnaceSideBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(SIDE_TYPE, 0)
                        .setValue(ROTATION_Y_AXIS, 0)
        );
    }
    public InfernalFurnaceSideBlock() {
        this(Properties
                .copy(Blocks.STONE)
                .strength(10.0f,500.f)
                .lightLevel(s -> 1)
                .requiresCorrectToolForDrops()
        );
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext).setValue(SIDE_TYPE, SIDE_TYPE_1_1_0).setValue(ROTATION_Y_AXIS,ROTATION_DEGREE_0);
    }

    public Direction getFacingFromState(Level level,BlockState state,BlockPos pos) {
        int side = state.getValue(SIDE_TYPE);
        if (side == SIDE_TYPE_1_1_0){
            return transformRelatedDirection(Direction.NORTH,findTransformBasePosRelatedInMultipart(level,state,pos),
                    getRotation(state),
                    VecTransformations.Mirror3D.NONE
            );
        }
        if (side == SIDE_TYPE_1_1_2){
            return transformRelatedDirection(Direction.SOUTH,findTransformBasePosRelatedInMultipart(level,state,pos),
                    getRotation(state),
                    VecTransformations.Mirror3D.NONE
            );
        }
        if (side == SIDE_TYPE_0_1_1){
            return transformRelatedDirection(Direction.WEST,findTransformBasePosRelatedInMultipart(level,state,pos),
                    getRotation(state),
                    VecTransformations.Mirror3D.NONE
            );
        }
        if (side == SIDE_TYPE_1_0_1){
            return Direction.DOWN;
        }
        throw new IllegalArgumentException("Unknown side type " + side);
    }


    @Override
    public @NotNull BlockPos findSelfPosRelatedInMultipart(Level level, BlockState state, BlockPos pos) {
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
        if (side == SIDE_TYPE_1_0_1) {
            return SELF_POS_1_0_1;
        }
        throw new IllegalStateException("Unexpected side type: " + side);
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
        builder.add(SIDE_TYPE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfernalFurnaceNozzleBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide() || blockEntityType != INFERNAL_FURNACE_NOZZLE){
            return null;
        }
        return (level1, blockPos, blockState1, blockEntity) -> {
            if (blockEntity instanceof InfernalFurnaceNozzleBlockEntity nozzleBlockEntity) {
                nozzleBlockEntity.serverTick();
            }
        };
    }
}
