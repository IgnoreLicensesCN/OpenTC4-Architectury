package thaumcraft.common.multiparts.formedmatch;

import com.linearity.opentc4.VecTransformations;
import com.linearity.opentc4.recipeclean.blockmatch.IBlockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.multiparts.MultipartMatchInfo;
import thaumcraft.common.multiparts.constructmatch.IMultipartConstructMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//I didn't consider about dynamic-sized multipart like mekanism,maybe you need to override match methods and send "null" matchers into constructor args
public record SimpleMultipartConstructMatcher(

        //[y][x][z] it seems to be left-handed sorry.
        IBlockMatcher[][][] matchers,
        BlockPos defaultCheckBasePosRelated,
        List<VecTransformations.Rotation3D> allowedRotations,
        List<VecTransformations.Mirror3D> allowedMirrors
) implements IMultipartConstructMatcher {


    public static SimpleMultipartConstructMatcher of(
            IBlockMatcher[][][] matchers, BlockPos defaultCheckBasePosRelated,
            boolean canRotateAroundXAxis, boolean canRotateAroundYAxis, boolean canRotateAroundZAxis,
            boolean canMirrorXZ, boolean canMirrorYZ, boolean canMirrorXY) {

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
        var allowedRotations = Collections.unmodifiableList(rotationsList);


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
        var allowedMirrors = Collections.unmodifiableList(mirrorsList);

        return new SimpleMultipartConstructMatcher(matchers,defaultCheckBasePosRelated,allowedRotations,allowedMirrors);
    }

    public @Nullable MultipartMatchInfo match(@NotNull Level level, BlockPos transformBasePosInWorld) {
        return match(level, transformBasePosInWorld, defaultCheckBasePosRelated);
    }
    public @Nullable MultipartMatchInfo match(@NotNull Level level,
                                              BlockPos transformBasePosInWorld,
                                              BlockPos transformBasePosInMultipart
    ) {

        for (VecTransformations.Rotation3D rotation : allowedRotations) {
            for (VecTransformations.Mirror3D mirror : allowedMirrors) {

                if (matchWithTransform(level, transformBasePosInWorld, transformBasePosInMultipart, rotation, mirror)) {
                    return new MultipartMatchInfo(rotation, mirror);
                }

            }
        }
        return null;
    }

    protected boolean matchWithTransform(
            Level level,
            BlockPos transformBasePosInWorld,
            BlockPos transformBasePosInMultipart,
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
                    BlockPos posRelated = new BlockPos(x,y,z);

                    BlockPos transformedRelatedPos =
                            VecTransformations.transformRelatedPos(
                            posRelated,
                            transformBasePosInMultipart,
                            rotation,
                            mirror
                    );
                    BlockPos worldPos =
                            transformBasePosInWorld
                            .offset(transformBasePosInMultipart.multiply(-1))
                                    .offset(transformedRelatedPos);


                    if (!matcher.match(level,level.getBlockState(worldPos), worldPos)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
