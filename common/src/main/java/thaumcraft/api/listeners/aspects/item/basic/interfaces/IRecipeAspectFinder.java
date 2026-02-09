package thaumcraft.api.listeners.aspects.item.basic.interfaces;

import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.RecipeResolveContext;

public interface IRecipeAspectFinder {
    //return newlyResolved after execute this
    void resolveItems(RecipeResolveContext context);
}
