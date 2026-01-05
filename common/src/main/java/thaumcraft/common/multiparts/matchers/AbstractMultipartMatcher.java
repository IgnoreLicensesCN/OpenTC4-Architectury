package thaumcraft.common.multiparts.matchers;

import com.linearity.opentc4.recipeclean.blockmatch.AbstractBlockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractMultipartMatcher {

    public record MatchInfo(Rotation3D usingRotation, Mirror3D usingMirror) {}

    //[y][x][z]
    protected final AbstractBlockMatcher[][][] matchers;
    protected final BlockPos defaultCheckBasePosRelated;
    protected final List<Rotation3D> allowedRotations;
    protected final List<Mirror3D> allowedMirrors;

    protected AbstractMultipartMatcher(AbstractBlockMatcher[][][] matchers, BlockPos defaultCheckBasePosRelated,
                                       boolean canRotateAroundXAxis, boolean canRotateAroundYAxis, boolean canRotateAroundZAxis,
                                       boolean canMirrorXZ, boolean canMirrorYZ, boolean canMirrorXY) {
        this.matchers = matchers;
        this.defaultCheckBasePosRelated = defaultCheckBasePosRelated;
        {
            List<Rotation3D> rotationsList = new ArrayList<>();
            rotationsList.add(Rotation3D.NONE);

            if (canRotateAroundXAxis) {
                rotationsList.add(Rotation3D.X_90);
                rotationsList.add(Rotation3D.X_180);
                rotationsList.add(Rotation3D.X_270);
            }
            if (canRotateAroundYAxis) {
                rotationsList.add(Rotation3D.Y_90);
                rotationsList.add(Rotation3D.Y_180);
                rotationsList.add(Rotation3D.Y_270);
            }
            if (canRotateAroundZAxis) {
                rotationsList.add(Rotation3D.Z_90);
                rotationsList.add(Rotation3D.Z_180);
                rotationsList.add(Rotation3D.Z_270);
            }
            allowedRotations = Collections.unmodifiableList(rotationsList);
        }
        {
            List<Mirror3D> mirrorsList = new ArrayList<>();
            mirrorsList.add(Mirror3D.NONE);
            if (canMirrorXZ) {
                mirrorsList.add(Mirror3D.XZ);
            }
            if (canMirrorYZ) {
                mirrorsList.add(Mirror3D.YZ);
            }
            if (canMirrorXY) {
                mirrorsList.add(Mirror3D.XY);
            }
            allowedMirrors = Collections.unmodifiableList(mirrorsList);
        }
    }

    public @Nullable MatchInfo match(@NotNull Level level,BlockPos basePosInWorld) {
        return match(level,basePosInWorld, defaultCheckBasePosRelated);
    }
    public @Nullable MatchInfo match(@NotNull Level level,
                           BlockPos basePosRelatedInWorld,
                           BlockPos basePosRelated) {

        for (Rotation3D rotation : allowedRotations) {
            for (Mirror3D mirror : allowedMirrors) {

                if (matchWithTransform(level, basePosRelatedInWorld, basePosRelated, rotation, mirror)) {
                    return new MatchInfo(rotation, mirror);
                }

            }
        }
        return null;
    }
    protected boolean matchWithTransform(
            Level level,
            BlockPos basePosRelatedInWorld,
            BlockPos basePosRelated,
            Rotation3D rotation,
            Mirror3D mirror
    ) {
        for (int y = 0; y < matchers.length; y++) {
            AbstractBlockMatcher[][] atY = matchers[y];
            if (atY == null) continue;

            for (int x = 0; x < matchers.length; x++) {
                AbstractBlockMatcher[] atXY = atY[y];
                if (atXY == null) continue;

                for (int z = 0; z < atXY.length; z++) {
                    AbstractBlockMatcher matcher = atXY[z];
                    if (matcher == null) continue;

                    BlockPos worldPos = transform(
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



    public enum Rotation3D {
        NONE,
        X_90, X_180, X_270,
        Y_90, Y_180, Y_270,
        Z_90, Z_180, Z_270;
    }
    public enum Mirror3D {
        NONE,
        XY, // 翻转 Z
        XZ, // 翻转 Y
        YZ; // 翻转 X
    }
    public static BlockPos rotate(BlockPos p, Rotation3D r) {
        int x = p.getX();
        int y = p.getY();
        int z = p.getZ();

        return switch (r) {
            case NONE -> p;

            case X_90  -> new BlockPos( x, -z,  y);
            case X_180 -> new BlockPos( x, -y, -z);
            case X_270 -> new BlockPos( x,  z, -y);

            case Y_90  -> new BlockPos( z,  y, -x);
            case Y_180 -> new BlockPos(-x,  y, -z);
            case Y_270 -> new BlockPos(-z,  y,  x);

            case Z_90  -> new BlockPos(-y,  x,  z);
            case Z_180 -> new BlockPos(-x, -y,  z);
            case Z_270 -> new BlockPos( y, -x,  z);
        };
    }
    public static BlockPos mirror(BlockPos p, Mirror3D m) {
        return switch (m) {
            case NONE -> p;
            case XY -> new BlockPos( p.getX(),  p.getY(), -p.getZ());
            case XZ -> new BlockPos( p.getX(), -p.getY(),  p.getZ());
            case YZ -> new BlockPos(-p.getX(),  p.getY(),  p.getZ());
        };
    }

    public static BlockPos transform(
            int x, int y, int z,
            BlockPos basePosRelated,
            BlockPos basePosRelatedInWorld,
            Rotation3D rotation,
            Mirror3D mirror
    ) {
        // 1. 结构坐标 → 相对基准
        BlockPos relative = new BlockPos(
                x - basePosRelated.getX(),
                y - basePosRelated.getY(),
                z - basePosRelated.getZ()
        );

        // 2. 旋转
        BlockPos rotated = rotate(relative, rotation);

        // 3. 镜像
        BlockPos mirrored = mirror(rotated, mirror);

        // 4. 世界坐标
        return basePosRelatedInWorld.offset(mirrored);
    }

}
