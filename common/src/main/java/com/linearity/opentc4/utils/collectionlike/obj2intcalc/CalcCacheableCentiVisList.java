package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;

import java.util.function.Supplier;

//if we always create new map for #add we will get lots of maps
public class CalcCacheableCentiVisList<Asp extends Aspect> extends
        CalcCacheableAbstractAspectList<
                Asp,
                CentiVisList<Asp>,
                CalcCacheableCentiVisList<Asp>
                > {

    public CalcCacheableCentiVisList(
            @NotNull @Unmodifiable /*input should be unmodifiable*/ CentiVisList<Asp> wrapped,
            boolean inputIsSingleton //considered as modified,if false "wrapped" you've input should be singleton
            //e.g. a ring provides runic shield in a const map,so we set couldBeModified false then there wont be lots of maps with same value cached
            //each map is considered as a single instance
    ) {
        super(wrapped, inputIsSingleton);
    }

    @Override
    protected CentiVisList<Asp> getEmptyInnerCollection() {
        return UnmodifiableCentiVisList.of();
    }

    @Override
    public CalcCacheableCentiVisList<Asp> newForCalculatedResult(
            CentiVisList<Asp> wrapped,
            CalcCacheableCentiVisList<Asp> singletonPart,
            CentiVisList<Asp> consideredNotSingletonPart) {
        return new CalcCacheableCentiVisList<>(wrapped, singletonPart, consideredNotSingletonPart);
    }

    protected CalcCacheableCentiVisList(
            @NotNull CentiVisList<Asp> wrapped,
            @NotNull CalcCacheableCentiVisList<Asp> singletonPart,//should be generated from a unmodifiablePart using "operation"
            @NotNull CentiVisList<Asp> consideredModifiablePart
    ) {
        super(wrapped, singletonPart, consideredModifiablePart);
    }

    @Override
    public CalcCacheableCentiVisList<Asp> newForCalculatedResult(CentiVisList<Asp> wrapped, boolean isSingleton) {
        return new CalcCacheableCentiVisList<>(wrapped, isSingleton);
    }

    public static final CalcCacheableCentiVisList<?> EMPTY = new CalcCacheableCentiVisList<Aspect>(UnmodifiableCentiVisList.of(), true) {
        @Override
        public CalcCacheableCentiVisList<Aspect> add(CalcCacheableCentiVisList<Aspect> another, Supplier<CentiVisList<Aspect>> newMapSupplier) {
            return another;
        }
    };

    public static <Asp extends Aspect> CalcCacheableCentiVisList<Asp> emptySingleton() {
        return (CalcCacheableCentiVisList<Asp>) EMPTY;
    }

    @Override
    protected CalcCacheableCentiVisList<Asp> emptyOuterSingleton() {
        return emptySingleton();
    }
    @Override
    protected CentiVisList<Asp> wrapAsUnmodifiable(CentiVisList<Asp> asps) {
        return UnmodifiableCentiVisList.of(asps);
    }
}

