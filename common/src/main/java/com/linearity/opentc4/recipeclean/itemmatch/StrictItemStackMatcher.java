package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.List;
import java.util.Objects;

public class StrictItemStackMatcher extends RecipeItemMatcher {
    private final ItemStack stack;
    private final List<ItemStack> sample;
    public StrictItemStackMatcher(final ItemStack stack) {
        this.stack = stack.copy();
        this.sample = List.of(stack);
    }

    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return InventoryUtils.areItemStacksEqual(this.stack,stack,false,false);
    }

    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        return this.sample;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StrictItemStackMatcher that)) return false;
        return Objects.equals(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stack);
    }
}
