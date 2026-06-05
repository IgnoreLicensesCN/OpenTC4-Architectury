package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.SuppressedWarningBlock;
import thaumcraft.common.tiles.crafted.FluxScrubberBlockEntity;

import java.util.EnumMap;
import java.util.Map;

//voxel shape changed
public class FluxScrubberBlock extends SuppressedWarningBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public FluxScrubberBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }
    public FluxScrubberBlock() {
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
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getClickedFace().getOpposite());
    }

    public static final Map<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class){{
        put(Direction.NORTH,Shapes.or(Block.box(0,0,8,16,16,16),Block.box(0,0,0,8,8,8)));
        put(Direction.WEST,Shapes.or(Block.box(8,0,0,16,16,16),Block.box(0,0,8,8,8,16)));
        put(Direction.SOUTH,Shapes.or(Block.box(0,0,0,16,16,8),Block.box(8,0,8,16,8,16)));
        put(Direction.EAST,Shapes.or(Block.box(0,0,0,8,16,16),Block.box(8,0,0,16,8,8)));
        put(Direction.UP,Shapes.or(Block.box(0,0,0,16,8,16),Block.box(0,8,0,8,16,8)));
        put(Direction.DOWN,Shapes.or(Block.box(0,8,0,16,16,16),Block.box(0,0,8,8,8,16)));
    }};

    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPES.get(blockState.getValue(FACING));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluxScrubberBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return (level1, blockPos, blockState1, be) -> {
                if (be instanceof FluxScrubberBlockEntity fluxScrubber) {
                    fluxScrubber.serverTick();
                }
            };
        }
        return null;
    }
}
