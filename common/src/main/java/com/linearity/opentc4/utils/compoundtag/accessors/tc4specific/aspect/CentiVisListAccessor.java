package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.LinkedHashCentiVisList;

public class CentiVisListAccessor extends CompoundTagAccessor<CentiVisList<Aspect>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final CompoundTagAccessor<Aspect> keyAccessor;
    protected final IntTagAccessor valueAccessor;
    public CentiVisListAccessor(String tagKey) {
        super(tagKey);
        this.keyAccessor = new AspectAccessor(tagKey + "_aspect");
        this.valueAccessor = new IntTagAccessor(tagKey + "_amount");
        this.listTagAccessorInternal = new ListTagAccessor(tagKey);
    }

    @Override
    public CentiVisList<Aspect> readFromCompoundTag(CompoundTag tag) {
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        Object2IntLinkedOpenHashMap<Aspect> result = new Object2IntLinkedOpenHashMap<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var hexCoord = keyAccessor.readFromCompoundTag(compoundTag);
            var hexType = valueAccessor.readIntFromCompoundTag(compoundTag);
            result.put(hexCoord, hexType);
        }
        return new LinkedHashCentiVisList<>(result);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, CentiVisList<Aspect> toWrite) {
        var listTag = new ListTag();
        toWrite.forEach((key,intValue) -> {
            var compound = new CompoundTag();
            keyAccessor.writeToCompoundTag(compound, key);
            valueAccessor.writeIntToCompoundTag(compound, intValue);
            listTag.add(compound);
        });
        listTagAccessorInternal.writeToCompoundTag(tag, listTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return listTagAccessorInternal.compoundTagHasKey(tag);
    }
}
