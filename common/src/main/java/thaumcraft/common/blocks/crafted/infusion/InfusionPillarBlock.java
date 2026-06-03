package thaumcraft.common.blocks.crafted.infusion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.ThaumcraftBlocks;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;

import java.util.Map;

//projectile graph(to floor)
// W N
//  M
// S E
//N:state north
//S:state south
//W:state west
//E:state ease
//M:infusion matrix
//(right handed up rotate 90)
//TODO:Model
public class InfusionPillarBlock extends SuppressedWarningBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ABOVE = BooleanProperty.create("above");
    public InfusionPillarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ABOVE, false)
        );
    }
    public InfusionPillarBlock() {
        this(
                Properties.of()
                        .strength(3,25)
                        .sound(SoundType.STONE)
                        .requiresCorrectToolForDrops()
                        .noOcclusion()
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, ABOVE);
    }

    public boolean checkExpectedNeighbourState(BlockState selfState,BlockState aboveOrDownState){
        if (selfState.getValue(ABOVE)==aboveOrDownState.getValue(ABOVE)){
            return false;
        }
        return selfState.getValue(FACING)==aboveOrDownState.getValue(FACING);
    }

    public static final Map<Direction,BlockPos> OFFSET_TO_MATRIX = Map.of(
            Direction.NORTH,BlockPos.ZERO.relative(Direction.NORTH.getOpposite()).relative(Direction.NORTH.getClockWise().getOpposite()),
            Direction.WEST,BlockPos.ZERO.relative(Direction.WEST.getOpposite()).relative(Direction.SOUTH.getClockWise().getOpposite()),
            Direction.SOUTH,BlockPos.ZERO.relative(Direction.SOUTH.getOpposite()).relative(Direction.EAST.getClockWise().getOpposite()),
            Direction.EAST,BlockPos.ZERO.relative(Direction.EAST.getOpposite()).relative(Direction.NORTH.getClockWise().getOpposite())
    );
    public void tickInfusionMatrix(LevelAccessor level,BlockState selfState,BlockPos selfPos){
        var matrixPos = OFFSET_TO_MATRIX.get(selfState.getValue(FACING)).offset(selfPos).above();
        if (!selfState.getValue(ABOVE)){
            matrixPos = matrixPos.above();
        }
        level.scheduleTick(matrixPos,level.getBlockState(matrixPos).getBlock(),1);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState prevState, Direction changeFromDirection, BlockState neighborState, LevelAccessor levelAccessor, BlockPos selfPos, BlockPos changedPos) {
        boolean isAbove = prevState.getValue(ABOVE);
        if (
                isAbove && changeFromDirection == Direction.DOWN
                || !isAbove && changeFromDirection == Direction.UP
        ) {
            if (!checkExpectedNeighbourState(prevState,neighborState)){
                tickInfusionMatrix(levelAccessor,prevState,selfPos);
                return isAbove?ThaumcraftBlocks.ARCANE_STONE_BLOCK.defaultBlockState():ThaumcraftBlocks.ARCANE_STONE_BRICKS.defaultBlockState();
            }
        }
        return super.updateShape(prevState, changeFromDirection, neighborState, levelAccessor, selfPos, changedPos);
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }
}
