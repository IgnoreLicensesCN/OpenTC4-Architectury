package com.linearity.opentc4.recipeclean;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.lib.utils.InventoryUtils;

import static com.linearity.opentc4.utils.Vanilla1710Utils.setIgnoresDamage;

public class IgnoresDamageItemStackMatcher extends RecipeItemMatcher {
    private final ItemStack stack;
    public IgnoresDamageItemStackMatcher(final ItemStack stack) {
        this.stack = stack.copy();
        setIgnoresDamage(this.stack,true);
    }

    @Override
    public boolean matches(ItemStack stack) {
        return InventoryUtils.areItemStacksEqual(this.stack,stack,true,false);
    }
}
