package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.vanilla;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver;

public class VanillaSmokingRecipeResolver extends VanillaTypedRecipeResolver<SmokingRecipe,Container> {
    public VanillaSmokingRecipeResolver(int weight) {
        super(weight, RecipeType.SMOKING);
    }
}
