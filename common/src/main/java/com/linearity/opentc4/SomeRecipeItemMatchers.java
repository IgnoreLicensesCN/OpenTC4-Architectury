package com.linearity.opentc4;

import com.linearity.opentc4.recipeclean.itemmatch.*;
import thaumcraft.common.items.ThaumcraftItemsRegistry;

import static thaumcraft.common.items.ThaumcraftItems.ItemTags.*;

public class SomeRecipeItemMatchers {

    public static final RecipeItemMatcher EMPTY_MATCHER = EmptyMatcher.EMPTY_MATCHER;
    public static final RecipeItemMatcher WAND_CAP_MATCHER = TagItemMatcher.of(WAND_CAP);
    public static final RecipeItemMatcher WAND_ROD_MATCHER = TagItemMatcher.of(WAND_ROD);
    public static final RecipeItemMatcher STAFF_ROD_MATCHER = TagItemMatcher.of(STAFF_ROD);
    public static final RecipeItemMatcher PRIMAL_CHARM_MATCHER = ItemMatcher.of(ThaumcraftItemsRegistry.SUPPLIER_PRIMAL_CHARM.get());
    public static final RecipeItemMatcher SCEPTRE_MATCHER = ItemMatcher.of(ThaumcraftItemsRegistry.SUPPLIER_SCEPTRE_CASTING.get());//TODO:Sceptre Item Instance
}
