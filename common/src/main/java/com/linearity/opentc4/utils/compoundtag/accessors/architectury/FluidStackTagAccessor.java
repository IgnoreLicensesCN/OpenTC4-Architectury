package com.linearity.opentc4.utils.compoundtag.accessors.architectury;

import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessor;
import com.linearity.opentc4.utils.compoundtag.accessors.basic.CompoundTagAccessorImpl;
import dev.architectury.fluid.FluidStack;
import net.minecraft.nbt.CompoundTag;

public class FluidStackTagAccessor extends CompoundTagAccessor<FluidStack> {
    private final CompoundTagAccessorImpl nameWrapTag;
    public FluidStackTagAccessor(String tagKey) {
        super(tagKey);
        this.nameWrapTag = new CompoundTagAccessorImpl(tagKey);
    }

    @Override
    public FluidStack readFromCompoundTag(CompoundTag tag) {
        return FluidStack.read(nameWrapTag.readFromCompoundTag(tag));
    }

    @Override
    public void writeToCompoundTag(CompoundTag tag, FluidStack value) {
        var wrappedTag = nameWrapTag.readFromCompoundTag(tag);
        value.write(wrappedTag);
        nameWrapTag.writeToCompoundTag(tag,wrappedTag);
    }

    @Override
    public boolean compoundTagHasKey(CompoundTag tag) {
        return nameWrapTag.compoundTagHasKey(tag);
    }
}
