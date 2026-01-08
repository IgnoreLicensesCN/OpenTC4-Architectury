package thaumcraft.common.multiparts.placers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.multiparts.MultipartMatchInfo;

public interface IMultipartPlacer {
    void place(@NotNull Level level,
                      @NotNull BlockPos transformBaseInWorld,
                      @NotNull MultipartMatchInfo multipartMatchInfo
    );
    void place(@NotNull Level level,
                      @NotNull BlockPos transformBaseInMultipart,
                      @NotNull BlockPos transformBaseInWorld,
                      @NotNull MultipartMatchInfo multipartMatchInfo
    );
}
