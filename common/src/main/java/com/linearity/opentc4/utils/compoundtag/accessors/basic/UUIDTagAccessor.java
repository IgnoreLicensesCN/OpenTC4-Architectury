package com.linearity.opentc4.utils.compoundtag.accessors.basic;

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
        return new UUID(mostPart.readFromCompoundTag(tag), leastPart.readFromCompoundTag(tag));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, UUID value) {
        mostPart.writeToCompoundTag(tag,value.getMostSignificantBits());
        leastPart.writeToCompoundTag(tag,value.getLeastSignificantBits());
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return mostPart.compoundTagHasKey(tag) && leastPart.compoundTagHasKey(tag);
    }
}
