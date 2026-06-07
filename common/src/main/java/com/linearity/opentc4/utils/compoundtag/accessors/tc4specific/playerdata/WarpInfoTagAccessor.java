package com.linearity.opentc4.utils.compoundtag.accessors.tc4specific.playerdata;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import net.minecraft.nbt.CompoundTag;
import thaumcraft.api.warp.WarpInfo;

public class WarpInfoTagAccessor extends CompoundTagAccessor<WarpInfo> {
    private final CompoundTagAccessorImpl wrapTagAccessor = new CompoundTagAccessorImpl(tagKey);
    private final IntTagAccessor tempWarpAccessor = new IntTagAccessor(tagKey + "_temp_warp");
    private final IntTagAccessor stickyWarpAccessor = new IntTagAccessor(tagKey + "_sticky_warp");
    private final IntTagAccessor permWarpAccessor = new IntTagAccessor(tagKey + "_perm_warp");
    private final IntTagAccessor warpEventCounterAccessor = new IntTagAccessor(tagKey + "_warp_event_counter");
    public WarpInfoTagAccessor(String tagKey) {
        super(tagKey);
    }
    @Override
    public WarpInfo readFromCompoundTag(CompoundTag tag) {
        WarpInfo warpInfo = new WarpInfo();
        var wrappedTag = wrapTagAccessor.readFromCompoundTag(tag);
        warpInfo.setTempWarp(tempWarpAccessor.readIntFromCompoundTag(wrappedTag));
        warpInfo.setStickyWarp(stickyWarpAccessor.readIntFromCompoundTag(wrappedTag));
        warpInfo.setPermWarp(permWarpAccessor.readIntFromCompoundTag(wrappedTag));
        warpInfo.setWarpEventCounter(warpEventCounterAccessor.readIntFromCompoundTag(tag));
        return warpInfo;
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, WarpInfo value) {
        var wrapTagToWrite = new CompoundTag();
        tempWarpAccessor.writeIntToCompoundTag(wrapTagToWrite, value.getTempWarp());
        stickyWarpAccessor.writeIntToCompoundTag(wrapTagToWrite, value.getStickyWarp());
        permWarpAccessor.writeIntToCompoundTag(wrapTagToWrite, value.getPermWarp());
        warpEventCounterAccessor.writeIntToCompoundTag(wrapTagToWrite, value.getWarpEventCounter());

        wrapTagAccessor.writeToCompoundTag(tag, wrapTagToWrite);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return wrapTagAccessor.compoundTagHasKey(tag);
    }
}
