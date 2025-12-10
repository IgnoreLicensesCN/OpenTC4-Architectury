package com.linearity.opentc4.recipeclean.blockmatch;

import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockMatcher {

    public abstract boolean match(BlockState state);
}
