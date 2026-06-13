package com.linearity.opentc4.recipeclean.itemmatch;

import com.linearity.opentc4.annotations.JEILikeOnly;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemClassInstanceOfMatcher extends RecipeItemMatcher{

    private final Class<?>  clazz;
    private final List<ItemStack> examples;
    public ItemClassInstanceOfMatcher(Class<?> clazz,List<ItemStack> exampleStacks) {
        this.clazz = clazz;
        this.examples = exampleStacks;
    }
    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return clazz.isInstance(stack.getItem());
    }

    @Override
    public @JEILikeOnly @NotNull List<ItemStack> getAvailableItemStackSample() {
        return examples;
    }
    private static final Map<Class<?>, ItemClassInstanceOfMatcher> CACHE = new HashMap<>();
    public static ItemClassInstanceOfMatcher of(Class<?> clazz,List<ItemStack> exampleStacks) {
        return CACHE.computeIfAbsent(clazz, clazz1 -> new ItemClassInstanceOfMatcher(clazz1, exampleStacks));
    }

    @Override
    public String toString() {
        return "ItemClassInstanceOfMatcher{" +
                "clazz=" + clazz +
                '}';
    }
}
