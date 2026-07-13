package com.linearity.opentc4.mixinaccessors;

import net.minecraft.world.item.crafting.Recipe;

import java.util.Collection;

public interface RecipeManagerAccessor {
    void opentc4$addRecipes(Collection<Recipe<?>> recipes);
}
