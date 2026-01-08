package thaumcraft.common.multiparts.formedmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.multiparts.MultipartMatchInfo;

public interface IFormedBlockMatcher {
    boolean match(@Nullable Level atLevel,
                  @NotNull BlockState state,
                  @NotNull BlockPos posInWorld,
                  @NotNull BlockPos transformBasePosRelatedInMultipart,
                  @NotNull BlockPos relatedPosInMultipart,
                  @NotNull MultipartMatchInfo multipartMatchInfo
    );
}
