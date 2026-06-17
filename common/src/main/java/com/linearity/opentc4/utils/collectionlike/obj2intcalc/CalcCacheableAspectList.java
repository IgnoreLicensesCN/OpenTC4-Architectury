package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;

import java.util.function.Supplier;

//if we always create new map for #add we will get lots of maps
public class CalcCacheableAspectList<Asp extends Aspect> extends 
        CalcCacheableAbstractAspectList<
                Asp,
                AspectList<Asp>,
                CalcCacheableAspectList<Asp>
                > {

    public CalcCacheableAspectList(
            @NotNull @Unmodifiable /*input should be unmodifiable*/ AspectList<Asp> wrapped,
            boolean inputIsSingleton //considered as modified,if false "wrapped" you've input should be singleton
            //e.g. a ring provides runic shield in a const map,so we set couldBeModified false then there wont be lots of maps with same value cached
            //each map is considered as a single instance
    ) {
        super(wrapped, inputIsSingleton);
    }

    @Override
    protected AspectList<Asp> getEmptyInnerCollectionUnmodifiable() {
        return UnmodifiableAspectList.of();
    }

    @Override
    public CalcCacheableAspectList<Asp> newForCalculatedResult(
            AspectList<Asp> wrapped,
            CalcCacheableAspectList<Asp> singletonPart,
            AspectList<Asp> consideredNotSingletonPart) {
        return new CalcCacheableAspectList<>(wrapped, singletonPart, consideredNotSingletonPart);
    }

    protected CalcCacheableAspectList(
            @NotNull AspectList<Asp> wrapped,
            @NotNull CalcCacheableAspectList<Asp> singletonPart,//should be generated from a unmodifiablePart using "operation"
            @NotNull AspectList<Asp> consideredModifiablePart
    ) {
        super(wrapped, singletonPart, consideredModifiablePart);
    }

    @Override
    public CalcCacheableAspectList<Asp> newForCalculatedResult(AspectList<Asp> wrapped, boolean isSingleton) {
        return new CalcCacheableAspectList<>(wrapped, isSingleton);
    }

    public static final CalcCacheableAspectList<?> EMPTY = new CalcCacheableAspectList<>(UnmodifiableAspectList.of(), true) {
        @Override
        public CalcCacheableAspectList<Aspect> add(CalcCacheableAspectList<Aspect> another, Supplier<AspectList<Aspect>> newMapSupplier) {
            return another;
        }
    };

    public static <Asp extends Aspect> CalcCacheableAspectList<Asp> emptySingleton() {
        return (CalcCacheableAspectList<Asp>) EMPTY;
    }

    @Override
    protected CalcCacheableAspectList<Asp> emptyOuterSingleton() {
        return emptySingleton();
    }
    @Override
    protected AspectList<Asp> wrapAsUnmodifiable(AspectList<Asp> asps) {
        return UnmodifiableAspectList.of(asps);
    }
}

