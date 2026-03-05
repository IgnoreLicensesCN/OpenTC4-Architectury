package thaumcraft.common.lib.resourcelocations;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.crafting.AbstractResourceLocationIdentifiedRecipe;

public abstract class AbstractRecipeResourceLocation<
        RecipeClass extends AbstractResourceLocationIdentifiedRecipe<RecipeClass,ResourceLocationClass>,
        ResourceLocationClass extends AbstractRecipeResourceLocation<RecipeClass,ResourceLocationClass>
        >
        extends VariedResourceLocation<
        RecipeClass, ResourceLocationClass
        > {

    protected AbstractRecipeResourceLocation(String string, String string2, @Nullable Dummy dummy) {
        super(string, string2, dummy);
    }

    public AbstractRecipeResourceLocation(String string, String string2) {
        super(string, string2);
    }

    public AbstractRecipeResourceLocation(String string) {
        super(string);
    }

    public AbstractRecipeResourceLocation(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }


}
