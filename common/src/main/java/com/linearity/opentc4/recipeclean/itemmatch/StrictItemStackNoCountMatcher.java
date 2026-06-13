package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class StrictItemStackNoCountMatcher extends RecipeItemMatcher {
    private final ItemStack stack;
    private final List<ItemStack> sample;
    public StrictItemStackNoCountMatcher(final ItemStack stack) {
        this.stack = stack.copy();
        this.sample = List.of(stack);
    }

    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return ItemStack.isSameItemSameTags(this.stack,stack);
    }

    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        return this.sample;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StrictItemStackNoCountMatcher that)) return false;
        return ItemStack.isSameItemSameTags(this.stack,that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stack);
    }

    @Override
    public String toString() {
        return "StrictItemStackNoCountMatcher{" + "stack=" + stack + '}';
    }
}
