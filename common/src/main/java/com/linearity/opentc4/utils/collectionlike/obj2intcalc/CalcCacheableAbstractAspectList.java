package com.linearity.opentc4.utils.collectionlike.obj2intcalc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;

import java.util.function.Supplier;

//if we always create new map for #add we will get lots of maps
public abstract class CalcCacheableAbstractAspectList<
        Asp extends Aspect,
        L extends AspectList<Asp>,
        SelfClass extends CalcCacheableAbstractAspectList<Asp,L,SelfClass>
        >
        extends CalcCacheableCollection<
        L,
        SelfClass
        > {
    public @Unmodifiable
    @NotNull L wrapped;//the value should be considered as(do not modify it)
    public final boolean inputIsSingleton;//the value in could be modified,determine how we cache the result

    protected @NotNull SelfClass singletonPart = emptyOuterSingleton();
    protected @Unmodifiable @NotNull L consideredNotSingletonPart = getEmptyInnerCollection();

    @Override
    public @Unmodifiable @NotNull L getWrapped() {
        return wrapped;
    }

    @Override
    public boolean getInputIsSingleton() {
        return inputIsSingleton;
    }

    @Override
    public @NotNull SelfClass getSingletonPart() {
        return singletonPart;
    }

    @Override
    public @Unmodifiable @NotNull L getConsideredNotSingletonPart() {
        return consideredNotSingletonPart;
    }

    public CalcCacheableAbstractAspectList(
            @NotNull @Unmodifiable /*input should be unmodifiable*/ L wrapped,
            boolean inputIsSingleton //considered as modified,if false "wrapped" you've input should be singleton
            //e.g. a ring provides runic shield in a const map,so we set couldBeModified false then there wont be lots of maps with same value cached
            //each map is considered as a single instance
    ) {
        this.wrapped = wrapped;
        this.inputIsSingleton = inputIsSingleton;
        if (!this.inputIsSingleton) {
            consideredNotSingletonPart = wrapAsUnmodifiable(getConsideredNotSingletonPart());
        }
    }

    protected CalcCacheableAbstractAspectList(
            @NotNull L wrapped,
            @NotNull SelfClass singletonPart,//should be generated from a unmodifiablePart using "operation"
            @NotNull L consideredModifiablePart
    ) {
        this.wrapped = wrapped;
        this.inputIsSingleton = consideredModifiablePart.isEmpty();
        this.singletonPart = singletonPart;
        this.consideredNotSingletonPart = consideredModifiablePart;
    }

    public SelfClass add(SelfClass another, Supplier<L> newMapSupplier) {
        return CalculationOperationAdd.INSTANCE.calculateWithCache(
                (SelfClass)this,
                another,
                newMapSupplier,
                this::addObject2IntMapAsNew
        );
    }

    public L addObject2IntMapAsNew(
            L a,
            L b,
            Supplier<L> newMapSupplier
    ) {
        var map = newMapSupplier.get();
        map.addAll(a);
        b.forEach((k, v) -> map.merge(k, v, Integer::sum));
        return wrapAsUnmodifiable(map);
    }

    @Override
    public boolean isCollectionEmpty(L collection) {
        return collection.isEmpty();
    }

    protected abstract L getEmptyInnerCollection();
    protected abstract SelfClass emptyOuterSingleton();
    protected abstract L wrapAsUnmodifiable(L asps);
}
