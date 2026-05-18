package thaumcraft.common.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public @NotNull ItemStack pickupBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if (blockState.getFluidState().getValue(finiteFluid.liquidLevel) == finiteFluid.maxLevel) {
            var bucketItem = this.fluid.getBucket();
            if (bucketItem == Items.AIR){
                return ItemStack.EMPTY;
            }
            levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            return new ItemStack(this.fluid.getBucket());
        } else {
            return ItemStack.EMPTY;
        }
    }

}

