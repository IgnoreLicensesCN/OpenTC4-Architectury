package com.linearity.opentc4.recipeclean.blockmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBlockMatcher implements IBlockMatcher {
    private final @NotNull Block block;

    private SimpleBlockMatcher(@NotNull Block block) {
        this.block = block;
    }


    private static final Map<Block, SimpleBlockMatcher> cache = new ConcurrentHashMap<>();

    public static @NotNull SimpleBlockMatcher of(@NotNull Block block) {
        return cache.computeIfAbsent(block, SimpleBlockMatcher::new);
    }
    @Override
    public boolean match(@Nullable Level atLevel, @NotNull BlockState state, @NotNull BlockPos pos) {
        return state.getBlock() == this.block;
    }
}
