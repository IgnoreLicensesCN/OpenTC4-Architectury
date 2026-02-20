package com.linearity.opentc4.recipeclean.blockmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.ThaumcraftBlocks;

public class BlockMatcherPresents {
    public static final IBlockMatcher AIR_BLOCK_MATCHER = new IBlockMatcher() {
        @Override
        public boolean match(@Nullable Level atLevel, @NotNull BlockState state, @NotNull BlockPos pos) {
            return state.isAir();
        }
    };
    public static final SimpleBlockMatcher OBSIDIAN_MATCHER = SimpleBlockMatcher.of(Blocks.OBSIDIAN);
    public static final SimpleBlockMatcher NETHER_BRICKS_MATCHER = SimpleBlockMatcher.of(Blocks.NETHER_BRICKS);
    public static final SimpleBlockMatcher IRON_BARS_MATCHER = SimpleBlockMatcher.of(Blocks.IRON_BARS);
    public static final SimpleFluidMatcher LAVA_SOURCE_MATCHER = new SimpleFluidMatcher(Fluids.LAVA, true);

    public static final SimpleBlockMatcher ADVANCED_ALCHEMICAL_CONSTRUCT_MATCHER = SimpleBlockMatcher.of(
            ThaumcraftBlocks.ADVANCED_ALCHEMICAL_CONSTRUCT
    );
    public static final SimpleBlockMatcher ALCHEMICAL_CONSTRUCT_MATCHER = SimpleBlockMatcher.of(
            ThaumcraftBlocks.ALCHEMICAL_CONSTRUCT
    );
    public static final SimpleBlockMatcher ALCHEMICAL_FURNACE_MATCHER = SimpleBlockMatcher.of(
            ThaumcraftBlocks.ALCHEMICAL_FURNACE
    );
    public static final SimpleBlockMatcher ARCANE_ALEMBIC_MATCHER = SimpleBlockMatcher.of(
            ThaumcraftBlocks.ARCANE_ALEMBIC
    );

}
