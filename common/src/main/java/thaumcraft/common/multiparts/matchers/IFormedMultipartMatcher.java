package thaumcraft.common.multiparts.matchers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface IFormedMultipartMatcher {
    void recoverMultipart(Level level,
                          BlockPos basePosInWorld,
                          BlockPos basePosRelated,
                          BlockPos multipartCheckerWorldPos,
                          MultipartMatchInfo info
    );
    boolean match(@NotNull Level level, BlockPos basePosInWorld, MultipartMatchInfo info);
    boolean match(@NotNull Level level,
                                                     BlockPos basePosInWorld,
                                                     BlockPos basePosRelatedInMultipart, MultipartMatchInfo info);
}
