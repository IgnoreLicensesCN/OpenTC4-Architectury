package com.linearity.opentc4.recipeclean.blockmatch;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBlockMatcher extends BlockMatcher {
    private final @NotNull Block block;

    private SimpleBlockMatcher(@NotNull Block block) {
        this.block = block;
    }


    private static final Map<Block, BlockMatcher> cache = new ConcurrentHashMap<>();

    public static @NotNull BlockMatcher of(@NotNull Block block) {
        return cache.computeIfAbsent(block, SimpleBlockMatcher::new);
    }
    @Override
    public boolean match(BlockState state) {
        return state.getBlock() == this.block;
    }
}
