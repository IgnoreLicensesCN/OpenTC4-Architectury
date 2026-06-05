package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.thaumcraft;

import thaumcraft.api.crafting.arcane.AbstractArcaneRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.IAspectCalculableRecipeResolver;

public class ArcaneCraftingRecipeResolver extends IAspectCalculableRecipeResolver {

    public ArcaneCraftingRecipeResolver(int weight) {
        super(weight, AbstractArcaneRecipe::getAbstractArcaneRecipes);
    }

}
