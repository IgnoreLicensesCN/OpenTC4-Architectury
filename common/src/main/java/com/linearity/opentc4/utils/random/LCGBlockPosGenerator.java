package com.linearity.opentc4.utils.random;

import net.minecraft.core.BlockPos;

public class LCGBlockPosGenerator {
    protected EasyLCGGenerator lcg;
    protected final int xRange;
    protected final int yRange;
    protected final int zRange;
    //get result from here
    public final BlockPos.MutableBlockPos resultCache = new BlockPos.MutableBlockPos();

    public LCGBlockPosGenerator(int xRange, int yRange, int zRange) {
        if (xRange <= 0 || yRange <= 0 || zRange <= 0) {
            throw new IllegalArgumentException("xRange, yRange and zRange must be greater than 0");
        }
        if (((Integer.MAX_VALUE / xRange) / yRange) / zRange == 0) {
            throw new IllegalArgumentException("Range too large caused integer overflow");
        }
        this.xRange = xRange;
        this.yRange = yRange;
        this.zRange = zRange;
        lcg = new EasyLCGGenerator((2 * xRange + 1) * (2 * yRange + 1) * (2 * zRange + 1));
    }

    public void moveToNextBlockPos() {
        int xyz = lcg.next();
        int x = xyz % (2 * xRange + 1);
        int yz = xyz / (2 * xRange + 1);
        int y = yz % (2 * yRange + 1);
        int z = yz / (2 * yRange + 1);
        resultCache.set(x - xRange, y - yRange, z - yRange);
    }
}
