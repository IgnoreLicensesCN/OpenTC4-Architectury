package com.linearity.opentc4.recipeclean.blockmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IBlockMatcher {
    boolean match(@Nullable Level atLevel, @NotNull BlockState state, @NotNull BlockPos pos);
}
