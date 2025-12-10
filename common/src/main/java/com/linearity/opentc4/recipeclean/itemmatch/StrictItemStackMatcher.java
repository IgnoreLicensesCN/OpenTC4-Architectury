package com.linearity.opentc4.recipeclean;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.lib.utils.InventoryUtils;

public class StrictItemStackMatcher extends RecipeItemMatcher {
    private final ItemStack stack;
    public StrictItemStackMatcher(final ItemStack stack) {
        this.stack = stack.copy();
    }

    @Override
    public boolean matches(ItemStack stack) {
        return InventoryUtils.areItemStacksEqual(this.stack,stack,false,false);
    }
}
