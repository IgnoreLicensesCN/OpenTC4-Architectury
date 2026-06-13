package com.linearity.opentc4.recipeclean.itemmatch;

import com.linearity.opentc4.annotations.JEILikeOnly;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//oh i mean using List<Object> to make this is terrible.
public abstract class RecipeItemMatcher implements ReturnItemStackAfterConsume{
    //should not modify stack
    public abstract boolean matches(@NotNull ItemStack stack);

    //fill as many as possible,except tool with different durability.
    @JEILikeOnly
    public abstract @NotNull List<ItemStack> getAvailableItemStackSample();
    public ItemStack getRemainingStack(ItemStack stack) {
        var remaining = stack.getItem().getCraftingRemainingItem();
        if (remaining == null) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(remaining,stack.getCount());
    }
}
