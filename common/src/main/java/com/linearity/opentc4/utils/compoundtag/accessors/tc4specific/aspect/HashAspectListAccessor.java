
package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.aspect;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.ListTagAccessor;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;

public class HashAspectListAccessor extends CompoundTagAccessor<AspectList<Aspect>> {
    protected final ListTagAccessor listTagAccessorInternal;
    protected final CompoundTagAccessor<Aspect> keyAccessor;
    protected final IntTagAccessor valueAccessor;
    public HashAspectListAccessor(String tagKey) {
        super(tagKey);
        this.keyAccessor = new AspectAccessor(tagKey + "_aspect");
        this.valueAccessor = new IntTagAccessor(tagKey + "_amount");
        this.listTagAccessorInternal = new ListTagAccessor(tagKey);
    }

    @Override
    public AspectList<Aspect> readFromCompoundTag(CompoundTag tag) {
        var listTag = listTagAccessorInternal.readFromCompoundTag(tag);
        Object2IntOpenHashMap<Aspect> result = new Object2IntOpenHashMap<>(listTag.size());
        for (int i = 0; i < listTag.size(); i++) {
            var compoundTag = listTag.getCompound(i);
            var hexCoord = keyAccessor.readFromCompoundTag(compoundTag);
            var hexType = valueAccessor.readIntFromCompoundTag(compoundTag);
            result.put(hexCoord, hexType);
        }
        return HashAspectList.viewOf(result);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, AspectList<Aspect> toWrite) {
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
