package com.linearity.opentc4.recipeclean.itemmatch;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.linearity.opentc4.utils.IndexPicker.pickByTime;

public class ItemMatcherSum extends RecipeItemMatcher{
    private final RecipeItemMatcher[] matchers;
    public ItemMatcherSum(RecipeItemMatcher... matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean matches(@NotNull ItemStack stack) {
        for (RecipeItemMatcher matcher : matchers) {
            if (matcher.matches(stack)){
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull List<ItemStack> getAvailableItemStackSample() {
        return pickByTime(matchers).getAvailableItemStackSample();
    }
}
