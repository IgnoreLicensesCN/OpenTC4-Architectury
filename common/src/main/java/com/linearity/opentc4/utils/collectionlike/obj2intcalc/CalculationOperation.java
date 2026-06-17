package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Contract;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
//A operator B
//(A+B,A-B,....)
public abstract class CalculationOperation {
    protected final Map<CalculationCacheKey<?,?>,CalcCacheableObject2IntMap<?>> calculationResults = new ConcurrentHashMap<>();
    @Contract(pure = true)
    protected abstract <ObjCollection,S extends CalcCacheableCollection<ObjCollection,S>> CalculationCacheKey<ObjCollection,S> getCalculationCacheKey(
            S a,
            S b
    );
    @Contract(pure = true)
    public abstract <ObjCollection,S extends CalcCacheableCollection<ObjCollection,S>> S calculateWithCache(
            S a,
            S b,
            Supplier<ObjCollection> newMapSupplier
    );
}
