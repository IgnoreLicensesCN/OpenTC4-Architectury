package com.linearity.opentc4.utils.compoundtag.accessors.mc;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.IntTagAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class BlockPosAccessor extends CompoundTagAccessor<BlockPos> {
    public static final BlockPos NULL_POS_TO_WRITE = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    protected final IntTagAccessor xAccessor;
    protected final IntTagAccessor yAccessor;
    protected final IntTagAccessor zAccessor;

    public BlockPosAccessor(String tagKey) {
        super(tagKey);
        this.xAccessor = new IntTagAccessor(tagKey + "_x");
        this.yAccessor = new IntTagAccessor(tagKey + "_y");
        this.zAccessor = new IntTagAccessor(tagKey + "_z");
    }

    @Override
    public BlockPos readFromCompoundTag(CompoundTag tag) {
        return new BlockPos(
                xAccessor.readIntFromCompoundTag(tag),
                yAccessor.readIntFromCompoundTag(tag),
                zAccessor.readIntFromCompoundTag(tag)
        );
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, BlockPos value) {
        xAccessor.writeIntToCompoundTag(tag,value.getX());
        yAccessor.writeIntToCompoundTag(tag,value.getY());
        zAccessor.writeIntToCompoundTag(tag,value.getZ());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return xAccessor.compoundTagHasKey(tag)
                && yAccessor.compoundTagHasKey(tag)
                && zAccessor.compoundTagHasKey(tag);
    }
}
