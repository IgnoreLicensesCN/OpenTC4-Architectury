package thaumcraft.common.multiparts.placers;

import com.linearity.opentc4.VecTransformations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.multiparts.matchers.MultipartMatchInfo;

public abstract class AbstractMultipartPlacer implements IMultipartPlacer {
    //[y][x][z]
    protected final IBlockPlacer[][][] multipartPlacer;
    protected final BlockPos defaultBlockPosRelated;

    protected AbstractMultipartPlacer(IBlockPlacer[][][] multipartPlacer,BlockPos defaultBlockPosRelated) {
        this.multipartPlacer = multipartPlacer;
        this.defaultBlockPosRelated = defaultBlockPosRelated;
    }

    public void place(@NotNull Level level,
                      @NotNull BlockPos baseInWorld,
                      @NotNull MultipartMatchInfo multipartMatchInfo
    ){
        place(level,defaultBlockPosRelated,baseInWorld, multipartMatchInfo);
    }
    public void place(@NotNull Level level,
                      @NotNull BlockPos basePosRelated,
                      @NotNull BlockPos basePosRelatedInWorld,
                      @NotNull MultipartMatchInfo multipartMatchInfo
    ) {

        VecTransformations.Rotation3D rotation = multipartMatchInfo.usingRotation();
        VecTransformations.Mirror3D mirror = multipartMatchInfo.usingMirror();

        for (int y = 0; y < multipartPlacer.length; y++) {
            IBlockPlacer[][] atY = multipartPlacer[y];
            if (atY == null) continue;

            for (int x = 0; x < atY.length; x++) {
                IBlockPlacer[] atYX = atY[x];
                if (atYX == null) continue;

                for (int z = 0; z < atYX.length; z++) {
                    IBlockPlacer placer = atYX[z];
                    if (placer == null) continue;

                    BlockPos worldPos = VecTransformations.transform(
                            x, y, z,
                            basePosRelated,
                            basePosRelatedInWorld,
                            rotation,
                            mirror
                    );

                    placer.place(level, worldPos, multipartMatchInfo);
                }
            }
        }
    }
}
