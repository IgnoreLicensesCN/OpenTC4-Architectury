package thaumcraft.api.listeners.aspects.item.basic;

import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.ArcaneCraftingRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.VanillaCraftingRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.VanillaSmithingRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.VanillaStoneCutterRecipeResolver;

public enum RecipeAspectResolvers {
    VANILLA_STONE_CUTTER(new VanillaStoneCutterRecipeResolver(100)),
//    VANILLA_SMELTING(new VanillaSmeltingRecipeResolver(200)),
//    VANILLA_BLASTING(new VanillaBlastingRecipeResolver(300)),
//    VANILLA_SMOKING(new VanillaSmokingRecipeResolver(400)),
//    VANILLA_CAMPFIRE(new VanillaCampfireCookingRecipeResolver(500)),
    VANILLA_SMITHING(new VanillaSmithingRecipeResolver(600)),
    VANILLA_CRAFTING(new VanillaCraftingRecipeResolver(650)),
//    VANILLA_ALL(new VanillaRecipeResolver(700)),
    ARCANE_CRAFTING(new ArcaneCraftingRecipeResolver(1000)),

    //TODO:TC4's RecipeResolver
    ;
    public final AbstractRecipeResolver resolver;

    RecipeAspectResolvers(AbstractRecipeResolver resolver) {
        this.resolver = resolver;
    }
}
