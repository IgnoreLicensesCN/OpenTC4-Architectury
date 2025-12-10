package com.linearity.opentc4.recipeclean;

import net.minecraft.world.item.ItemStack;

public abstract class RecipeMatcher {
    public abstract boolean matches(ItemStack stack);
}
