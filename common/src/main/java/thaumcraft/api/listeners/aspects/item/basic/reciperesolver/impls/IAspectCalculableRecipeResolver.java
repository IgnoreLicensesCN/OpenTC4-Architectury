package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import net.minecraft.world.item.Item;
import thaumcraft.api.crafting.interfaces.IArcaneRecipe;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage.RecipeResolveContext;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static thaumcraft.api.crafting.interfaces.IArcaneRecipe.getIArcaneRecipes;
import static thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver.resolveItemsCommon;

public class IAspectCalculableRecipeResolver extends AbstractRecipeResolver {

    public IAspectCalculableRecipeResolver(int weight, Supplier<? extends Collection<? extends IAspectCalculableRecipe>> onReloadRecipesGetter) {
        super(weight);
        this.onReloadRecipesGetter = onReloadRecipesGetter;
    }
//    private final Map<Item, Multimap<CompoundTag, Recipe<?>>> ITEM_WITH_NBT_CRAFTED_FROM_RECIPES = new HashMap<>();
    private final Multimap<Item, IAspectCalculableRecipe> ITEM_CRAFTED_FROM_RECIPES = HashMultimap.create();
    private final Multimap<Item, IAspectCalculableRecipe> ITEM_IN_RECIPES = HashMultimap.create();
    private final Set<IAspectCalculableRecipe> RECIPES_RESOLVED = new HashSet<>();
    private final Supplier<? extends Collection<? extends IAspectCalculableRecipe>> onReloadRecipesGetter;

    @Override
    public void reloadRecipes() {
        for (var recipe:onReloadRecipesGetter.get()){
            if (!recipe.supportsAspectCalculation()){
                continue;
            }
            var inputs = recipe.getAspectCalculationInputs();
            var output = recipe.getAspectCalculationOutput();
            var remaining = recipe.getAspectCalculationRemaining();
//            var aspects = recipe.getAspectCalculationCentiVisList();
            AtomicInteger inputStacksIndex = new AtomicInteger();
            AtomicInteger specificInputStackIndex = new AtomicInteger();
            inputs.forEach(input -> {
                input.forEach(stack -> {
                    if (stack == null || stack.isEmpty()){return;}
                    ITEM_IN_RECIPES.put(stack.getItem(), recipe);
                    var returnStack = remaining.get(inputStacksIndex.get()).get(specificInputStackIndex.get()).apply(stack);
                    if (returnStack == null || returnStack.isEmpty()){return;}
                    ITEM_IN_RECIPES.put(returnStack.getItem(), recipe);
                    specificInputStackIndex.addAndGet(1);
                });
                inputStacksIndex.addAndGet(1);
            });
            ITEM_CRAFTED_FROM_RECIPES.put(output.getItem(), recipe);
        }
    }

    private static final float CRAFTING_ASPECTS_MULTIPLIER = 0.75F;//anazor's strange idea.

    @Override
    public void resolveItems(RecipeResolveContext context) {
        resolveItemsCommon(
                context.itemsResolvedLastTurnView,
                RECIPES_RESOLVED,
                ITEM_CRAFTED_FROM_RECIPES,
                ITEM_IN_RECIPES,
                IAspectCalculableRecipe::getAspectCalculationOutput,
                context::getAlreadyCalculatedAspectForItem,
                IAspectCalculableRecipe::getAspectCalculationInputs,
                IAspectCalculableRecipe::getAspectCalculationRemaining,
                context::addResolvedItem,
                context::addResolvedAspectForItem
//                aspList -> aspList.multiplyAndCeil(CRAFTING_ASPECTS_MULTIPLIER)//i dont want this now
        );
    }
}
