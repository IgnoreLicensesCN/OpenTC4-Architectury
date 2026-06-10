package com.linearity.opentc4.simpleutils;

import org.jetbrains.annotations.NotNull;

public record ObjectFloatPair<Obj>(Obj obj, float value) implements Comparable<ObjectFloatPair<Obj>>{
    @Override
    public int compareTo(@NotNull ObjectFloatPair<Obj> o) {
        return Float.compare(value, o.value);
    }
}
