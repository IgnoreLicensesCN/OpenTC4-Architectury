package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.vanilla;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver;

public class VanillaCampfireCookingRecipeResolver extends VanillaTypedRecipeResolver<CampfireCookingRecipe,Container> {
    public VanillaCampfireCookingRecipeResolver(int weight) {
        super(weight, RecipeType.CAMPFIRE_COOKING);
    }
}
