package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.thaumcraft;

import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.IAspectCalculableRecipeResolver;

public class InfusionRecipeResolver extends IAspectCalculableRecipeResolver {

    public InfusionRecipeResolver(int weight) {
        super(weight, InfusionRecipe::getInfusionRecipes,(recipe,aspList) -> {
            var count = recipe.getAspectCalculationOutput().getCount();
            var aspListAdditional = recipe.getAspectCalculationAspectsList();
            aspListAdditional.forEach((asp,amount) ->
                    aspList.addAll(asp,(int)Math.ceil(Math.sqrt((double) amount /(double) count)))
            );
            return aspList;
        });
    }

}
