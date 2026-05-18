package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;

public abstract class FiniteLiquidBlock extends LiquidBlock {
    public final FiniteFlowingFluid finiteFluid;

    public FiniteLiquidBlock(FiniteFlowingFluid fluid, Properties props) {
        super(fluid, props);
        this.finiteFluid = fluid;
        this.registerDefaultState(this.stateDefinition.any().setValue(finiteFluid.liquidLevel, finiteFluid.maxLevel));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(finiteFluid.liquidLevel);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return false;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        level.scheduleTick(blockPos, this, finiteFluid.getTickDelay(level));
    }

}

