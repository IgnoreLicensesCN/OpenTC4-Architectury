package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver;

public class VanillaSmeltingRecipeResolver extends VanillaTypedRecipeResolver<SmeltingRecipe,Container> {
    public VanillaSmeltingRecipeResolver(int weight) {
        super(weight, RecipeType.SMELTING);
    }
}
