package thaumcraft.common.blocks.crafted;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

//used for golem ai not here.
public class GolemFetterBlock extends Block {

    public GolemFetterBlock(Properties properties) {
        super(properties);
    }
    public GolemFetterBlock() {
        super(
                BlockBehaviour.Properties.copy(Blocks.STONE)
                .strength(2.F,10.F)
        );
    }


    //steal from redstone lamp
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
    @Override
    public void tick(BlockState arg, ServerLevel arg2, BlockPos arg3, RandomSource arg4) {
        if (arg.getValue(LIT) && !arg2.hasNeighborSignal(arg3)) {
            arg2.setBlock(arg3, arg.cycle(LIT), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(LIT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return this.defaultBlockState().setValue(LIT, arg.getLevel().hasNeighborSignal(arg.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState arg, Level arg2, BlockPos arg3, Block arg4, BlockPos arg5, boolean bl) {
        if (!arg2.isClientSide) {
            boolean bl2 = arg.getValue(LIT);
            if (bl2 != arg2.hasNeighborSignal(arg3)) {
                if (bl2) {
                    arg2.scheduleTick(arg3, this, 4);
                } else {
                    arg2.setBlock(arg3, arg.cycle(LIT), 2);
                }
            }
        }
    }
}
