package thaumcraft.common.blocks.abstracts;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public interface IShapeConnectNearBlock {
    BooleanProperty NORTH_CONNECTED = BooleanProperty.create("north_connected");
    BooleanProperty SOUTH_CONNECTED = BooleanProperty.create("south_connected");
    BooleanProperty EAST_CONNECTED = BooleanProperty.create("east_connected");
    BooleanProperty WEST_CONNECTED = BooleanProperty.create("west_connected");
    BooleanProperty UP_CONNECTED = BooleanProperty.create("up_connected");
    BooleanProperty DOWN_CONNECTED = BooleanProperty.create("down_connected");

    EnumMap<Direction,BooleanProperty> DIRECTION_TO_PROPERTY_MAP = new EnumMap<>(Direction.class){
        {
            put(Direction.NORTH, NORTH_CONNECTED);
            put(Direction.SOUTH, SOUTH_CONNECTED);
            put(Direction.EAST, EAST_CONNECTED);
            put(Direction.WEST, WEST_CONNECTED);
            put(Direction.UP, UP_CONNECTED);
            put(Direction.DOWN, DOWN_CONNECTED);
        }
    };

    VoxelShape DEFAULT_SHAPE = Block.box(2, 2, 2, 14, 14, 14);
    VoxelShape DOWN_SHAPE = Block.box(2, 0, 2, 14, 14, 14);
    VoxelShape UP_SHAPE = Block.box(2, 2, 2, 14, 16, 14);
    VoxelShape UP_DOWN_SHAPE = Block.box(2, 0, 2, 14, 16, 14);
    VoxelShape EAST_SHAPE = Block.box(2, 2, 2, 16, 14, 14);
    VoxelShape EAST_DOWN_SHAPE = Block.box(2, 0, 2, 16, 14, 14);
    VoxelShape EAST_UP_SHAPE = Block.box(2, 2, 2, 16, 16, 14);
    VoxelShape EAST_UP_DOWN_SHAPE = Block.box(2, 0, 2, 16, 16, 14);
    VoxelShape WEST_SHAPE = Block.box(0, 2, 2, 14, 14, 14);
    VoxelShape WEST_DOWN_SHAPE = Block.box(0, 0, 2, 14, 14, 14);
    VoxelShape WEST_UP_SHAPE = Block.box(0, 2, 2, 14, 16, 14);
    VoxelShape WEST_UP_DOWN_SHAPE = Block.box(0, 0, 2, 14, 16, 14);
    VoxelShape WEST_EAST_SHAPE = Block.box(0, 2, 2, 16, 14, 14);
    VoxelShape WEST_EAST_DOWN_SHAPE = Block.box(0, 0, 2, 16, 14, 14);
    VoxelShape WEST_EAST_UP_SHAPE = Block.box(0, 2, 2, 16, 16, 14);
    VoxelShape WEST_EAST_UP_DOWN_SHAPE = Block.box(0, 0, 2, 16, 16, 14);
    VoxelShape SOUTH_SHAPE = Block.box(2, 2, 2, 14, 14, 16);
    VoxelShape SOUTH_DOWN_SHAPE = Block.box(2, 0, 2, 14, 14, 16);
    VoxelShape SOUTH_UP_SHAPE = Block.box(2, 2, 2, 14, 16, 16);
    VoxelShape SOUTH_UP_DOWN_SHAPE = Block.box(2, 0, 2, 14, 16, 16);
    VoxelShape SOUTH_EAST_SHAPE = Block.box(2, 2, 2, 16, 14, 16);
    VoxelShape SOUTH_EAST_DOWN_SHAPE = Block.box(2, 0, 2, 16, 14, 16);
    VoxelShape SOUTH_EAST_UP_SHAPE = Block.box(2, 2, 2, 16, 16, 16);
    VoxelShape SOUTH_EAST_UP_DOWN_SHAPE = Block.box(2, 0, 2, 16, 16, 16);
    VoxelShape SOUTH_WEST_SHAPE = Block.box(0, 2, 2, 14, 14, 16);
    VoxelShape SOUTH_WEST_DOWN_SHAPE = Block.box(0, 0, 2, 14, 14, 16);
    VoxelShape SOUTH_WEST_UP_SHAPE = Block.box(0, 2, 2, 14, 16, 16);
    VoxelShape SOUTH_WEST_UP_DOWN_SHAPE = Block.box(0, 0, 2, 14, 16, 16);
    VoxelShape SOUTH_WEST_EAST_SHAPE = Block.box(0, 2, 2, 16, 14, 16);
    VoxelShape SOUTH_WEST_EAST_DOWN_SHAPE = Block.box(0, 0, 2, 16, 14, 16);
    VoxelShape SOUTH_WEST_EAST_UP_SHAPE = Block.box(0, 2, 2, 16, 16, 16);
    VoxelShape SOUTH_WEST_EAST_UP_DOWN_SHAPE = Block.box(0, 0, 2, 16, 16, 16);
    VoxelShape NORTH_SHAPE = Block.box(2, 2, 0, 14, 14, 14);
    VoxelShape NORTH_DOWN_SHAPE = Block.box(2, 0, 0, 14, 14, 14);
    VoxelShape NORTH_UP_SHAPE = Block.box(2, 2, 0, 14, 16, 14);
    VoxelShape NORTH_UP_DOWN_SHAPE = Block.box(2, 0, 0, 14, 16, 14);
    VoxelShape NORTH_EAST_SHAPE = Block.box(2, 2, 0, 16, 14, 14);
    VoxelShape NORTH_EAST_DOWN_SHAPE = Block.box(2, 0, 0, 16, 14, 14);
    VoxelShape NORTH_EAST_UP_SHAPE = Block.box(2, 2, 0, 16, 16, 14);
    VoxelShape NORTH_EAST_UP_DOWN_SHAPE = Block.box(2, 0, 0, 16, 16, 14);
    VoxelShape NORTH_WEST_SHAPE = Block.box(0, 2, 0, 14, 14, 14);
    VoxelShape NORTH_WEST_DOWN_SHAPE = Block.box(0, 0, 0, 14, 14, 14);
    VoxelShape NORTH_WEST_UP_SHAPE = Block.box(0, 2, 0, 14, 16, 14);
    VoxelShape NORTH_WEST_UP_DOWN_SHAPE = Block.box(0, 0, 0, 14, 16, 14);
    VoxelShape NORTH_WEST_EAST_SHAPE = Block.box(0, 2, 0, 16, 14, 14);
    VoxelShape NORTH_WEST_EAST_DOWN_SHAPE = Block.box(0, 0, 0, 16, 14, 14);
    VoxelShape NORTH_WEST_EAST_UP_SHAPE = Block.box(0, 2, 0, 16, 16, 14);
    VoxelShape NORTH_WEST_EAST_UP_DOWN_SHAPE = Block.box(0, 0, 0, 16, 16, 14);
    VoxelShape NORTH_SOUTH_SHAPE = Block.box(2, 2, 0, 14, 14, 16);
    VoxelShape NORTH_SOUTH_DOWN_SHAPE = Block.box(2, 0, 0, 14, 14, 16);
    VoxelShape NORTH_SOUTH_UP_SHAPE = Block.box(2, 2, 0, 14, 16, 16);
    VoxelShape NORTH_SOUTH_UP_DOWN_SHAPE = Block.box(2, 0, 0, 14, 16, 16);
    VoxelShape NORTH_SOUTH_EAST_SHAPE = Block.box(2, 2, 0, 16, 14, 16);
    VoxelShape NORTH_SOUTH_EAST_DOWN_SHAPE = Block.box(2, 0, 0, 16, 14, 16);
    VoxelShape NORTH_SOUTH_EAST_UP_SHAPE = Block.box(2, 2, 0, 16, 16, 16);
    VoxelShape NORTH_SOUTH_EAST_UP_DOWN_SHAPE = Block.box(2, 0, 0, 16, 16, 16);
    VoxelShape NORTH_SOUTH_WEST_SHAPE = Block.box(0, 2, 0, 14, 14, 16);
    VoxelShape NORTH_SOUTH_WEST_DOWN_SHAPE = Block.box(0, 0, 0, 14, 14, 16);
    VoxelShape NORTH_SOUTH_WEST_UP_SHAPE = Block.box(0, 2, 0, 14, 16, 16);
    VoxelShape NORTH_SOUTH_WEST_UP_DOWN_SHAPE = Block.box(0, 0, 0, 14, 16, 16);
    VoxelShape NORTH_SOUTH_WEST_EAST_SHAPE = Block.box(0, 2, 0, 16, 14, 16);
    VoxelShape NORTH_SOUTH_WEST_EAST_DOWN_SHAPE = Block.box(0, 0, 0, 16, 14, 16);
    VoxelShape NORTH_SOUTH_WEST_EAST_UP_SHAPE = Block.box(0, 2, 0, 16, 16, 16);
    VoxelShape NORTH_SOUTH_WEST_EAST_UP_DOWN_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    Int2ObjectMap<VoxelShape> voxelShapeMap = new Int2ObjectOpenHashMap<>(64) {
        {
            put(0, DEFAULT_SHAPE);
            put(32, DOWN_SHAPE);
            put(16, UP_SHAPE);
            put(48, UP_DOWN_SHAPE);
            put(8, EAST_SHAPE);
            put(40, EAST_DOWN_SHAPE);
            put(24, EAST_UP_SHAPE);
            put(56, EAST_UP_DOWN_SHAPE);
            put(4, WEST_SHAPE);
            put(36, WEST_DOWN_SHAPE);
            put(20, WEST_UP_SHAPE);
            put(52, WEST_UP_DOWN_SHAPE);
            put(12, WEST_EAST_SHAPE);
            put(44, WEST_EAST_DOWN_SHAPE);
            put(28, WEST_EAST_UP_SHAPE);
            put(60, WEST_EAST_UP_DOWN_SHAPE);
            put(2, SOUTH_SHAPE);
            put(34, SOUTH_DOWN_SHAPE);
            put(18, SOUTH_UP_SHAPE);
            put(50, SOUTH_UP_DOWN_SHAPE);
            put(10, SOUTH_EAST_SHAPE);
            put(42, SOUTH_EAST_DOWN_SHAPE);
            put(26, SOUTH_EAST_UP_SHAPE);
            put(58, SOUTH_EAST_UP_DOWN_SHAPE);
            put(6, SOUTH_WEST_SHAPE);
            put(38, SOUTH_WEST_DOWN_SHAPE);
            put(22, SOUTH_WEST_UP_SHAPE);
            put(54, SOUTH_WEST_UP_DOWN_SHAPE);
            put(14, SOUTH_WEST_EAST_SHAPE);
            put(46, SOUTH_WEST_EAST_DOWN_SHAPE);
            put(30, SOUTH_WEST_EAST_UP_SHAPE);
            put(62, SOUTH_WEST_EAST_UP_DOWN_SHAPE);
            put(1, NORTH_SHAPE);
            put(33, NORTH_DOWN_SHAPE);
            put(17, NORTH_UP_SHAPE);
            put(49, NORTH_UP_DOWN_SHAPE);
            put(9, NORTH_EAST_SHAPE);
            put(41, NORTH_EAST_DOWN_SHAPE);
            put(25, NORTH_EAST_UP_SHAPE);
            put(57, NORTH_EAST_UP_DOWN_SHAPE);
            put(5, NORTH_WEST_SHAPE);
            put(37, NORTH_WEST_DOWN_SHAPE);
            put(21, NORTH_WEST_UP_SHAPE);
            put(53, NORTH_WEST_UP_DOWN_SHAPE);
            put(13, NORTH_WEST_EAST_SHAPE);
            put(45, NORTH_WEST_EAST_DOWN_SHAPE);
            put(29, NORTH_WEST_EAST_UP_SHAPE);
            put(61, NORTH_WEST_EAST_UP_DOWN_SHAPE);
            put(3, NORTH_SOUTH_SHAPE);
            put(35, NORTH_SOUTH_DOWN_SHAPE);
            put(19, NORTH_SOUTH_UP_SHAPE);
            put(51, NORTH_SOUTH_UP_DOWN_SHAPE);
            put(11, NORTH_SOUTH_EAST_SHAPE);
            put(43, NORTH_SOUTH_EAST_DOWN_SHAPE);
            put(27, NORTH_SOUTH_EAST_UP_SHAPE);
            put(59, NORTH_SOUTH_EAST_UP_DOWN_SHAPE);
            put(7, NORTH_SOUTH_WEST_SHAPE);
            put(39, NORTH_SOUTH_WEST_DOWN_SHAPE);
            put(23, NORTH_SOUTH_WEST_UP_SHAPE);
            put(55, NORTH_SOUTH_WEST_UP_DOWN_SHAPE);
            put(15, NORTH_SOUTH_WEST_EAST_SHAPE);
            put(47, NORTH_SOUTH_WEST_EAST_DOWN_SHAPE);
            put(31, NORTH_SOUTH_WEST_EAST_UP_SHAPE);
            put(63, NORTH_SOUTH_WEST_EAST_UP_DOWN_SHAPE);
        }
    };

    default void addStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH_CONNECTED,SOUTH_CONNECTED, EAST_CONNECTED, WEST_CONNECTED, UP_CONNECTED, DOWN_CONNECTED);
    }

    default @NotNull VoxelShape getShapeConnectNear(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        int flag = 0;
        var northState = blockGetter.getBlockState(blockPos.north());
        var southState = blockGetter.getBlockState(blockPos.south());
        var eastState = blockGetter.getBlockState(blockPos.east());
        var westState = blockGetter.getBlockState(blockPos.west());
        var upState = blockGetter.getBlockState(blockPos.above());
        var downState = blockGetter.getBlockState(blockPos.below());
        boolean northConnected = northState.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(DEFAULT_SHAPE,northState.getShape(blockGetter, blockPos));
        boolean southConnected = southState.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(DEFAULT_SHAPE,southState.getShape(blockGetter, blockPos));
        boolean eastConnected = eastState.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(DEFAULT_SHAPE,eastState.getShape(blockGetter, blockPos));
        boolean westConnected = westState.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(DEFAULT_SHAPE,westState.getShape(blockGetter, blockPos));
        boolean upConnected = upState.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(DEFAULT_SHAPE,upState.getShape(blockGetter, blockPos));
        boolean downConnected = downState.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(DEFAULT_SHAPE,downState.getShape(blockGetter, blockPos));

        if (northConnected) {
            flag |= 1;
        }
        if (southConnected) {
            flag |= 1<<1;
        }
        if (westConnected) {
            flag |= 1<<2;
        }
        if (eastConnected) {
            flag |= 1<<3;
        }
        if (upConnected) {
            flag |= 1<<4;
        }
        if (downConnected) {
            flag |= 1<<5;
        }
        return voxelShapeMap.get(flag);
    }

    default boolean isCovered(VoxelShape beingCovered, VoxelShape coverWithFace) {
        return !Shapes.joinIsNotEmpty(coverWithFace, beingCovered, BooleanOp.ONLY_FIRST);
    }

    default BlockState getState(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos){
        for (Direction direction : Direction.values()) {
            var property = DIRECTION_TO_PROPERTY_MAP.get(direction);
            var directionState = blockGetter.getBlockState(blockPos.relative(direction));
            boolean directionConnected = directionState.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(DEFAULT_SHAPE,directionState.getShape(blockGetter, blockPos));
            blockState = blockState.setValue(property,directionConnected);
        }
        return blockState;
    }
}
