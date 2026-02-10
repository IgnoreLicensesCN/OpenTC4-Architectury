package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.thaumcraft;

import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.interfaces.IArcaneRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.IAspectCalculableRecipeResolver;

public class CrucibleRecipeResolver extends IAspectCalculableRecipeResolver {

    public CrucibleRecipeResolver(int weight) {
        super(weight, CrucibleRecipe::getCrucibleRecipes,(recipe,aspList) -> {
            var count = recipe.getAspectCalculationOutput().getCount();
            var aspListAdditional = recipe.getAspectCalculationAspectsList();
            aspListAdditional.forEach((asp,amount) ->
                            aspList.addAll(asp,(int)Math.ceil(Math.sqrt((double) amount /(double) count)))
                    );
            return aspList;
        });
    }

}
