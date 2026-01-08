package thaumcraft.common.multiparts.constructmatch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.multiparts.MultipartMatchInfo;

public interface IMultipartConstructMatcher {
    @Nullable MultipartMatchInfo match(@NotNull Level level,
                                       BlockPos transformBasePosInWorld);
    @Nullable MultipartMatchInfo match(@NotNull Level level,
                                       BlockPos transformBasePosInWorld,
                                       BlockPos transformBasePosInMultipart);

}
