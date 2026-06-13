package com.linearity.opentc4;

import com.linearity.opentc4.recipeclean.itemmatch.EmptyMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.ItemClassInstanceOfMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.ItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import thaumcraft.api.wands.IWandCapPropertiesOwnerComponent;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.ThaumcraftItemsRegistry;

import java.util.List;

public class SomeRecipeItemMatchers {

    public static final RecipeItemMatcher EMPTY_MATCHER = EmptyMatcher.EMPTY_MATCHER;
    public static final RecipeItemMatcher WAND_CAP_MATCHER = ItemClassInstanceOfMatcher.of(IWandCapPropertiesOwnerComponent.class,
            List.of(
                    ThaumcraftItemsRegistry.SUPPLIER_IRON_WAND_CAP.get().getDefaultInstance(),
                    ThaumcraftItemsRegistry.SUPPLIER_COPPER_WAND_CAP.get().getDefaultInstance(),
                    ThaumcraftItemsRegistry.SUPPLIER_GOLD_WAND_CAP.get().getDefaultInstance(),
                    ThaumcraftItemsRegistry.SUPPLIER_THAUMIUM_WAND_CAP.get().getDefaultInstance(),
                    ThaumcraftItemsRegistry.SUPPLIER_VOID_WAND_CAP.get().getDefaultInstance()
            ));
    public static final RecipeItemMatcher WAND_ROD_MATCHER = ItemClassInstanceOfMatcher.of(WorkAsWandRod.class, List.of(
            ThaumcraftItemsRegistry.SUPPLIER_BLAZE_WAND_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_BONE_WAND_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_WAND_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_WAND_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_OBSIDIAN_WAND_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_ICE_WAND_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_QUARTZ_WAND_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_REED_WAND_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_WOOD_WAND_ROD.get().getDefaultInstance()
    ));
    public static final RecipeItemMatcher STAFF_ROD_MATCHER = ItemClassInstanceOfMatcher.of(WorkAsStaffRod.class, List.of(
            ThaumcraftItemsRegistry.SUPPLIER_BLAZE_STAFF_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_BONE_STAFF_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_GREATWOOD_STAFF_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_SILVERWOOD_STAFF_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_OBSIDIAN_STAFF_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_ICE_STAFF_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_QUARTZ_STAFF_ROD.get().getDefaultInstance(),
            ThaumcraftItemsRegistry.SUPPLIER_REED_STAFF_ROD.get().getDefaultInstance()
    ));
    public static final RecipeItemMatcher PRIMAL_CHARM_MATCHER = ItemMatcher.of(ThaumcraftItemsRegistry.SUPPLIER_PRIMAL_CHARM.get());
    public static final RecipeItemMatcher SCEPTRE_MATCHER = ItemMatcher.of(ThaumcraftItemsRegistry.SUPPLIER_SCEPTRE_CASTING.get());//TODO:Sceptre Item Instance
}
