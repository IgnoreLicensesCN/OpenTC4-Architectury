package com.linearity.opentc4.recipeclean;

import net.minecraft.world.item.ItemStack;

import static com.linearity.opentc4.OpenTC4.utils;

public class TagMatcher extends RecipeMatcher {
    private final String[] tags;
    public TagMatcher(String[] tags) {
        this.tags = tags;
    }
    public TagMatcher(String tag) {
        this.tags = new String[] {tag};
    }

    @Override
    public boolean matches(ItemStack stack) {
        for (String tag : tags) {
            if (utils.isItemStackMatchTag(stack,tag)){
                return true;
            }
        }
        return false;
    }
}
