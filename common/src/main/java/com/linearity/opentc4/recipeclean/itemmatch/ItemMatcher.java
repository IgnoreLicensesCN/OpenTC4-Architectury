package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ItemMatcher extends RecipeItemMatcher {
    private static final Map<Item, ItemMatcher> cache = new ConcurrentHashMap<>();
    private final Item item;
    private final List<ItemStack> sample;
    private ItemMatcher(final Item item) {
        this.item = item;
        this.sample = List.of(new ItemStack(item));
    }



    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return Objects.equals(this.item, stack.getItem());
    }

    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        return this.sample;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemMatcher that)) return false;
        return Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(item);
    }

    public static @NotNull ItemMatcher of(@NotNull final ItemStack stack){
        return of(stack.getItem());
    }

    public static @NotNull ItemMatcher of(@NotNull final Item item) {
        return cache.computeIfAbsent(item, ItemMatcher::new);
    }
}
