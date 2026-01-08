package thaumcraft.common.multiparts.formedmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.multiparts.MultipartMatchInfo;

public interface IFormedMultipartMatcher {
    void destroyMultipart(Level level,
                          BlockPos selfWorldPos,
                          BlockPos selfBasePosRelatedInMultipart,
                          BlockPos transformBasePosInWorld,
                          BlockPos transformBasePosInMultipart,
                          MultipartMatchInfo info
    );
    boolean match(@NotNull Level level,
                  BlockPos selfPosInWorld,
                  BlockPos selfPosRelatedInMultipart,
                  BlockPos transformBasePosRelatedInMultipart,
                  MultipartMatchInfo info
    );
    boolean matchNearbyBlocksAndSelf(
            Level level,
            BlockPos selfPosInWorld,
            BlockPos selfPosRelatedInMultipart,
            BlockPos transformBasePosRelatedInMultipart,
            MultipartMatchInfo info);
}
