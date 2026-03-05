package thaumcraft.api.crafting;

import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.resourcelocations.AbstractRecipeResourceLocation;

public abstract class AbstractResourceLocationIdentifiedRecipe<
        RecipeClass extends AbstractResourceLocationIdentifiedRecipe<RecipeClass,ResourceLocationClass>,
        ResourceLocationClass extends AbstractRecipeResourceLocation<RecipeClass,ResourceLocationClass>
        > {
    public final @NotNull ResourceLocationClass recipeID;
    public AbstractResourceLocationIdentifiedRecipe(@NotNull ResourceLocationClass recipeID) {
        this.recipeID = recipeID;
        registerRecipe(recipeID);
    }

    protected abstract void registerRecipe(ResourceLocationClass recipeID);

}
