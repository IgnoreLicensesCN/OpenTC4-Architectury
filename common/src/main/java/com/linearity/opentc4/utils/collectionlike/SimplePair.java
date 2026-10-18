package com.linearity.opentc4.utils.collectionlike;

import java.util.Objects;

public record SimplePair<A,B>(A a, B b) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SimplePair<?, ?> that = (SimplePair<?, ?>) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(a);
        result = 31 * result + Objects.hashCode(b);
        return result;
    }
}
