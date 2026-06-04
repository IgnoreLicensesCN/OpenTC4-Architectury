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

public class EnchantableResultMatcher extends RecipeItemMatcher{

    private final Enchantment enchantment;
    private EnchantableResultMatcher(Enchantment enchantment) {
        this.enchantment = enchantment;
    }
    @Override
    public boolean matches(@NotNull ItemStack stack) {
        int lvl = EnchantmentHelper.getItemEnchantmentLevel(enchantment,stack);
        return lvl > 0 && enchantment.getMaxLevel() >= lvl;
    }

    public static final List<ItemStack> enchantedBookSample = List.of(Items.ENCHANTED_BOOK.getDefaultInstance());
    @Override
    public @JEILikeOnly @NotNull List<ItemStack> getAvailableItemStackSample() {
        return enchantedBookSample;
    }
    private static final Map<Enchantment, EnchantableResultMatcher> enchantableMatcherMap = new ConcurrentHashMap<>();
    public static EnchantableResultMatcher of(Enchantment enchantment) {
        return enchantableMatcherMap.computeIfAbsent(enchantment, EnchantableResultMatcher::new);
    }
}
