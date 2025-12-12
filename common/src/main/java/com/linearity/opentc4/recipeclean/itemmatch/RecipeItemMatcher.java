package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public abstract class RecipeItemMatcher implements ReturnItemStackAfterConsume{
    public abstract boolean matches(@NotNull ItemStack stack);

    //you know what?
    // you can just return randomly,
    // just satisfy
    // matches(getAvailableItemsSample()[someindex at least 0 is avaliable]) == true
    public abstract @NotNull List<ItemStack> getAvailableItemStackSample();
}
