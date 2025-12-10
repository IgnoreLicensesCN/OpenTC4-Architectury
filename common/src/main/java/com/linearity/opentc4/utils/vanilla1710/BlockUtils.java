package com.linearity.opentc4.utils.vanilla1710;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;

public class BlockUtils {
    public static boolean isFoliage(BlockState state) {
        return state.is(BlockTags.LEAVES)
                || state.is(BlockTags.SMALL_FLOWERS)
                || state.is(BlockTags.TALL_FLOWERS)
                || state.is(BlockTags.CROPS);
    }
}
