package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.wands.IArcaneCraftingWand;
import thaumcraft.api.wands.IWandInteractableBlock;

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

    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var usingWand = useOnContext.getItemInHand();
        if (usingWand.getItem() instanceof IArcaneCraftingWand craftingWand && craftingWand.canInsertIntoArcaneCraftingTable(usingWand)) {
            //TODO:new ArcaneWorktable but **do not place wand on**(yes i want that)

            var level = useOnContext.getLevel();
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
