package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import org.apache.commons.lang3.function.TriFunction;

import java.util.Map;
import java.util.function.Supplier;

public class CalculationOperationAdd extends CalculationOperation {
    public static final CalculationOperationAdd INSTANCE = new CalculationOperationAdd();
    private CalculationOperationAdd(){}

    @Override
    protected <O,S extends CalcCacheableCollection<O,S>> CalculationCacheKey<O,S> getCalculationCacheKey(
            S a,
            S b
    ) {
        return new AllowOrderExchangeCalculationCacheKey<>(a,b);
    }

    @Override
    public <O,S extends CalcCacheableCollection<O,S>> S calculateWithCache(
            S a,
            S b,
            Supplier<O> newMapSupplier
    ){
        //in multiplication there might not be so fine as we did here
        //in that case we may have to (aSingleton + aNotSingleton) * (bSingleton + bNotSingleton)
        // = (aSingleton * bSingleton) + (aSingleton * bNotSingleton)  + (bSingleton * aNotSingleton) + (aNotSingleton * bNotSingleton).
        //but here we have only (aSingleton + bSingleton) + (aNotSingleton + bNotSingleton)
        if (a.isCollectionEmpty(a.getWrapped())){
            return b;
        }
        if (b.isCollectionEmpty(b.getWrapped())){
            return a;
        }

        if (a.getInputIsSingleton() && b.getInputIsSingleton()){
            var cacheKey = getCalculationCacheKey(a,b);
            return ((Map<CalculationCacheKey<O,S>,S>)
                    (Object)
                            calculationResults).computeIfAbsent(
                    cacheKey,
                    cachedKey -> a.newForCalculatedResult(
                            a.operateEachValue(
                                    cachedKey.a.getWrapped(),
                                    cachedKey.b.getWrapped(),
                                    newMapSupplier,
                                    Integer::sum
                            ),
                            true
                    )
            );
        }
        S singletonPart;
        if (a.getInputIsSingleton()){
            singletonPart = calculateWithCache(a,b.getSingletonPart(),newMapSupplier);
        } else if (b.getInputIsSingleton()){
            singletonPart = calculateWithCache(a.getSingletonPart(),b,newMapSupplier);
        } else {
            singletonPart = calculateWithCache(a,b,newMapSupplier);
        }
        O notSingletonPart;
        if (a.getInputIsSingleton()){
            notSingletonPart = b.getConsideredNotSingletonPart();
        }else if (b.getInputIsSingleton()){
            notSingletonPart = a.getConsideredNotSingletonPart();
        }else  {
            notSingletonPart = a.operateEachValue(
                    a.getConsideredNotSingletonPart(),b.getConsideredNotSingletonPart(),newMapSupplier,Integer::sum
            );
        }
        if (a.isCollectionEmpty(notSingletonPart)){
            return singletonPart;
        }
        O wrapped = a.operateEachValue(singletonPart.getWrapped(),notSingletonPart, newMapSupplier,Integer::sum);
        return a.newForCalculatedResult(
                wrapped,
                singletonPart,
                notSingletonPart

        );
    }
}
