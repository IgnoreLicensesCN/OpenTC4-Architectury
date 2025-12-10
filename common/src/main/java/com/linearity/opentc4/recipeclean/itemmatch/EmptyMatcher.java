package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//obviously,we need only one
public class EmptyMatcher extends RecipeItemMatcher {
    public static final EmptyMatcher EMPTY_MATCHER = new EmptyMatcher();

    private static final List<ItemStack> EMPTY_SINGLE = List.of(ItemStack.EMPTY);
    private EmptyMatcher() {}
    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return stack.isEmpty();
    }

    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        return EMPTY_SINGLE;
    }
}
