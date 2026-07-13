package com.linearity.opentc4.utils.collectionlike;

import org.jetbrains.annotations.NotNull;

public record ObjectFloatPair<Obj>(Obj left, float rightFloat) implements Comparable<ObjectFloatPair<Obj>>, it.unimi.dsi.fastutil.objects.ObjectFloatPair<Obj> {
    @Override
    public int compareTo(@NotNull ObjectFloatPair<Obj> o) {
        return Float.compare(rightFloat, o.rightFloat);
    }
}
