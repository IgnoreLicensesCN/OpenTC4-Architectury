package com.linearity.opentc4.utils.functionalinterface;

//ObjInt2BiPredicate
@FunctionalInterface
public interface ObjInt2BooleanFunction<T> {
    boolean accept(T t, int i);
}
