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
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final IntegerProperty COORDINATE_BASED_RAND = IntegerProperty.create("rand", 1, 4);


    public ObsidianTotemBlock(Properties properties) {
        super(properties);
    }
    public ObsidianTotemBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        boolean up = level.getBlockState(pos.above()).getBlock() instanceof ObsidianTotemBlock;
        boolean down = level.getBlockState(pos.below()).getBlock() instanceof ObsidianTotemBlock;

        int x = pos.getX(), y = pos.getY(), z = pos.getZ();

        int base = (x % 4 + y % 4 + z % 4) % 4; // 0~3

        return defaultBlockState()
                .setValue(UP, up)
                .setValue(DOWN, down)
                .setValue(COORDINATE_BASED_RAND, base);
    }
    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos currentPos, BlockPos neighborPos) {
        if (!world.isClientSide()) {
            boolean up = world.getBlockState(currentPos.above()).is(this);
            boolean down = world.getBlockState(currentPos.below()).is(this);

            int x = currentPos.getX(), y = currentPos.getY(), z = currentPos.getZ();
            int base = (x % 4 + y % 4 + z % 4) % 4;

            return state
                    .setValue(UP, up)
                    .setValue(DOWN, down)
                    .setValue(COORDINATE_BASED_RAND, base);
        }
        return state;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, COORDINATE_BASED_RAND);
    }

}
