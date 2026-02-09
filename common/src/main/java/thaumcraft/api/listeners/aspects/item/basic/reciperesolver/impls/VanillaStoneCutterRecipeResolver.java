package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver;

public class VanillaStoneCutterRecipeResolver extends VanillaTypedRecipeResolver<StonecutterRecipe,Container> {
    public VanillaStoneCutterRecipeResolver(int weight) {
        super(weight, RecipeType.STONECUTTING);
    }
}
