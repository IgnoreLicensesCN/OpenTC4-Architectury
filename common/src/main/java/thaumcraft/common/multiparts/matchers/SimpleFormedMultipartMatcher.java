package thaumcraft.common.multiparts.matchers;

import com.linearity.opentc4.VecTransformations;
import com.linearity.opentc4.recipeclean.blockmatch.multipartmatch.IMultipartFormedBlockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.blocks.abstracts.IMultipartComponentBlock;

//I didn't consider about dynamic-sized multipart like mekanism,maybe you need to override match methods and send "null" matchers into constructor args
public class SimpleFormedMultipartMatcher implements IFormedMultipartMatcher {

    //[y][x][z] it seems to be left-handed sorry.
    public final IMultipartFormedBlockMatcher[][][] matchers;
    protected final BlockPos defaultCheckBasePosRelated;

    protected SimpleFormedMultipartMatcher(
            IMultipartFormedBlockMatcher[][][] matchers, BlockPos defaultCheckBasePosRelated) {
        this.matchers = matchers;
        this.defaultCheckBasePosRelated = defaultCheckBasePosRelated;
    }

    public boolean match(@NotNull Level level, BlockPos basePosInWorld, MultipartMatchInfo info) {
        return match(level,basePosInWorld, defaultCheckBasePosRelated,info);
    }
    public boolean match(@NotNull Level level,
                         BlockPos basePosInWorld,
                         BlockPos basePosRelated, MultipartMatchInfo info) {
        return matchWithTransform(level, basePosInWorld, basePosRelated, info);
    }

    @Override
    public void recoverMultipart(Level level,
                                 BlockPos basePosInWorld,
                                 BlockPos basePosRelatedInMultipart,
                                 BlockPos multipartCheckerWorldPos,
                                 MultipartMatchInfo info
    ) {
        var rot3D = info.usingRotation();
        var mirror3D = info.usingMirror();
        for (int y = 0; y < matchers.length; y++) {
            var atY = matchers[y];
            if (atY == null) continue;

            for (int x = 0; x < matchers.length; x++) {
                var atXY = atY[x];
                if (atXY == null) continue;

                for (int z = 0; z < atXY.length; z++) {
                    var matcher = atXY[z];
                    if (matcher == null) continue;

                    BlockPos worldPos = VecTransformations.transform(
                            x, y, z,
                            basePosRelatedInMultipart,
                            basePosInWorld,
                            rot3D,
                            mirror3D
                    );

                    var bState = level.getBlockState(worldPos);
                    if (
                        matcher.match(level,level.getBlockState(worldPos), worldPos,basePosRelatedInMultipart,info)
                                && bState.getBlock() instanceof IMultipartComponentBlock componentBlock
                                && componentBlock.findMultipartCheckerPosRelatedToSelf(level,bState,worldPos).equals(multipartCheckerWorldPos)
                    ) {
                        componentBlock.recoverToOriginalBlock(level,bState,worldPos);
                    }
                }
            }
        }
    }
    protected boolean matchWithTransform(
            Level level,
            BlockPos basePosInWorld,
            BlockPos basePosRelatedInMultipart,
            MultipartMatchInfo info
    ) {
        for (int y = 0; y < matchers.length; y++) {
            var atY = matchers[y];
            if (atY == null) continue;

            for (int x = 0; x < matchers.length; x++) {
                var atXY = atY[x];
                if (atXY == null) continue;

                for (int z = 0; z < atXY.length; z++) {
                    var matcher = atXY[z];
                    if (matcher == null) continue;

                    BlockPos worldPos = VecTransformations.transform(
                            x, y, z,
                            basePosRelatedInMultipart,
                            basePosInWorld,
                            info.usingRotation(),
                            info.usingMirror()
                    );

                    if (!matcher.match(level,level.getBlockState(worldPos),worldPos,basePosRelatedInMultipart,info)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
