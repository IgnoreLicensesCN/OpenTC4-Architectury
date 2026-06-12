package com.linearity.opentc4.utils.collectionlike;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import thaumcraft.api.aspects.Aspect;

public class UnmodifiableAspectFloatEntry<Asp extends Aspect> implements Object2FloatMap.Entry<Asp> {
    private final Asp aspect;
    private final float value;

    public UnmodifiableAspectFloatEntry(Asp aspect, float value) {
        this.aspect = aspect;
        this.value = value;
    }

    @Override
    public float getFloatValue() {
        return value;
    }

    @Override
    public float setValue(float v) {
        throw new UnsupportedOperationException("Unmodifiable entry!");
    }

    @Override
    public Asp getKey() {
        return aspect;
    }
}
