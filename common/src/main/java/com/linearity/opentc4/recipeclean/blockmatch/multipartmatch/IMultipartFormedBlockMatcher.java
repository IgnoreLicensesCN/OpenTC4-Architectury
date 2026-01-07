package com.linearity.opentc4.recipeclean.blockmatch.multipartmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.multiparts.matchers.MultipartMatchInfo;

public interface IMultipartFormedBlockMatcher {
    boolean match(@Nullable Level atLevel,
                  @NotNull BlockState state,
                  @NotNull BlockPos posInWorld,
                  @NotNull BlockPos relatedPosInMultipart,
                  @NotNull MultipartMatchInfo multipartMatchInfo
    );
}
