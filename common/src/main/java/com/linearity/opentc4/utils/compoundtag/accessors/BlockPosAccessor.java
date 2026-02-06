package com.linearity.opentc4.utils.compoundtag.accessors;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class BlockPosAccessor extends CompoundTagAccessor<BlockPos> {
    protected final IntTagAccessor xAccessor;
    protected final IntTagAccessor yAccessor;
    protected final IntTagAccessor zAccessor;

    public BlockPosAccessor(String tagKey) {
        super(tagKey, BlockPos.class);
        this.xAccessor = new IntTagAccessor(tagKey + "_x");
        this.yAccessor = new IntTagAccessor(tagKey + "_y");
        this.zAccessor = new IntTagAccessor(tagKey + "_z");
    }

    @Override
    public BlockPos readFromCompoundTag(CompoundTag tag) {
        return new BlockPos(
                xAccessor.readFromCompoundTag(tag),
                yAccessor.readFromCompoundTag(tag),
                zAccessor.readFromCompoundTag(tag)
        );
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, BlockPos value) {
        xAccessor.writeToCompoundTag(tag,value.getX());
        yAccessor.writeToCompoundTag(tag,value.getY());
        zAccessor.writeToCompoundTag(tag,value.getZ());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return xAccessor.compoundTagHasKey(tag)
                && yAccessor.compoundTagHasKey(tag)
                && zAccessor.compoundTagHasKey(tag);
    }
}
