package com.linearity.opentc4.simpleutils;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

public class SingleSupplierPairMap<A,B> extends AbstractMap<A, B> {

    private final Supplier<A> aspect;
    private final Supplier<B> amount;
    private final Set<Entry<A, B>> entrySet;

    public SingleSupplierPairMap(
            Supplier<A> aspect,
            Supplier<B> amount
    ) {
        this.aspect = aspect;
        this.amount = amount;
        this.entrySet = Collections.singleton(new SupplierEntry<>(aspect, amount));
    }

    @Override
    public B get(Object key) {
        A a = aspect.get();
        if (a == null) return null;
        return a.equals(key) ? amount.get() : null;
    }

    @Override
    public boolean containsKey(Object key) {
        A a = aspect.get();
        return a != null && a.equals(key);
    }

    @Override
    public @NotNull Set<Entry<A, B>> entrySet() {
        return entrySet;
    }

    @Override
    public int size() {
        A a = aspect.get();
        B amt = amount.get();
        return (a != null && amt != null) ? 1 : 0;
    }
}
