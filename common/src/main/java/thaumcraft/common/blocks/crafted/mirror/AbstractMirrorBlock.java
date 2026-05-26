package thaumcraft.common.blocks.crafted.mirror;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.mirror.AbstractMirrorBlockEntity;

import java.util.Map;

import static thaumcraft.common.blocks.crafted.jars.JarBlock.JAR_SOUND;

public abstract class AbstractMirrorBlock extends SuppressedWarningBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public AbstractMirrorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN));
    }
    public AbstractMirrorBlock(){
        this(
                Properties.of()
                        .sound(JAR_SOUND)
                        .strength(1,10)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    public static final Map<Direction, VoxelShape> SHAPES = Map.of(
            Direction.DOWN, Block.box(0, 15, 0, 16, 16, 16),
            Direction.UP, Block.box(0, 0, 0, 16, 1, 16),
            Direction.NORTH, Block.box(0, 0, 15, 16, 16, 16),
            Direction.SOUTH, Block.box(0, 0, 0, 16, 16, 1),
            Direction.WEST, Block.box(15, 0, 0, 16, 16, 16),
            Direction.EAST, Block.box(0, 0, 0, 15, 16, 16)
    );

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPES.get(blockState.getValue(FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        var clickedFace = blockPlaceContext.getClickedFace();
        return defaultBlockState().setValue(FACING, clickedFace);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return canSupportCenter(levelReader, blockPos.below(), blockState.getValue(FACING));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState prevState, Direction changeFromDirection, BlockState blockState2, LevelAccessor levelAccessor, BlockPos selfPos, BlockPos changedPos) {
        var facing = prevState.getValue(FACING);
        if (changeFromDirection != facing.getOpposite()) {
            return super.updateShape(prevState, changeFromDirection, blockState2, levelAccessor, selfPos, changedPos);
        }
        if (!this.canSurvive(prevState, levelAccessor, selfPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(prevState, changeFromDirection, blockState2, levelAccessor, selfPos, changedPos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if (!level.isClientSide()) {
            if (level.getBlockEntity(blockPos) instanceof AbstractMirrorBlockEntity mirror && itemStack.hasTag()) {
                var tag = itemStack.getTag();
                if (tag != null) {
                    mirror.readLinkedFromTag(tag);
                }
            }
        }
    }
}
