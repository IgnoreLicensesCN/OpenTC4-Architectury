package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;

//if we always create new map for #add we will get lots of maps
public class CalcCacheableObject2IntMap<Obj>
        extends CalcCacheableCollection<
        Object2IntMap<Obj>,
        CalcCacheableObject2IntMap<Obj>
        > {
    public static final CalcCacheableObject2IntMap<?> EMPTY = new CalcCacheableObject2IntMap<>(
            Object2IntMaps.emptyMap(),
            true
    ){
        @Override
        public CalcCacheableObject2IntMap<Object> add(CalcCacheableObject2IntMap<Object> another, Supplier<Object2IntMap<Object>> newMapSupplier) {
            return another;
        }
    };
    public static <O> CalcCacheableObject2IntMap<O> empty() {
        return (CalcCacheableObject2IntMap<O>) EMPTY;
    }
    public @Unmodifiable @NotNull Object2IntMap<Obj> wrapped;//the value should be considered as(do not modify it)
    public final boolean inputIsSingleton;//the value in could be modified,determine how we cache the result

    protected @NotNull CalcCacheableObject2IntMap<Obj> singletonPart = empty();
    protected @Unmodifiable @NotNull Object2IntMap<Obj> consideredNotSingletonPart = Object2IntMaps.emptyMap();

    @Override
    public @Unmodifiable @NotNull Object2IntMap<Obj> getWrapped() {
        return wrapped;
    }

    @Override
    public boolean getInputIsSingleton() {
        return inputIsSingleton;
    }

    @Override
    public @NotNull CalcCacheableObject2IntMap<Obj> getSingletonPart() {
        if (inputIsSingleton) {return this;}
        return singletonPart;
    }

    @Override
    public @Unmodifiable @NotNull Object2IntMap<Obj> getConsideredNotSingletonPart() {
        return consideredNotSingletonPart;
    }
    public CalcCacheableObject2IntMap(
            @NotNull @Unmodifiable /*input should be unmodifiable*/ Object2IntMap<Obj> wrapped,
            boolean inputIsSingleton //considered as modified,if false "wrapped" you've input should be singleton
            //e.g. a ring provides runic shield in a const map,so we set couldBeModified false then there wont be lots of maps with same value cached
            //each map is considered as a single instance
    ) {
        this.wrapped = wrapped;
        this.inputIsSingleton = inputIsSingleton;
        if (!this.inputIsSingleton) {
            consideredNotSingletonPart = wrapped;
        }
    }

    @Override
    public CalcCacheableObject2IntMap<Obj> newForCalculatedResult(
            Object2IntMap<Obj> wrapped,
            CalcCacheableObject2IntMap<Obj> singletonPart,
            Object2IntMap<Obj> consideredNotSingletonPart
    ) {
        return new CalcCacheableObject2IntMap<>(
                wrapped,
                singletonPart,
                consideredNotSingletonPart
        );
    }

    protected CalcCacheableObject2IntMap(
            @NotNull Object2IntMap<Obj> wrapped,
            @NotNull CalcCacheableObject2IntMap<Obj> singletonPart,//should be generated from a unmodifiablePart using "operation"
            @NotNull Object2IntMap<Obj> consideredModifiablePart
    ) {
        this.wrapped = wrapped;
        this.inputIsSingleton = consideredModifiablePart.isEmpty();
        this.singletonPart = singletonPart;
        this.consideredNotSingletonPart = consideredModifiablePart;
    }
    public CalcCacheableObject2IntMap<Obj> add(CalcCacheableObject2IntMap<Obj> another, Supplier<Object2IntMap<Obj>> newMapSupplier){
        return CalculationOperationAdd.INSTANCE.calculateWithCache(this,another,newMapSupplier);
    }
    public static <Obj> Object2IntMap<Obj> addObject2IntMapAsNew(
            Object2IntMap<Obj> a,
            Object2IntMap<Obj> b,
            Supplier<Object2IntMap<Obj>> newMapSupplier
    ){
        var map = newMapSupplier.get();
        map.putAll(a);
        b.forEach((k,v)-> map.mergeInt(k,v,Integer::sum));
        return Object2IntMaps.unmodifiable(map);
    }

    @Override
    public boolean isCollectionEmpty(Object2IntMap<Obj> collection) {
        return collection.isEmpty();
    }

    @Override
    public CalcCacheableObject2IntMap<Obj> newForCalculatedResult(Object2IntMap<Obj> wrapped, boolean isSingleton) {
        return new CalcCacheableObject2IntMap<>(wrapped, isSingleton);
    }

    @Override
    public Object2IntMap<Obj> operateEachValue(Object2IntMap<Obj> a,Object2IntMap<Obj> b, Supplier<Object2IntMap<Obj>> newMapSupplier, IntBinaryOperator oper) {
        var newMap = newMapSupplier.get();
        var bClone = new Object2IntOpenHashMap<>(b);
        a.forEach((k,v) -> {
            newMap.put(k,oper.applyAsInt(v,bClone.removeInt(k)));
        });
        bClone.forEach((k,v) -> {
            newMap.put(k,oper.applyAsInt(0,v));
        });
        return Object2IntMaps.unmodifiable(newMap);
    }
}
