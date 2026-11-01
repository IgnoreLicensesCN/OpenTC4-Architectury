package com.linearity.opentc4.mixinaccessors;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

public interface ExtendedItemStack {
    ItemStack opentc4$splitAll();

    @ApiStatus.Internal
    void opentc4$forceSetTag(CompoundTag compoundTag);

    static ItemStack splitAll(ItemStack stack) {
        return ((ExtendedItemStack)(Object)stack).opentc4$splitAll();
    }

    static ItemStack split(ItemStack stack, int amount) {
        if (amount == stack.getCount()) {
            return splitAll(stack);
        }
        return stack.split(amount);
    }
}
