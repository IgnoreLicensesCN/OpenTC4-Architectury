package thaumcraft.common.multiparts.placers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.multiparts.MultipartMatchInfo;

public interface IBlockPlacer {
    void place(@NotNull Level placeAtLevel,@NotNull BlockPos placeAtWorldPos, @NotNull MultipartMatchInfo multipartMatchInfo);
}
