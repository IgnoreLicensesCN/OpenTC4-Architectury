package com.linearity.opentc4.recipeclean.blockmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleFluidMatcher extends AbstractBlockMatcher {
    protected final boolean matchSource;
    protected final Fluid fluid;

    public SimpleFluidMatcher(Fluid fluid, boolean matchSource) {
        this.matchSource = matchSource;
        this.fluid = fluid;
    }

    @Override
    public boolean match(@Nullable Level atLevel, @NotNull BlockState state, @NotNull BlockPos pos) {
        var fluidState = atLevel.getFluidState(pos);
        if (!fluidState.is(fluid)){return false;}
        if (matchSource && fluidState.isSource()) {
            return true;
        }
        else if(matchSource){
            return false;
        }
        return true;
    }
}
