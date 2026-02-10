package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver;

import thaumcraft.api.crafting.interfaces.IArcaneRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.IAspectCalculableRecipeResolver;

public class ArcaneCraftingRecipeResolver extends IAspectCalculableRecipeResolver {

    public ArcaneCraftingRecipeResolver(int weight) {
        super(weight,IArcaneRecipe::getIArcaneRecipes);
    }

}
