package com.linearity.opentc4.utils.collectionlike;

import org.jetbrains.annotations.NotNull;

public record ObjectIntPair<Obj>(Obj left, int rightInt) implements it.unimi.dsi.fastutil.objects.ObjectIntPair<Obj>,Comparable<ObjectIntPair<Obj>> {
    @Override
    public int compareTo(@NotNull ObjectIntPair<Obj> o) {
        return Integer.compare(rightInt, o.rightInt);
    }

}
