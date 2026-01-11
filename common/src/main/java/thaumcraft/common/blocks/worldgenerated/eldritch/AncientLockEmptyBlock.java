package thaumcraft.common.blocks.worldgenerated.eldritch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;

public class AncientLockEmptyBlock extends Block{
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    public AncientLockEmptyBlock(Properties properties) {
        super(properties);
    }
    public AncientLockEmptyBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(-1,Float.MAX_VALUE)
                .sound(SoundType.STONE)
                .mapColor(MapColor.COLOR_BLACK)
                .lightLevel(s -> 5)
                .requiresCorrectToolForDrops());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    public void insertRunedTablet(Level level, BlockState state, BlockPos pos) {
        if (state.getBlock() != this){return;}
        level.setBlock(
                pos,
                ThaumcraftBlocks.ANCIENT_LOCK_INSERTED.defaultBlockState().setValue(FACING,state.getValue(FACING)),
                3
        );
    }
}
