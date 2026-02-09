package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver;

public class VanillaSmithingRecipeResolver extends VanillaTypedRecipeResolver<SmithingRecipe,Container> {
    public VanillaSmithingRecipeResolver(int weight) {
        super(weight, RecipeType.SMITHING);
    }
}
