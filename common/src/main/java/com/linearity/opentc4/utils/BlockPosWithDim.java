package com.linearity.opentc4.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record BlockPosWithDim(ResourceLocation dim, BlockPos pos) implements Comparable<BlockPosWithDim> {
    @Override
    public int compareTo(@NotNull BlockPosWithDim o) {
        var dimCompare = dim.compareTo(o.dim);
        var posComp = pos.compareTo(o.pos);

        return dimCompare != 0? dimCompare : posComp;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlockPosWithDim(ResourceLocation dim1, BlockPos pos1))) return false;
        return Objects.equals(pos, pos1) && Objects.equals(dim, dim1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dim, pos);
    }

    @Override
    public String toString() {
        return dim + ":" + pos.getX() + ":" + pos.getY() + ":" + pos.getZ();
    }

}
