package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EnchantmentItemMatcher extends RecipeItemMatcher{
    private final Enchantment enchantment;
    private final List<ItemStack> samples;
    protected EnchantmentItemMatcher(Enchantment enchantment) {
        this.enchantment = enchantment;
        var samples = new ArrayList<ItemStack>(enchantment.getMaxLevel());
        var enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);

        for (int i=1;i<=enchantment.getMaxLevel();i++) {
            EnchantmentHelper.setEnchantmentLevel(enchantedBook.getOrCreateTag(), i);
            samples.add(enchantedBook.copy());
        }
        this.samples = Collections.unmodifiableList(samples);
    }
    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return enchantment.canEnchant(stack);
    }

    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        return samples;
    }
    private static final Map<Enchantment,EnchantmentItemMatcher> ofInstances = new HashMap<>();
    public static EnchantmentItemMatcher of(@NotNull Enchantment enchantment) {
        return ofInstances.computeIfAbsent(enchantment, EnchantmentItemMatcher::new);
    }
}
