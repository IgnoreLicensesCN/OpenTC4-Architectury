package com.linearity.opentc4.utils.equip.bauble;

import net.minecraft.world.item.ItemStack;

public interface BaubleConsumer<T> {
    /**
     * @param stack stack in slot,skip if null.
     * @param item item of the stack
     * @return break flag
     */
    boolean accept(EquippedBaubleSlot slot,ItemStack stack,T item);
}
