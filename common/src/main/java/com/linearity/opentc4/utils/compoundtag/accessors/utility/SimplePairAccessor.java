package com.linearity.opentc4.utils.compoundtag.accessors.utility;

import com.linearity.opentc4.simpleutils.SimplePair;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;

public class SimplePairAccessor<A,B> extends CompoundTagAccessor<SimplePair<A,B>> {
    protected final CompoundTagAccessor<A> aAccessor;
    protected final CompoundTagAccessor<B> bAccessor;
    public SimplePairAccessor(String tagKey, CompoundTagAccessor<A> accessorA, CompoundTagAccessor<B> accessorB) {
        super(tagKey, (Class<SimplePair<A, B>>)(Class<?>)SimplePair.class);
        this.aAccessor = accessorA;
        this.bAccessor = accessorB;
    }

    @Override
    public SimplePair<A, B> readFromCompoundTag(CompoundTag tag) {
        return new SimplePair<>(aAccessor.readFromCompoundTag(tag), bAccessor.readFromCompoundTag(tag));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, SimplePair<A, B> value) {
        aAccessor.writeToCompoundTag(tag,value.a());
        bAccessor.writeToCompoundTag(tag,value.b());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return aAccessor.compoundTagHasKey(tag) && bAccessor.compoundTagHasKey(tag);
    }
}
