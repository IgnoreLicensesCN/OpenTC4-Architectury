package com.linearity.opentc4.utils.compoundtag.accessors.mc;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.ByteTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public class DirectionTagAccessor extends CompoundTagAccessor<Direction> {

    private final ByteTagAccessor byteTagAccessor;
    public DirectionTagAccessor(String tagKey) {
        super(tagKey);
        byteTagAccessor = new ByteTagAccessor(tagKey);
    }

    @Override
    public Direction readFromCompoundTag(CompoundTag tag) {
        byte index = byteTagAccessor.readByteFromCompoundTag(tag);
        if (index < 0 || index >= Direction.values().length) {
            return Direction.NORTH;
        }
        return Direction.values()[index];
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, Direction value) {
        byteTagAccessor.writeByteToCompoundTag(tag,(byte)value.ordinal());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return byteTagAccessor.compoundTagHasKey(tag);
    }
}
