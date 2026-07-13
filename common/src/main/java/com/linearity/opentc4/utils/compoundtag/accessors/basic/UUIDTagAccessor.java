package com.linearity.opentc4.utils.compoundtag.accessors.basic;

import com.linearity.opentc4.utils.compoundtag.accessors.CompoundTagAccessor;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class UUIDTagAccessor extends CompoundTagAccessor<UUID> {
    private final LongTagAccessor mostPart;
    private final LongTagAccessor leastPart;

    public UUIDTagAccessor(String tagKey) {
        super(tagKey);
        this.mostPart = new LongTagAccessor(tagKey+"_most");
        this.leastPart = new LongTagAccessor(tagKey+"_least");
    }

    @Override
    public UUID readFromCompoundTag(CompoundTag tag) {
        return new UUID(mostPart.readLongFromCompoundTag(tag), leastPart.readLongFromCompoundTag(tag));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, UUID value) {
        mostPart.writeLongToCompoundTag(tag,value.getMostSignificantBits());
        leastPart.writeLongToCompoundTag(tag,value.getLeastSignificantBits());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return mostPart.compoundTagHasKey(tag) && leastPart.compoundTagHasKey(tag);
    }
}
