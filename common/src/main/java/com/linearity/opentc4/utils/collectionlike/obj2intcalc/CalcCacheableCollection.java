package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;

public abstract class CalcCacheableCollection<ObjCollection,SelfClass extends CalcCacheableCollection<ObjCollection,SelfClass>> {

    @Contract(pure = true)
    public abstract @Unmodifiable @NotNull ObjCollection getWrapped();//the value should be considered as(do not modify it)
    @Contract(pure = true)
    public abstract boolean getInputIsSingleton();//the value in could be modified,determine how we cache the result
    @Contract(pure = true)
    protected abstract @NotNull SelfClass getSingletonPart();
    @Contract(pure = true)
    protected abstract @Unmodifiable @NotNull ObjCollection getConsideredNotSingletonPart();
    @Contract(pure = true)
    public abstract SelfClass newForCalculatedResult(
            ObjCollection wrapped,
            SelfClass singletonPart,
            ObjCollection consideredNotSingletonPart
    );
    @Contract(pure = true)
    public abstract SelfClass newForCalculatedResult(
            ObjCollection wrapped,
            boolean isSingleton
    );
    @Contract(pure = true)
    public abstract boolean isCollectionEmpty(ObjCollection collection);
    @Contract(pure = true)
    public abstract ObjCollection operateEachValue(ObjCollection a,ObjCollection b, Supplier<ObjCollection> newMapSupplier, IntBinaryOperator oper);
}
