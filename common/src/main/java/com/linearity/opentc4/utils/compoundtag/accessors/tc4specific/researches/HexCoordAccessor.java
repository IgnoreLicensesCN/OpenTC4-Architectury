package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.researches;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.common.lib.utils.HexCoord;

public class HexCoordAccessor extends CompoundTagAccessor<HexCoord> {
    protected final IntTagAccessor qAccessorInternal;
    protected final IntTagAccessor rAccessorInternal;

    protected HexCoordAccessor(String tagKey) {
        super(tagKey);
        this.qAccessorInternal = new IntTagAccessor(tagKey + "_q");
        this.rAccessorInternal = new IntTagAccessor(tagKey + "_r");
    }

    @Override
    public HexCoord readFromCompoundTag(CompoundTag tag) {
        var q = qAccessorInternal.readIntFromCompoundTag(tag);
        var r = rAccessorInternal.readIntFromCompoundTag(tag);
        return new HexCoord(q, r);
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, HexCoord value) {
        qAccessorInternal.writeIntToCompoundTag(tag, value.q());
        rAccessorInternal.writeIntToCompoundTag(tag, value.r());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return qAccessorInternal.compoundTagHasKey(tag) && rAccessorInternal.compoundTagHasKey(tag);
    }
}
