package com.linearity.opentc4.recipeclean.itemmatch;

import com.linearity.opentc4.annotations.JEILikeOnly;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnchantableMatcher extends RecipeItemMatcher{

    private final Enchantment enchantment;
    private EnchantableMatcher(Enchantment enchantment) {
        this.enchantment = enchantment;
    }
    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return enchantment.canEnchant(stack) && enchantment.getMaxLevel() > EnchantmentHelper.getItemEnchantmentLevel(enchantment,stack);
    }

    public static final List<ItemStack> enchantedBookSample = List.of(Items.ENCHANTED_BOOK.getDefaultInstance());
    @Override
    public @JEILikeOnly @NotNull List<ItemStack> getAvailableItemStackSample() {
        return enchantedBookSample;
    }
    private static final Map<Enchantment, EnchantableMatcher> enchantableMatcherMap = new ConcurrentHashMap<>();
    public static EnchantableMatcher of(Enchantment enchantment) {
        return enchantableMatcherMap.computeIfAbsent(enchantment, EnchantableMatcher::new);
    }

    @Override
    public String toString() {
        return "EnchantableMatcher{" + "enchantment=" + enchantment + '}';
    }
}
