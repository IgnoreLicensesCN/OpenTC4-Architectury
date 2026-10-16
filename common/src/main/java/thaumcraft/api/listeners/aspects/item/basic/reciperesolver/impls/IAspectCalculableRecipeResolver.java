package thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.AbstractRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage.RecipeResolveContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static thaumcraft.api.listeners.aspects.item.basic.reciperesolver.VanillaTypedRecipeResolver.resolveItemsCommon;

public class IAspectCalculableRecipeResolver extends AbstractRecipeResolver {

    public IAspectCalculableRecipeResolver(int weight,Supplier<? extends Collection<? extends IAspectCalculableRecipe>> onReloadRecipesGetter) {
        this(weight,onReloadRecipesGetter,(recipe,aspList) -> aspList);
    }
    public IAspectCalculableRecipeResolver(int weight, Supplier<? extends Collection<? extends IAspectCalculableRecipe>> onReloadRecipesGetter, BiFunction<IAspectCalculableRecipe, AspectList<Aspect>, AspectList<Aspect>> resolvedAspectResultModifier) {
        super(weight);
        this.onReloadRecipesGetter = onReloadRecipesGetter;
        this.resolvedAspectResultModifier = resolvedAspectResultModifier;
    }
//    private final Map<Item, Multimap<CompoundTag, Recipe<?>>> ITEM_WITH_NBT_CRAFTED_FROM_RECIPES = new HashMap<>();
    private final Multimap<Item, IAspectCalculableRecipe> ITEM_CRAFTED_FROM_RECIPES = HashMultimap.create();
    private final Multimap<Item, IAspectCalculableRecipe> ITEM_IN_RECIPES = HashMultimap.create();
    private final Set<IAspectCalculableRecipe> RECIPES_RESOLVED = new HashSet<>();
    private final Supplier<? extends Collection<? extends IAspectCalculableRecipe>> onReloadRecipesGetter;
    private final BiFunction<IAspectCalculableRecipe, AspectList<Aspect>,AspectList<Aspect>> resolvedAspectResultModifier;

    @Override
    public void reloadRecipes() {
//        ITEM_WITH_NBT_CRAFTED_FROM_RECIPES.clear();
        ITEM_CRAFTED_FROM_RECIPES.clear();
        ITEM_IN_RECIPES.clear();
        RECIPES_RESOLVED.clear();
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

    private static final float CRAFTING_ASPECTS_MULTIPLIER = 0.75F;//azanor's strange idea.

    @Override
    public void resolveItems(RecipeResolveContext context) {
        VanillaTypedRecipeResolver.ResolveProgressAndRecipeAccessor<IAspectCalculableRecipe> accessor =
                new VanillaTypedRecipeResolver.ResolveProgressAndRecipeAccessor<>() {
            @Override
            public ItemStack getResultStack(IAspectCalculableRecipe iAspectCalculableRecipe) {
                return iAspectCalculableRecipe.getAspectCalculationOutput();
            }

            @Override
            public Collection<Item> getItemsToResolve() {
                return context.itemsResolvedLastTurnView;
            }

            @Override
            public Collection<IAspectCalculableRecipe> getResolvedRecipes() {
                return RECIPES_RESOLVED;
            }

            @Override
            public Collection<IAspectCalculableRecipe> getItemsCraftedFromRecipes(Item item) {
                return ITEM_CRAFTED_FROM_RECIPES.get(item);
            }

            @Override
            public Collection<IAspectCalculableRecipe> getRecipesWhereItemIsInIngredient(Item item) {
                return ITEM_IN_RECIPES.get(item);
            }

            @Override
            public UnmodifiableAspectList<Aspect> getAlreadyCalculatedAspectForItem(Item item) {
                return context.getAlreadyCalculatedAspectForItem(item);
            }

            @Override
            public List<List<ItemStack>> getIngredientItems(IAspectCalculableRecipe iAspectCalculableRecipe) {
                return iAspectCalculableRecipe.getAspectCalculationInputs();
            }

            @Override
            public List<List<Function<ItemStack, ItemStack>>> getRemainingItems(IAspectCalculableRecipe iAspectCalculableRecipe) {
                return iAspectCalculableRecipe.getAspectCalculationRemaining();
            }

            @Override
            public void addResolvedItem(Item item) {
                context.addResolvedItem(item);
            }

            @Override
            public void addResolvedAspectForItem(Item item, UnmodifiableAspectList<Aspect> aspects) {
                context.addResolvedAspectForItem(item, aspects);
            }

            @Override
            public AspectList<Aspect> modifyCalculatedAspect(IAspectCalculableRecipe iAspectCalculableRecipe, AspectList<Aspect> aspectsNotModified) {
                return resolvedAspectResultModifier.apply(iAspectCalculableRecipe, aspectsNotModified);
            }
        };
        resolveItemsCommon(
                accessor
//                context.itemsResolvedLastTurnView,
//                RECIPES_RESOLVED,
//                ITEM_CRAFTED_FROM_RECIPES,
//                ITEM_IN_RECIPES,
//                IAspectCalculableRecipe::getAspectCalculationOutput,
//                context::getAlreadyCalculatedAspectForItem,
//                IAspectCalculableRecipe::getAspectCalculationInputs,
//                IAspectCalculableRecipe::getAspectCalculationRemaining,
//                context::addResolvedItem,
//                context::addResolvedAspectForItem,
//                resolvedAspectResultModifier
////                (recipe,aspList) -> aspList.multiplyAndCeil(CRAFTING_ASPECTS_MULTIPLIER)//i dont want this now
        );
    }
}
