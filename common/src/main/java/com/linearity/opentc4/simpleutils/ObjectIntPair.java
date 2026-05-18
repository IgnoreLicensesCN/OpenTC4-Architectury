package com.linearity.opentc4.simpleutils;

import org.jetbrains.annotations.NotNull;

public record ObjectIntPair<Obj>(Obj obj, int value) implements Comparable<ObjectIntPair<Obj>>{
    @Override
    public int compareTo(@NotNull ObjectIntPair<Obj> o) {
        return Integer.compare(value, o.value);
    }
}
