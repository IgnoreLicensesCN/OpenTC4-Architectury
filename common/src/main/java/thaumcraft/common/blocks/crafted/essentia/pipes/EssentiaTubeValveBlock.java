package thaumcraft.common.blocks.crafted.essentia.pipes;

import com.linearity.opentc4.utils.LevelBlockEntityAccessing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.crafted.essentiabe.pipes.EssentiaTubeValveBlockEntity;

import static com.linearity.opentc4.utils.LevelBlockEntityAccessing.getExistingBlockEntity;

public class EssentiaTubeValveBlock extends AbstractEssentiaTubeBlock
        implements
        EntityBlock {

    public EssentiaTubeValveBlock(Properties properties) {
        super(properties);
        registerDefaultState();
    }
    public EssentiaTubeValveBlock() {
        super();
        registerDefaultState();
    }
    private void registerDefaultState() {
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(POWERED,false)
                        .setValue(FORCE_CUT_FLOW,false)
                        .setValue(FACING, Direction.NORTH)
                        .setValue(AXIS,NEXT_AXIS_TABLE[Direction.NORTH.getAxis().ordinal()])
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, AXIS, POWERED, FORCE_CUT_FLOW);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var clickedFace = blockPlaceContext.getClickedFace();
        var clickedAxis = clickedFace.getAxis();
        var level = blockPlaceContext.getLevel();
        var pos = blockPlaceContext.getClickedPos();
        return this.defaultBlockState()
                .setValue(POWERED, level.hasNeighborSignal(pos))
                .setValue(FACING, clickedFace)
                .setValue(AXIS, NEXT_AXIS_TABLE[clickedAxis.ordinal()]);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EssentiaTubeValveBlockEntity(blockPos, blockState);
    }

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty FORCE_CUT_FLOW = BooleanProperty.create("cut_flow");

    public static final Direction.Axis[][] AXIS_TRANSITION_TABLE = new Direction.Axis[Direction.Axis.values().length][Direction.values().length];
    public static final Direction.Axis[] NEXT_AXIS_TABLE = {Direction.Axis.Y, Direction.Axis.Z,Direction.Axis.X,};
    static {
        for (var axisBefore : Direction.Axis.values()) {
            for (var facingAfter : Direction.values()) {
                Direction.Axis result = switch (facingAfter.getAxis()) {
                    case X -> (axisBefore != Direction.Axis.Y) ? Direction.Axis.Y : Direction.Axis.Z;
                    case Y -> (axisBefore != Direction.Axis.Z) ? Direction.Axis.Z : Direction.Axis.X;
                    case Z -> (axisBefore != Direction.Axis.X) ? Direction.Axis.X : Direction.Axis.Y;
                };
                AXIS_TRANSITION_TABLE[axisBefore.ordinal()][facingAfter.ordinal()] = result;
            }
        }
    }

    public final BlockState changeStateForDirection(BlockState state,Direction changeToFacing){
        var currentAxis = state.getValue(AXIS);
        var currentFacing = state.getValue(FACING);
        if (currentFacing == changeToFacing){
            currentAxis = AXIS_TRANSITION_TABLE[currentAxis.ordinal()][changeToFacing.ordinal()];
        }
        else {
            currentAxis = NEXT_AXIS_TABLE[changeToFacing.getAxis().ordinal()];
        }
        return state.setValue(FACING,changeToFacing).setValue(AXIS,currentAxis);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (!level.isClientSide()) {
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof EssentiaTubeValveBlockEntity valve) {
                var hasSignalNow = level.hasNeighborSignal(pos);
                var hasSignalBefore = state.getValue(POWERED);
                if (hasSignalNow != hasSignalBefore) {
                    level.playSound(null,pos, ThaumcraftSounds.SQUEEK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
                    valve.setBlockStateAndUpdate(state.setValue(POWERED,hasSignalNow));
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide){
            if (LevelBlockEntityAccessing.getExistingBlockEntity(level, pos) instanceof EssentiaTubeValveBlockEntity valve){
                valve.setBlockStateAndUpdate(state.setValue(FORCE_CUT_FLOW,!state.getValue(FORCE_CUT_FLOW)));
            }
        }
        return super.use(state, level, pos, player, interactionHand, blockHitResult);
    }
}
