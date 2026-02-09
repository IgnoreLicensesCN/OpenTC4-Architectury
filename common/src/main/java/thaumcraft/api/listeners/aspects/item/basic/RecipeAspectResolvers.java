package thaumcraft.api.listeners.aspects.item.basic;

import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.*;

public enum RecipeAspectResolvers {
    VANILLA_STONE_CUTTER(new VanillaStoneCutterRecipeResolver(100)),
    VANILLA_SMELTING(new VanillaSmeltingRecipeResolver(VANILLA_STONE_CUTTER.resolver.weight + 100)),
    VANILLA_BLASTING(new VanillaBlastingRecipeResolver(VANILLA_SMELTING.resolver.weight + 100)),
    VANILLA_SMOKING(new VanillaSmokingRecipeResolver(VANILLA_BLASTING.resolver.weight + 100)),
    VANILLA_CAMPFIRE(new VanillaCampfireCookingRecipeResolver(VANILLA_SMOKING.resolver.weight + 100)),
    VANILLA_SMITHING(new VanillaSmithingRecipeResolver(VANILLA_CAMPFIRE.resolver.weight + 100)),
    VANILLA_ALL(new VanillaRecipeResolver(1000)),
    //TODO:TC4's RecipeResolver
    ;
    public final AbstractRecipeResolver resolver;

    RecipeAspectResolvers(AbstractRecipeResolver resolver) {
        this.resolver = resolver;
    }
}
