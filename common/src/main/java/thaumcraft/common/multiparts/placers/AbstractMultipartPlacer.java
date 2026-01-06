package thaumcraft.common.multiparts.placers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.multiparts.matchers.AbstractMultipartMatcher;

public abstract class AbstractMultipartPlacer {
    //[y][x][z]
    protected final IBlockPlacer[][][] multipartPlacer;
    protected final BlockPos defaultBlockPosRelated;

    protected AbstractMultipartPlacer(IBlockPlacer[][][] multipartPlacer,BlockPos defaultBlockPosRelated) {
        this.multipartPlacer = multipartPlacer;
        this.defaultBlockPosRelated = defaultBlockPosRelated;
    }

    public void place(@NotNull Level level,
                      @NotNull BlockPos basePosRelatedInWorld,
                      @NotNull AbstractMultipartMatcher.MatchInfo matchInfo){
        place(level,defaultBlockPosRelated,basePosRelatedInWorld,matchInfo);
    }
    public void place(@NotNull Level level,
                      @NotNull BlockPos basePosRelated,
                      @NotNull BlockPos basePosRelatedInWorld,
                      @NotNull AbstractMultipartMatcher.MatchInfo matchInfo) {

        AbstractMultipartMatcher.Rotation3D rotation = matchInfo.usingRotation();
        AbstractMultipartMatcher.Mirror3D mirror = matchInfo.usingMirror();

        for (int y = 0; y < multipartPlacer.length; y++) {
            IBlockPlacer[][] atY = multipartPlacer[y];
            if (atY == null) continue;

            for (int x = 0; x < atY.length; x++) {
                IBlockPlacer[] atYX = atY[x];
                if (atYX == null) continue;

                for (int z = 0; z < atYX.length; z++) {
                    IBlockPlacer placer = atYX[z];
                    if (placer == null) continue;

                    BlockPos worldPos = AbstractMultipartMatcher.transform(
                            x, y, z,
                            basePosRelated,
                            basePosRelatedInWorld,
                            rotation,
                            mirror
                    );

                    placer.place(level, worldPos, matchInfo);
                }
            }
        }
    }
}
