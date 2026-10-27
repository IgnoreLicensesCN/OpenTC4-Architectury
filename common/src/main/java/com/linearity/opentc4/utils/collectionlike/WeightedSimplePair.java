package com.linearity.opentc4.utils.collectionlike;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class WeightedSimplePair<A, B> implements Comparable<WeightedSimplePair<A, B>>{
    private final A a;
    private final B b;
    private final int weight;

    public WeightedSimplePair(A a, B b, int weight) {
        this.a = a;
        this.b = b;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        WeightedSimplePair<?, ?> that = (WeightedSimplePair<?, ?>) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b) && weight == that.weight;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(a);
        result = 31 * result + Objects.hashCode(b);
        result = 31 * result + weight;
        return result;
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }

    @Override
    public String toString() {
        return "WeightedSimplePair[" +
                "a=" + a
                + ", b=" + b
                + ", weight=" + weight
                +']';
    }

    @Override
    public int compareTo(@NotNull WeightedSimplePair<A, B> o) {
        return Integer.compare(weight, o.weight);
    }

    public static int compare(WeightedSimplePair<?, ?> a, WeightedSimplePair<?, ?> b) {
        return Integer.compare(a.weight, b.weight);
    }
}
