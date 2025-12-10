package com.linearity.opentc4;

import com.linearity.opentc4.recipeclean.itemmatch.EmptyMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.ItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.ThaumcraftItems;

public class SomeRecipeItemMatchers {

    public static final RecipeItemMatcher EMPTY_MATCHER = EmptyMatcher.EMPTY_MATCHER;
    public static final RecipeItemMatcher WAND_CAP_MATCHER = ItemMatcher.of(ConfigItems.itemWandCap);//TODO:This itemWandCap will be removed
    public static final RecipeItemMatcher WAND_ROD_MATCHER = ItemMatcher.of(ConfigItems.itemWandRod);//TODO:This itemWandRod will be removed
    public static final RecipeItemMatcher PRIMAL_CHARM_MATCHER = ItemMatcher.of(ThaumcraftItems.PRIMAL_CHARM);
    public static final RecipeItemMatcher SCEPTRE_MATCHER = ItemMatcher.of(ConfigItems.itemSceptre);//TODO:Sceptre Item Instance
}
