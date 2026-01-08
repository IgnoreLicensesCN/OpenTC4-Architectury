package com.linearity.opentc4.simpleutils;

import java.util.Objects;

public record SimpleTri<A,B,C>(A a, B b, C c) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SimpleTri<?, ?, ?> simpleTri)) return false;
        return Objects.equals(a, simpleTri.a) && Objects.equals(
                b, simpleTri.b) && Objects.equals(c, simpleTri.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }
}
