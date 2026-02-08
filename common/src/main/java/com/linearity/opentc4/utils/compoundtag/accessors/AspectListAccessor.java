package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.simpleutils.SimplePair;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.ModifiableListAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.utility.SimplePairAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class AspectListAccessor extends CompoundTagAccessor<AspectList<Aspect>> {
    protected final ModifiableListAccessor<SimplePair<Aspect,Integer>> aspectAndAmountsAccessor;
    public AspectListAccessor(String tagKey) {
        super(tagKey, (Class<AspectList<Aspect>>) (Class<?>)AspectList.class);
        this.aspectAndAmountsAccessor = new ModifiableListAccessor<>(
                tagKey
                ,new SimplePairAccessor<>(
                tagKey+"_pair",
                        new AspectAccessor(tagKey + "_aspect"),
                        new IntTagAccessor(tagKey + "_amount"))
        );
    }

    @Override
    public AspectList<Aspect> readFromCompoundTag(CompoundTag tag) {
        var list = this.aspectAndAmountsAccessor.readFromCompoundTag(tag);
        AspectList<Aspect> result = new AspectList<>();
        for(var pair : list) {
            result.addAll(pair.a(),pair.b());
        }
        return result;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, AspectList<Aspect> toWrite) {
        List<SimplePair<Aspect,Integer>> toWriteList = new ArrayList<>(toWrite.size());
        toWrite.forEach((key, value1) -> toWriteList.add(new SimplePair<>(key, value1)));
        this.aspectAndAmountsAccessor.writeToCompoundTag(tag, toWriteList);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return aspectAndAmountsAccessor.compoundTagHasKey(tag);
    }
}
