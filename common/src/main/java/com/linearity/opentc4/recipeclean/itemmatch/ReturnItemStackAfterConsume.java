package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;

public interface ReturnItemStackAfterConsume {
    //should consider stack count.
    default ItemStack getReturn(ItemStack stack) {
        return ItemStack.EMPTY;
    }
}
