package thaumcraft.common.blocks.crafted.visnet;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.wands.IWandInteractableBlock;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.tiles.ThaumcraftBlockEntities;
import thaumcraft.common.tiles.crafted.VisNetRelayBlockEntity;

//TODO:loot table,BER
public class VisNetRelayBlock extends Block implements IWandInteractableBlock, EntityBlock {
    public VisNetRelayBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(COLOR, 0)
                        .setValue(FACING, Direction.DOWN)
        );
    }
    public VisNetRelayBlock() {
        this(
                Properties.of()
                        .lightLevel(state -> 10)//i'm lazy to check if it's valid
                        .sound(SoundType.METAL)
                        .strength(3,17)
        );
    }
    public static final int COLOR_TYPES = 7;
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, COLOR_TYPES-1);
    //  AIR AIR                 AIR
    //  AIR Relay(facing:down) AIR
    //  AIR DIRT(or else)       AIR
    public static final DirectionProperty FACING = BlockStateProperties.FACING;


    // 注册 Property
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, FACING);
    }

    // 放置时决定 facing
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace());
    }
    @Override
    public @NotNull InteractionResult useOnWandInteractable(UseOnContext useOnContext) {
        var level = useOnContext.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        var state = level.getBlockState(useOnContext.getClickedPos());
        state = state.setValue(COLOR, (state.getValue(COLOR) + 1)%COLOR_TYPES);
        level.setBlockAndUpdate(useOnContext.getClickedPos(), state);
        if (level.getBlockEntity(useOnContext.getClickedPos()) instanceof VisNetRelayBlockEntity relay) {
            relay.removeThisNode();
            relay.nodeRefresh=true;
            relay.markDirtyAndUpdateSelf();
            level.playSound(null,relay.getBlockPos(), ThaumcraftSounds.CRYSTAL, SoundSource.BLOCKS, 0.2F, 1.0F);
        }
        return InteractionResult.SUCCESS;
    }
    private static final VoxelShape SHAPE_DOWN = Block.box(
            5, 0, 5,
            11, 8, 11
    );

    private static final VoxelShape SHAPE_UP = Block.box(
            5, 8, 5,
            11, 16, 11
    );

    private static final VoxelShape SHAPE_NORTH = Block.box(
            5, 5, 0,
            11, 11, 8
    );

    private static final VoxelShape SHAPE_SOUTH = Block.box(
            5, 5, 8,
            11, 11, 16
    );

    private static final VoxelShape SHAPE_WEST = Block.box(
            0, 5, 5,
            8, 11, 11
    );

    private static final VoxelShape SHAPE_EAST = Block.box(
            8, 5, 5,
            16, 11, 11
    );
    @Override
    public @NotNull VoxelShape getShape(BlockState state,
                                        BlockGetter level,
                                        BlockPos pos,
                                        CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case UP -> SHAPE_UP;
            case DOWN -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
        };
    }@Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction dir = state.getValue(FACING);
        BlockPos supportPos = pos.relative(dir.getOpposite());
        return level.getBlockState(supportPos)
                .isFaceSturdy(level, supportPos, dir);
    }

    @Override
    public void neighborChanged(BlockState state,
                                Level level,
                                BlockPos pos,
                                Block block,
                                BlockPos fromPos,
                                boolean isMoving) {

        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getBlock() == this){
            return new VisNetRelayBlockEntity(blockPos, blockState);
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ThaumcraftBlockEntities.VIS_RELAY && blockState.getBlock() == this){
            if (Platform.getEnvironment() == Env.SERVER){
                return ((level1, blockPos, blockState1, blockEntity) -> {
                    if (blockEntity instanceof VisNetRelayBlockEntity relay) {
                        relay.tick();
                    }
                });
            }
            if (Platform.getEnvironment() ==  Env.CLIENT){
                return ((level1, blockPos, blockState1, blockEntity) -> {
                    if (blockEntity instanceof VisNetRelayBlockEntity relay) {
                        relay.clientTick();
                    }
                });
            }
        }
        return null;
    }
}
