package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import java.util.Objects;

public class CalculationCacheKey<Obj,S extends CalcCacheableCollection<Obj,S>> {
    public final CalcCacheableCollection<Obj,S> a;
    public final CalcCacheableCollection<Obj,S> b;

    public CalculationCacheKey(CalcCacheableCollection<Obj,S> a, CalcCacheableCollection<Obj,S> b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CalculationCacheKey<?,?> that = (CalculationCacheKey<?,?>) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
