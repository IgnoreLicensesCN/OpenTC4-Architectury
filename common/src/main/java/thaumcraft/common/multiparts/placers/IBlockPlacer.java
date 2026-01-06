package thaumcraft.common.multiparts.placers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.multiparts.matchers.AbstractMultipartMatcher;

public interface IBlockPlacer {
    void place(@NotNull Level level,@NotNull BlockPos pos, @NotNull AbstractMultipartMatcher.MatchInfo matchInfo);
}
