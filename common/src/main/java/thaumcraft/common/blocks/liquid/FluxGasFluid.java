package thaumcraft.common.blocks.liquid;

import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import static thaumcraft.common.blocks.ThaumcraftBlocks.FLUX_GAS;

public class FluxGasFluid extends FiniteFlowingFluid {


    public FluxGasFluid(int maxLevel, Direction gravityDirection) {
        super(maxLevel, gravityDirection);
    }
    public FluxGasFluid(){
        this(8, Direction.UP);
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 20;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(FluidState fluidState) {
        return FLUX_GAS.defaultBlockState().setValue(liquidLevel, fluidState.getValue(liquidLevel));
    }

}
