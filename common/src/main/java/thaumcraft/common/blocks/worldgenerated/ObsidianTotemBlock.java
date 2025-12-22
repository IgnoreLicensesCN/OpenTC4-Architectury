package thaumcraft.common.blocks.worldgenerated;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class ObsidianTotemBlock extends Block {
    public static final IntegerProperty RENDER_STATE = IntegerProperty.create("render_state", 0, 5);
    public static final int RENDER_STATE_PROPERTY_VALUE_UP_HAS_TOTEM = 0;
    public static final int RENDER_STATE_PROPERTY_VALUE_SINGLE_TOTEM = 1;
    public static final int RENDER_STATE_PROPERTY_VALUE_TOP_TOTEM_1 = 2;
    public static final int RENDER_STATE_PROPERTY_VALUE_TOP_TOTEM_2 = 3;
    public static final int RENDER_STATE_PROPERTY_VALUE_TOP_TOTEM_3 = 4;
    public static final int RENDER_STATE_PROPERTY_VALUE_TOP_TOTEM_4 = 5;
    public final BlockState RENDER_STATE_UP_HAS_TOTEM = defaultBlockState().setValue(RENDER_STATE, RENDER_STATE_PROPERTY_VALUE_UP_HAS_TOTEM);
    public final BlockState RENDER_STATE_SINGLE_TOTEM = defaultBlockState().setValue(RENDER_STATE, RENDER_STATE_PROPERTY_VALUE_SINGLE_TOTEM);
    public final BlockState RENDER_STATE_TOP_TOTEM_1 = defaultBlockState().setValue(RENDER_STATE, RENDER_STATE_PROPERTY_VALUE_TOP_TOTEM_1);
    public final BlockState RENDER_STATE_TOP_TOTEM_2 = defaultBlockState().setValue(RENDER_STATE, RENDER_STATE_PROPERTY_VALUE_TOP_TOTEM_2);
    public final BlockState RENDER_STATE_TOP_TOTEM_3 = defaultBlockState().setValue(RENDER_STATE, RENDER_STATE_PROPERTY_VALUE_TOP_TOTEM_3);
    public final BlockState RENDER_STATE_TOP_TOTEM_4 = defaultBlockState().setValue(RENDER_STATE, RENDER_STATE_PROPERTY_VALUE_TOP_TOTEM_4);
    public final BlockState[] RENDER_STATES_TOP_TOTEM = new BlockState[]{RENDER_STATE_TOP_TOTEM_1, RENDER_STATE_TOP_TOTEM_2, RENDER_STATE_TOP_TOTEM_3,RENDER_STATE_TOP_TOTEM_4};

    public ObsidianTotemBlock(Properties properties) {
        super(properties);
    }
    public ObsidianTotemBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).explosionResistance(999));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        boolean up = level.getBlockState(pos.above()).getBlock() instanceof ObsidianTotemBlock;
        if (up){
            return RENDER_STATE_UP_HAS_TOTEM;
        }
        boolean down = level.getBlockState(pos.below()).getBlock() instanceof ObsidianTotemBlock;
        if (!down){
            return RENDER_STATE_SINGLE_TOTEM;
        }

        int x = pos.getX(), y = pos.getY(), z = pos.getZ();

        int base = (x % 4 + y % 4 + z % 4) % 4; // 0~3

        return RENDER_STATES_TOP_TOTEM[base];
    }
    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos currentPos, BlockPos neighborPos) {
        if (!world.isClientSide()) {
            boolean up = world.getBlockState(currentPos.above()).getBlock() instanceof ObsidianTotemBlock;
            if (up){
                return RENDER_STATE_UP_HAS_TOTEM;
            }
            boolean down = world.getBlockState(currentPos.below()).getBlock() instanceof ObsidianTotemBlock;
            if (!down){
                return RENDER_STATE_SINGLE_TOTEM;
            }

            int x = currentPos.getX(), y = currentPos.getY(), z = currentPos.getZ();
            int base = (x % 4 + y % 4 + z % 4) % 4;

            return RENDER_STATES_TOP_TOTEM[base];
        }
        return state;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RENDER_STATE);
    }

}
