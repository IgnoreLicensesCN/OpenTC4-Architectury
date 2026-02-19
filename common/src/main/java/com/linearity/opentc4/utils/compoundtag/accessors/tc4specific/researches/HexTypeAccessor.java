package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.researches;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.lib.research.HexType;

public class HexTypeAccessor extends CompoundTagAccessor<HexType> {
    protected final IntTagAccessor intTypeAccessorInternal;

    public HexTypeAccessor(String tagKey) {
        super(tagKey);
        this.intTypeAccessorInternal = new IntTagAccessor(tagKey + "_type_int");
    }

    @Override
    public HexType readFromCompoundTag(CompoundTag tag) {
        return HexType.values()[intTypeAccessorInternal.readFromCompoundTag(tag)];
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, HexType value) {
        intTypeAccessorInternal.writeToCompoundTag(tag, value.ordinal());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return intTypeAccessorInternal.compoundTagHasKey(tag);
    }
}
