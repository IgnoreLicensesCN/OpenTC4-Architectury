package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.LinkedHashMapAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.LinkedHashMap;

public class AspectListAccessor extends CompoundTagAccessor<AspectList<Aspect>> {
    protected final LinkedHashMapAccessor<Aspect,Integer> aspectAndAmountsAccessor;
//    protected final ModifiableListAccessor<SimplePair<Aspect,Integer>> aspectAndAmountsAccessor;
    public AspectListAccessor(String tagKey) {
        super(tagKey);
        this.aspectAndAmountsAccessor = new LinkedHashMapAccessor<>(
                tagKey,
                new AspectAccessor(tagKey + "_aspect"),
                new IntTagAccessor(tagKey + "_amount")
        );
    }

    @Override
    public AspectList<Aspect> readFromCompoundTag(CompoundTag tag) {
        return new AspectList<>(this.aspectAndAmountsAccessor.readFromCompoundTag(tag));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, AspectList<Aspect> toWrite) {
        this.aspectAndAmountsAccessor.writeToCompoundTag(tag, new LinkedHashMap<>(toWrite.getAspectView()));
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return aspectAndAmountsAccessor.compoundTagHasKey(tag);
    }
}
