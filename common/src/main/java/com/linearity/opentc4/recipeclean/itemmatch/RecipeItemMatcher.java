package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public abstract class RecipeItemMatcher implements ReturnItemStackAfterConsume{
    public abstract boolean matches(@NotNull ItemStack stack);

    //fill as many as possible,except tool with different durability.
    public abstract @NotNull List<ItemStack> getAvailableItemStackSample();
}
