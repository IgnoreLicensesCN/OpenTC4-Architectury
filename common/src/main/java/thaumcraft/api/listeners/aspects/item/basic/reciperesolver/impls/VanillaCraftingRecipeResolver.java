package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver;

public class VanillaCraftingRecipeResolver extends VanillaTypedRecipeResolver<CraftingRecipe,CraftingContainer> {
    public VanillaCraftingRecipeResolver(int weight) {
        super(weight, RecipeType.CRAFTING);
    }
}
