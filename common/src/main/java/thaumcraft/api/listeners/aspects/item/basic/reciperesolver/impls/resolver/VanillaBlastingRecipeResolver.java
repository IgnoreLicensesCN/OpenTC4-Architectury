package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver;

public class VanillaBlastingRecipeResolver extends VanillaTypedRecipeResolver<BlastingRecipe,Container> {
    public VanillaBlastingRecipeResolver(int weight) {
        super(weight, RecipeType.BLASTING);
    }
}
