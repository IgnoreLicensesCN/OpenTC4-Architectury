package com.linearity.opentc4.utils.collectionlike;

import java.util.Objects;

public record SimplePair<A,B>(A a, B b) {
    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
