package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.Object2IntLinkedOpenHashMapAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;

public class CentiVisListAccessor extends CompoundTagAccessor<CentiVisList<Aspect>> {
    protected final Object2IntLinkedOpenHashMapAccessor<Aspect> aspectAndAmountsAccessor;
    public CentiVisListAccessor(String tagKey) {
        super(tagKey);
        this.aspectAndAmountsAccessor = new Object2IntLinkedOpenHashMapAccessor<>(
                tagKey,
                new AspectAccessor(tagKey + "_aspect"),
                new IntTagAccessor(tagKey + "_amount")
        );
    }

    @Override
    public CentiVisList<Aspect> readFromCompoundTag(CompoundTag tag) {
        return new CentiVisList<>(this.aspectAndAmountsAccessor.readFromCompoundTag(tag));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, CentiVisList<Aspect> toWrite) {
        this.aspectAndAmountsAccessor.writeToCompoundTag(tag, toWrite.getAspectView());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return aspectAndAmountsAccessor.compoundTagHasKey(tag);
    }
}
