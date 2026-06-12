package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class TagItemMatcher extends RecipeItemMatcher {
    private final TagKey<Item> tag;
    private final List<ItemStack> sampleView;

    private TagItemMatcher(TagKey<Item> tag) {
        this.tag = tag;
        Set<Item> itemsExample = new HashSet<>(platformUtils.getItemsFromTag(tag));

        Item[] itemsExampleArr = itemsExample.toArray(new Item[0]);
        ItemStack[] sample = new ItemStack[itemsExample.size()];
        for (int i = 0; i < itemsExample.size(); i++) {
            sample[i] = new ItemStack(itemsExampleArr[i]);
        }
        sampleView = List.of(sample);
    }

    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return stack.is(tag);
    }

    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        return sampleView;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TagItemMatcher that)) return false;
        return Objects.deepEquals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }


    private static final Map<TagKey<Item>,TagItemMatcher> cache = new ConcurrentHashMap<>();

    public static @NotNull TagItemMatcher of(@NotNull TagKey<Item> tag) {
        return cache.computeIfAbsent(tag, TagItemMatcher::new);
    }

    @Override
    public String toString() {
        return "TagItemMatcher{" + "tag=" + tag + '}';
    }
}
