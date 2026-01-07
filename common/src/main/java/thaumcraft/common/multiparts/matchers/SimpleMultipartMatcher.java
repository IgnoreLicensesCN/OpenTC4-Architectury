package thaumcraft.common.multiparts.matchers;

import com.linearity.opentc4.VecTransformations;
import com.linearity.opentc4.recipeclean.blockmatch.IBlockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.blocks.abstracts.IMultipartComponentBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//I didn't consider about dynamic-sized multipart like mekanism,maybe you need to override match methods and send "null" matchers into constructor args
public class SimpleMultipartMatcher implements IMultipartMatcher {

    //[y][x][z] it seems to be left-handed sorry.
    public final IBlockMatcher[][][] matchers;
    protected final BlockPos defaultCheckBasePosRelated;
    protected final List<VecTransformations.Rotation3D> allowedRotations;
    protected final List<VecTransformations.Mirror3D> allowedMirrors;

    protected SimpleMultipartMatcher(
            IBlockMatcher[][][] matchers, BlockPos defaultCheckBasePosRelated,
            boolean canRotateAroundXAxis, boolean canRotateAroundYAxis, boolean canRotateAroundZAxis,
            boolean canMirrorXZ, boolean canMirrorYZ, boolean canMirrorXY) {
        this.matchers = matchers;
        this.defaultCheckBasePosRelated = defaultCheckBasePosRelated;
        {
            List<VecTransformations.Rotation3D> rotationsList = new ArrayList<>();
            rotationsList.add(VecTransformations.Rotation3D.NONE);

            if (canRotateAroundXAxis) {
                rotationsList.add(VecTransformations.Rotation3D.X_90);
                rotationsList.add(VecTransformations.Rotation3D.X_180);
                rotationsList.add(VecTransformations.Rotation3D.X_270);
            }
            if (canRotateAroundYAxis) {
                rotationsList.add(VecTransformations.Rotation3D.Y_90);
                rotationsList.add(VecTransformations.Rotation3D.Y_180);
                rotationsList.add(VecTransformations.Rotation3D.Y_270);
            }
            if (canRotateAroundZAxis) {
                rotationsList.add(VecTransformations.Rotation3D.Z_90);
                rotationsList.add(VecTransformations.Rotation3D.Z_180);
                rotationsList.add(VecTransformations.Rotation3D.Z_270);
            }
            allowedRotations = Collections.unmodifiableList(rotationsList);
        }
        {
            List<VecTransformations.Mirror3D> mirrorsList = new ArrayList<>();
            mirrorsList.add(VecTransformations.Mirror3D.NONE);
            if (canMirrorXZ) {
                mirrorsList.add(VecTransformations.Mirror3D.XZ);
            }
            if (canMirrorYZ) {
                mirrorsList.add(VecTransformations.Mirror3D.YZ);
            }
            if (canMirrorXY) {
                mirrorsList.add(VecTransformations.Mirror3D.XY);
            }
            allowedMirrors = Collections.unmodifiableList(mirrorsList);
        }
    }

    public @Nullable MultipartMatchInfo match(@NotNull Level level, BlockPos basePosInWorld) {
        return match(level,basePosInWorld, defaultCheckBasePosRelated);
    }
    public @Nullable MultipartMatchInfo match(@NotNull Level level,
                                              BlockPos basePosInWorld,
                                              BlockPos basePosRelated) {

        for (VecTransformations.Rotation3D rotation : allowedRotations) {
            for (VecTransformations.Mirror3D mirror : allowedMirrors) {

                if (matchWithTransform(level, basePosInWorld, basePosRelated, rotation, mirror)) {
                    return new MultipartMatchInfo(rotation, mirror);
                }

            }
        }
        return null;
    }

    @Override
    public void recoverMultipart(Level level, BlockPos basePosInWorld, BlockPos basePosRelated, BlockPos multipartCheckerWorldPos, MultipartMatchInfo info) {
        var rot3D = info.usingRotation();
        var mirror3D = info.usingMirror();
        for (int y = 0; y < matchers.length; y++) {
            IBlockMatcher[][] atY = matchers[y];
            if (atY == null) continue;

            for (int x = 0; x < matchers.length; x++) {
                IBlockMatcher[] atXY = atY[x];
                if (atXY == null) continue;

                for (int z = 0; z < atXY.length; z++) {
                    IBlockMatcher matcher = atXY[z];
                    if (matcher == null) continue;

                    BlockPos worldPos = VecTransformations.transform(
                            x, y, z,
                            basePosRelated,
                            basePosInWorld,
                            rot3D,
                            mirror3D
                    );

                    var bState = level.getBlockState(worldPos);
                    if (matcher.match(level,bState, worldPos)
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
            BlockPos basePosRelatedInWorld,
            BlockPos basePosRelated,
            VecTransformations.Rotation3D rotation,
            VecTransformations.Mirror3D mirror
    ) {
        for (int y = 0; y < matchers.length; y++) {
            IBlockMatcher[][] atY = matchers[y];
            if (atY == null) continue;

            for (int x = 0; x < matchers.length; x++) {
                IBlockMatcher[] atXY = atY[x];
                if (atXY == null) continue;

                for (int z = 0; z < atXY.length; z++) {
                    IBlockMatcher matcher = atXY[z];
                    if (matcher == null) continue;

                    BlockPos worldPos = VecTransformations.transform(
                            x, y, z,
                            basePosRelated,
                            basePosRelatedInWorld,
                            rotation,
                            mirror
                    );

                    if (!matcher.match(level,level.getBlockState(worldPos), worldPos)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
