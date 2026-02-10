package thaumcraft.api.listeners.aspects.item.basic;

import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.thaumcraft.ArcaneCraftingRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.thaumcraft.CrucibleRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.thaumcraft.InfusionRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.vanilla.VanillaCraftingRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.vanilla.VanillaSmithingRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver.vanilla.VanillaStoneCutterRecipeResolver;

public enum RecipeAspectResolvers {
    VANILLA_STONE_CUTTER(new VanillaStoneCutterRecipeResolver(100)),
//    VANILLA_SMELTING(new VanillaSmeltingRecipeResolver(200)),
//    VANILLA_BLASTING(new VanillaBlastingRecipeResolver(300)),
//    VANILLA_SMOKING(new VanillaSmokingRecipeResolver(400)),
//    VANILLA_CAMPFIRE(new VanillaCampfireCookingRecipeResolver(500)),
    VANILLA_SMITHING(new VanillaSmithingRecipeResolver(600)),
    VANILLA_CRAFTING(new VanillaCraftingRecipeResolver(650)),
//    VANILLA_ALL(new VanillaRecipeResolver(700)),
    CRUCIBLE_RECIPE(new CrucibleRecipeResolver(1000)),
    ARCANE_CRAFTING(new ArcaneCraftingRecipeResolver(1100)),
    INFUSION_RECIPE(new InfusionRecipeResolver(1200)),
    ;
    public final AbstractRecipeResolver resolver;

    RecipeAspectResolvers(AbstractRecipeResolver resolver) {
        this.resolver = resolver;
    }
}
