package com.linearity.opentc4.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FastCrucibleRecipeMatcher {
    //size->visSize->recipe
    private static final Comparator<Integer> integerComparator = (a,b) -> Integer.compare(b, a);
    private final Int2ObjectRBTreeMap<Int2ObjectRBTreeMap<List<CrucibleRecipe>>> recipeMap = new Int2ObjectRBTreeMap<>(integerComparator);

    public void registerRecipe(CrucibleRecipe recipe) {
        recipeMap.computeIfAbsent(
                recipe.aspects.size(),
                k -> new Int2ObjectRBTreeMap<>(integerComparator))
                .computeIfAbsent(
                        recipe.aspects.visSize(),
                        k -> new ArrayList<>())
                .add(recipe);
    }

    @Nullable
    public CrucibleRecipe findRecipeCanUse(Player player, ItemStack stack, AspectList<Aspect> aspectList) {
        final int currentSize = aspectList.size();
        final int currentVisSize = aspectList.visSize();

        var sizeBasedMaps = recipeMap.tailMap(currentSize);

        for (var visSizeBasedMap : sizeBasedMaps.values()) {

            Int2ObjectSortedMap<List<CrucibleRecipe>> eligibleVisRecipes = visSizeBasedMap.tailMap(currentVisSize);

            for (var visEntry : eligibleVisRecipes.int2ObjectEntrySet()) {
                List<CrucibleRecipe> recipes = visEntry.getValue();

                for (CrucibleRecipe recipe : recipes) {
                    if (recipe.research.isPlayerCompletedResearch(player) && recipe.matches(aspectList, stack)) {
                        return recipe;
                    }
                }
            }
        }
        return null;
    }
}
