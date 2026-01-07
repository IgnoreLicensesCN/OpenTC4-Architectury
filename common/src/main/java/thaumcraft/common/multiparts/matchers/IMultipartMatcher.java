package thaumcraft.common.multiparts.matchers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMultipartMatcher {
    void recoverMultipart(Level level, BlockPos basePosInWorld, BlockPos basePosRelated, BlockPos multipartCheckerWorldPos, MultipartMatchInfo info);
    @Nullable MultipartMatchInfo match(@NotNull Level level, BlockPos basePosInWorld);
    @Nullable MultipartMatchInfo match(@NotNull Level level,
                                       BlockPos basePosInWorld,
                                       BlockPos basePosRelated);

}
