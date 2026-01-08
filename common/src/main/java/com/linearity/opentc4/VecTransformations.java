package com.linearity.opentc4;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;

public class VecTransformations {

    @Contract(pure = true)
    public static Direction rotate(Direction dir, Rotation3D r) {
        if (r == Rotation3D.NONE) return dir;

        Vec3i vec = dir.getNormal(); // e.g. NORTH = (0,0,-1)
        Vec3i rotated = rotate(vec, r);

        // 向量 → Direction
        return Direction.fromDelta(
                rotated.getX(),
                rotated.getY(),
                rotated.getZ()
        );
    }

    @Contract(pure = true)
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

    @Contract(pure = true)
    public static Vec3i rotate(Vec3i p, Rotation3D r) {
        int x = p.getX();
        int y = p.getY();
        int z = p.getZ();

        return switch (r) {
            case NONE -> p;

            case X_90  -> new Vec3i( x, -z,  y);
            case X_180 -> new Vec3i( x, -y, -z);
            case X_270 -> new Vec3i( x,  z, -y);

            case Y_90  -> new Vec3i( z,  y, -x);
            case Y_180 -> new Vec3i(-x,  y, -z);
            case Y_270 -> new Vec3i(-z,  y,  x);

            case Z_90  -> new Vec3i(-y,  x,  z);
            case Z_180 -> new Vec3i(-x, -y,  z);
            case Z_270 -> new Vec3i( y, -x,  z);
        };
    }

    @Contract(pure = true)
    public static Vec3 rotate(Vec3 p, Rotation3D r) {
        double x = p.x();
        double y = p.y();
        double z = p.z();

        return switch (r) {
            case NONE -> p;

            case X_90  -> new Vec3( x, -z,  y);
            case X_180 -> new Vec3( x, -y, -z);
            case X_270 -> new Vec3( x,  z, -y);

            case Y_90  -> new Vec3( z,  y, -x);
            case Y_180 -> new Vec3(-x,  y, -z);
            case Y_270 -> new Vec3(-z,  y,  x);

            case Z_90  -> new Vec3(-y,  x,  z);
            case Z_180 -> new Vec3(-x, -y,  z);
            case Z_270 -> new Vec3( y, -x,  z);
        };
    }

    @Contract(pure = true)
    public static BlockPos mirror(BlockPos p, Mirror3D m) {
        return switch (m) {
            case NONE -> p;
            case XY -> new BlockPos( p.getX(),  p.getY(), -p.getZ());
            case XZ -> new BlockPos( p.getX(), -p.getY(),  p.getZ());
            case YZ -> new BlockPos(-p.getX(),  p.getY(),  p.getZ());
        };
    }

    @Contract(pure = true)
    public static Vec3i mirror(Vec3i p, Mirror3D m) {
        return switch (m) {
            case NONE -> p;
            case XY -> new Vec3i( p.getX(),  p.getY(), -p.getZ());
            case XZ -> new Vec3i( p.getX(), -p.getY(),  p.getZ());
            case YZ -> new Vec3i(-p.getX(),  p.getY(),  p.getZ());
        };
    }

    @Contract(pure = true)
    public static Vec3 mirror(Vec3 p, Mirror3D m) {
        return switch (m) {
            case NONE -> p;
            case XY -> new Vec3( p.x(),  p.y(), -p.z());
            case XZ -> new Vec3( p.x(), -p.y(),  p.z());
            case YZ -> new Vec3(-p.x(),  p.y(),  p.z());
        };
    }

    @Contract(pure = true)
    public static BlockPos transformRelatedPos(
            BlockPos selfRelatedPosInMultipart,
            BlockPos transformBasePosRelated,
            Rotation3D rotation,
            Mirror3D mirror
    ) {
        var relative = selfRelatedPosInMultipart.offset(transformBasePosRelated.multiply(-1));
        return mirror(rotate(relative, rotation), mirror).offset(transformBasePosRelated);
    }

    @Contract(pure = true)
    public static Vec3i transformRelatedPos(
            Vec3i selfRelatedPosInMultipart,
            Vec3i transformBasePosRelated,
            Rotation3D rotation,
            Mirror3D mirror
    ) {
        var relative = selfRelatedPosInMultipart.offset(transformBasePosRelated.multiply(-1));
        return mirror(rotate(relative, rotation), mirror).offset(transformBasePosRelated);
    }


    @Contract(pure = true)
    public static Vec3 transformRelatedPos(
            Vec3 selfRelatedPosInMultipart,
            Vec3 transformBasePosRelated,
            Rotation3D rotation,
            Mirror3D mirror
    ) {
        var relative = selfRelatedPosInMultipart.subtract(transformBasePosRelated);
        return mirror(rotate(relative, rotation), mirror).add(transformBasePosRelated);
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
}
