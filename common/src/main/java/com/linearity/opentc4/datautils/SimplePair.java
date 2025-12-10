package com.linearity.opentc4.datautils;

import java.util.Objects;

public record SimplePair<A,B>(A a, B b) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SimplePair<?, ?> that)) return false;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
