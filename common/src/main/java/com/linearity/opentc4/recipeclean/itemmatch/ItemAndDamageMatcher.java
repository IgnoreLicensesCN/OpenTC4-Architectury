package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ItemAndDamageMatcher extends RecipeItemMatcher{
    private record ItemAndDamageCacheKey(Item item, int damage) {
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ItemAndDamageCacheKey that)) return false;
            return damage == that.damage && Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, damage);
        }
    }

    private static final Map<ItemAndDamageCacheKey,ItemAndDamageMatcher> cache = new ConcurrentHashMap<>();
    private final Item item;
    private final int damage;
    private ItemAndDamageMatcher(ItemStack stack) {
        this(stack.getItem(),stack.getDamageValue());
    }
    private ItemAndDamageMatcher(Item item, int damage) {
        this.item = item;
        this.damage = damage;
        this.sample = List.of(new ItemStack(item,damage));
    }
    private ItemAndDamageMatcher(ItemAndDamageCacheKey key) {
        this.item = key.item;
        this.damage = key.damage;
        this.sample = List.of(new ItemStack(item,damage));
    }
    private final List<ItemStack> sample;

    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return stack.getItem() == item && stack.getDamageValue() == damage;
    }

    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        return sample;
    }

    public static @NotNull ItemAndDamageMatcher of(@NotNull ItemStack item) {
        return of(item.getItem(), item.getDamageValue());
    }

    public static @NotNull ItemAndDamageMatcher of(@NotNull Item item, int damage) {
        return cache.computeIfAbsent(new ItemAndDamageCacheKey(item,damage),ItemAndDamageMatcher::new);
    }

}
