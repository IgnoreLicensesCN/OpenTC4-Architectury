package com.linearity.opentc4;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class VecTransformations {
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

    public static BlockPos mirror(BlockPos p, Mirror3D m) {
        return switch (m) {
            case NONE -> p;
            case XY -> new BlockPos( p.getX(),  p.getY(), -p.getZ());
            case XZ -> new BlockPos( p.getX(), -p.getY(),  p.getZ());
            case YZ -> new BlockPos(-p.getX(),  p.getY(),  p.getZ());
        };
    }
    public static Vec3i mirror(Vec3i p, Mirror3D m) {
        return switch (m) {
            case NONE -> p;
            case XY -> new Vec3i( p.getX(),  p.getY(), -p.getZ());
            case XZ -> new Vec3i( p.getX(), -p.getY(),  p.getZ());
            case YZ -> new Vec3i(-p.getX(),  p.getY(),  p.getZ());
        };
    }
    public static Vec3 mirror(Vec3 p, Mirror3D m) {
        return switch (m) {
            case NONE -> p;
            case XY -> new Vec3( p.x(),  p.y(), -p.z());
            case XZ -> new Vec3( p.x(), -p.y(),  p.z());
            case YZ -> new Vec3(-p.x(),  p.y(),  p.z());
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

    public static Vec3i transform(
            int x, int y, int z,
            Vec3i basePosRelated,
            Vec3i basePosRelatedInWorld,
            Rotation3D rotation,
            Mirror3D mirror
    ) {
        // 1. 结构坐标 → 相对基准
        Vec3i relative = new Vec3i(
                x - basePosRelated.getX(),
                y - basePosRelated.getY(),
                z - basePosRelated.getZ()
        );

        // 2. 旋转
        Vec3i rotated = rotate(relative, rotation);

        // 3. 镜像
        Vec3i mirrored = mirror(rotated, mirror);

        // 4. 世界坐标
        return basePosRelatedInWorld.offset(mirrored);
    }

    public static Vec3 transform(
            double x, double y, double z,
            Vec3 basePosRelated,
            Vec3 basePosRelatedInWorld,
            Rotation3D rotation,
            Mirror3D mirror
    ) {
        // 1. 结构坐标 → 相对基准
        Vec3 relative = new Vec3(
                x - basePosRelated.x(),
                y - basePosRelated.y(),
                z - basePosRelated.z()
        );

        // 2. 旋转
        Vec3 rotated = rotate(relative, rotation);

        // 3. 镜像
        Vec3 mirrored = mirror(rotated, mirror);

        // 4. 世界坐标
        return basePosRelatedInWorld.add(mirrored);
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
