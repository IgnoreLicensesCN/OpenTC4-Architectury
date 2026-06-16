package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import java.util.Objects;

public class AllowOrderExchangeCalculationCacheKey<Obj,S extends CalcCacheableCollection<Obj,S>> extends CalculationCacheKey<Obj,S> {

    public AllowOrderExchangeCalculationCacheKey(CalcCacheableCollection<Obj,S> a, CalcCacheableCollection<Obj,S> b) {
        super(a,b);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AllowOrderExchangeCalculationCacheKey<?,?> that = (AllowOrderExchangeCalculationCacheKey<?,?>) o;
        return (Objects.equals(a, that.a) && Objects.equals(b, that.b)) || (Objects.equals(a, that.b) && Objects.equals(b, that.a));
    }

    @Override
    public int hashCode() {
        return Objects.hash(a) + Objects.hash(b);
    }
}
