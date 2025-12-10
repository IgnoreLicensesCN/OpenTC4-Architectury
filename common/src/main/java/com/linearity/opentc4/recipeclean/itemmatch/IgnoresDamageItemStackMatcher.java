package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class IgnoresDamageItemStackMatcher extends RecipeItemMatcher {
    private static final Map<ItemStack,IgnoresDamageItemStackMatcher> cache = new ConcurrentHashMap<>();
    private final ItemStack stack;
    private final ItemStack[] returnSample;
    private final AtomicInteger damageSample = new AtomicInteger(0);
    private IgnoresDamageItemStackMatcher(final ItemStack stack) {
        this.stack = stack.copy();
        this.returnSample = new ItemStack[]{stack};
    }

    @Override
    public boolean matches(@NotNull ItemStack b) {
        if (this.stack.isEmpty() && b.isEmpty()) return true;
        if (this.stack.isEmpty() || b.isEmpty()) return false;

        // NBT 比较
        boolean nbtEqual = ItemStack.isSameItemSameTags(this.stack, b);

        return this.stack.getItem() == b.getItem() && nbtEqual;
    }

    private final List<ItemStack> availableItemStackSample = new ArrayList<>(1);
    private final List<ItemStack> availableItemStackSampleView = Collections.unmodifiableList(availableItemStackSample);
    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        int dmg = damageSample.incrementAndGet();
        dmg = dmg%this.stack.getMaxDamage();
        if (dmg < 0){
            dmg += this.stack.getMaxDamage();
        }
        returnSample[0].setDamageValue(dmg);
        availableItemStackSample.set(0, returnSample[0]);
        return availableItemStackSampleView;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IgnoresDamageItemStackMatcher that)) return false;
        return Objects.equals(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stack);
    }


    public @NotNull IgnoresDamageItemStackMatcher of(ItemStack stack) {
        return cache.computeIfAbsent(stack, IgnoresDamageItemStackMatcher::new);
    }
}
