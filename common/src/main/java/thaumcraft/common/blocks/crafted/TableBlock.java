package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.researchtable.IResearchTableAspectEditTool;
import thaumcraft.api.wands.IArcaneCraftingWand;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.blocks.ThaumcraftBlocks;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_AXIS;

public class TableBlock extends Block implements IWandInteractableBlock {
    //TODO:texture png(from table.png)
    public static final VoxelShape X_AXIS_SHAPE = Shapes.or(
            Block.box(0, 12, 0, 16, 16, 16),
            Block.box(2, 0, 2, 6, 8, 6),
            Block.box(10, 0, 2, 14, 8, 6),
            Block.box(4, 8, 0, 12, 12, 16)     // Crossbar (X)
    );
    private static final VoxelShape Z_AXIS_SHAPE = Shapes.or(
            Block.box(0, 12, 0, 16, 16, 16),   // Top
            Block.box(2, 0, 2, 6, 8, 6),
            Block.box(10, 0, 2, 14, 8, 6),
            Block.box(0, 8, 4, 16, 12, 12)     // Crossbar (Z)
    );

    public static final EnumProperty<Direction.Axis> AXIS = HORIZONTAL_AXIS;
    public TableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }
    public TableBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE));
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    public static final Direction[] X_AXIS_DIRECTIONS = {Direction.WEST, Direction.EAST};
    public static final Direction[] Z_AXIS_DIRECTIONS = {Direction.NORTH, Direction.SOUTH};
    public static final Direction[] Y_AXIS_DIRECTIONS = {Direction.UP, Direction.DOWN};
    public Direction[] getDirectionsForAxis(Direction.Axis axis) {
        return switch (axis) {
            case X->X_AXIS_DIRECTIONS;
            case Y->Y_AXIS_DIRECTIONS;
            case Z->Z_AXIS_DIRECTIONS;
        };
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        var usingStack = player.getItemInHand(interactionHand);
        if (!usingStack.isEmpty()) {
            if (usingStack.getItem() instanceof IResearchTableAspectEditTool writeTool
                    && writeTool.canCreateResearchTable(level,blockPos,usingStack)) {
                var thisAxis = blockState.getValue(AXIS);
                var probablyLeftPartDirections = getDirectionsForAxis(thisAxis);
                for (Direction probablyLeftPartDirection : probablyLeftPartDirections){
                    var probablyRightPartPos = blockPos.relative(probablyLeftPartDirection);
                    var probablyRightPartState = level.getBlockState(probablyRightPartPos);
                    if (probablyRightPartState.getBlock() == this && probablyRightPartState.getValue(AXIS) == thisAxis) {
                        level.setBlockAndUpdate(
                                blockPos,
                                ThaumcraftBlocks.RESEARCH_TABLE_LEFT_PART
                                        .defaultBlockState()
                                        .setValue(FACING,probablyLeftPartDirection)
                        );
                        level.setBlockAndUpdate(
                                probablyRightPartPos,
                                ThaumcraftBlocks.RESEARCH_TABLE_RIGHT_PART
                                        .defaultBlockState()
                                        .setValue(FACING,probablyLeftPartDirection.getOpposite())
                        );
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var usingWand = useOnContext.getItemInHand();
        if (usingWand.getItem() instanceof IArcaneCraftingWand craftingWand && craftingWand.canInsertIntoArcaneCraftingTable(usingWand)) {
            var level = useOnContext.getLevel();
            level.setBlockAndUpdate(useOnContext.getClickedPos(), ThaumcraftBlocks.ARCANE_WORKBENCH.defaultBlockState());
            level.playSound(
                    useOnContext.getPlayer(),
                    useOnContext.getClickedPos(),
                    SoundEvents.WOODEN_BUTTON_CLICK_ON,
                    SoundSource.BLOCKS,
                    0.15F,
                    0.5F
            );
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void interactOnWandInteractable(Level level, LivingEntity livingEntity, ItemStack usingWand, int useRemainingCount) {
        IWandInteractableBlock.super.interactOnWandInteractable(level, livingEntity, usingWand, useRemainingCount);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        var axisValue = blockState.getValue(AXIS);
        if (axisValue == Direction.Axis.X) {
            return X_AXIS_SHAPE;
        }
        return Z_AXIS_SHAPE;
    }


    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (blockState.getValue(AXIS)) {
                case Z -> blockState.setValue(AXIS, Direction.Axis.X);
                case X -> blockState.setValue(AXIS, Direction.Axis.Z);
                default -> blockState;
            };
            default -> blockState;
        };
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS);
    }
}
