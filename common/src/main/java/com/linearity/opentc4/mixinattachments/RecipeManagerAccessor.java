package com.linearity.opentc4.mixinattachments;

import net.minecraft.world.item.crafting.Recipe;

import java.util.Collection;

public interface RecipeManagerAccessor {
    void opentc4$addRecipes(Collection<Recipe<?>> recipes);
}
