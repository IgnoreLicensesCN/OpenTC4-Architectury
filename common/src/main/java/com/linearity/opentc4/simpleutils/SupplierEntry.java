package com.linearity.opentc4.simpleutils;

import java.util.Map;
import java.util.function.Supplier;

public class SupplierEntry<A,B> implements Map.Entry<A, B> {
    private final Supplier<A> aspect;
    private final Supplier<B> amount;

    public SupplierEntry(Supplier<A> aspect, Supplier<B> amount) {
        this.aspect = aspect;
        this.amount = amount;
    }


    @Override
    public A getKey() {
        return aspect.get();
    }

    @Override
    public B getValue() {
        return amount.get();
    }

    @Override
    public B setValue(B value) {
        throw new UnsupportedOperationException();
    }

}
