package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.resolver;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import thaumcraft.api.crafting.interfaces.IArcaneRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage.RecipeResolveContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static thaumcraft.api.crafting.interfaces.IArcaneRecipe.getIArcaneRecipes;

public class ArcaneCraftingRecipeResolver extends AbstractRecipeResolver {

    public ArcaneCraftingRecipeResolver(int weight) {
        super(weight);
    }
//    private final Map<Item, Multimap<CompoundTag, Recipe<?>>> ITEM_WITH_NBT_CRAFTED_FROM_RECIPES = new HashMap<>();
    private final Multimap<Item, IArcaneRecipe> ITEM_CRAFTED_FROM_RECIPES = HashMultimap.create();
    private final Multimap<Item, IArcaneRecipe> ITEM_IN_RECIPES = HashMultimap.create();
    private final Set<IArcaneRecipe> RECIPES_RESOLVED = new HashSet<>();

    @Override
    public void reloadRecipes() {
        for (var recipe:getIArcaneRecipes()){
            if (!recipe.supportsAspectCalculation()){
                continue;
            }
            var inputs = recipe.getAspectCalculationInputs();
            var output = recipe.getAspectCalculationOutput();
            var remaining = recipe.getAspectCalculationRemaining();
//            var aspects = recipe.getAspectCalculationCentiVisList();
            inputs.forEach(input -> {
                input.forEach(stack -> {
                    if (stack == null || stack.isEmpty()){return;}
                    ITEM_IN_RECIPES.put(stack.getItem(), recipe);
                });
            });
            remaining.forEach(input -> {
                input.forEach(stack -> {
                    if (stack == null || stack.isEmpty()){return;}
                    ITEM_IN_RECIPES.put(stack.getItem(), recipe);
                });
            });
            ITEM_CRAFTED_FROM_RECIPES.put(output.getItem(), recipe);

        }

    }
    @Override
    public void resolveItems(RecipeResolveContext context) {
        //TODO
    }
}
