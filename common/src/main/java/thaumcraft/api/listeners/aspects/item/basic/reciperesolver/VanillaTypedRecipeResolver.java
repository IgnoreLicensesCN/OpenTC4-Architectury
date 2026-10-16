package thaumcraft.api.listeners.aspects.item.basic.reciperesolver;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectView;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage.RecipeResolveContext;

import java.util.*;
import java.util.function.Function;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils.UtilityConsts.VANILLA_RETURN_ITEMS_LIST_LIST;

public abstract class VanillaTypedRecipeResolver<T extends Recipe<C>,C extends Container> extends AbstractRecipeResolver {
    private final RecipeType<T> recipeType;
    public VanillaTypedRecipeResolver(int weight,RecipeType<T> type) {
        super(weight);
        this.recipeType = type;
    }

    @Override
    public void resolveItems(RecipeResolveContext context) {
        var registryAccess = platformUtils.getServer().registryAccess();

        ResolveProgressAndRecipeAccessor<Recipe<C>> accessor = new ResolveProgressAndRecipeAccessor<>() {
            @Override
            public ItemStack getResultStack(Recipe<C> recipe) {
                return recipe.getResultItem(registryAccess);
            }

            @Override
            public Collection<Item> getItemsToResolve() {
                return context.itemsResolvedLastTurnView;
            }

            @Override
            public Collection<Recipe<C>> getResolvedRecipes() {
                return RECIPES_RESOLVED;
            }

            @Override
            public Collection<Recipe<C>> getItemsCraftedFromRecipes(Item item) {
                return ITEM_CRAFTED_FROM_RECIPES.get(item);
            }

            @Override
            public Collection<Recipe<C>> getRecipesWhereItemIsInIngredient(Item item) {
                return ITEM_IN_RECIPES.get(item);
            }

            @Override
            public UnmodifiableAspectList<Aspect> getAlreadyCalculatedAspectForItem(Item item) {
                return context.getAlreadyCalculatedAspectForItem(item);
            }

            @Override
            public List<List<ItemStack>> getIngredientItems(Recipe<C> recipe) {
                return recipe.getIngredients().stream()
                        .map(ingredient -> Arrays.asList(ingredient.getItems()))
                        .toList();
            }

            @Override
            public List<List<Function<ItemStack,ItemStack>>> getRemainingItems(Recipe<C> recipe) {
                return VANILLA_RETURN_ITEMS_LIST_LIST;
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
            public AspectList<Aspect> modifyCalculatedAspect(Recipe<C> recipe, AspectList<Aspect> aspectsNotModified) {
                return aspectsNotModified;
            }
        };
        resolveItemsCommon(
                accessor
        );
    }


    private final Map<Item, Multimap<CompoundTag, Recipe<C>>> ITEM_WITH_NBT_CRAFTED_FROM_RECIPES = new HashMap<>();
    private final Multimap<Item, Recipe<C>> ITEM_CRAFTED_FROM_RECIPES = HashMultimap.create();
    private final Multimap<Item, Recipe<C>> ITEM_IN_RECIPES = HashMultimap.create();
    private final Set<Recipe<C>> RECIPES_RESOLVED = new HashSet<>();

    @Override
    public void reloadRecipes() {
        ITEM_CRAFTED_FROM_RECIPES.clear();
        ITEM_WITH_NBT_CRAFTED_FROM_RECIPES.clear();
        ITEM_IN_RECIPES.clear();
        RECIPES_RESOLVED.clear();
        var server = platformUtils.getServer();
        var registryAccess = server.registryAccess();

        server
                .getRecipeManager()
                .getAllRecipesFor(recipeType)
                .forEach(recipe -> {
                            if (anyRecipeIntputSameAsOutput(recipe, registryAccess)) {
                                return;
                            }
                            var stack = recipe.getResultItem(registryAccess);
                            if (stack.hasTag()) {
                                ITEM_WITH_NBT_CRAFTED_FROM_RECIPES.computeIfAbsent(stack.getItem(), i -> HashMultimap.create())
                                        .put(stack.getTag(), recipe);
                            }
//                            else {
                                ITEM_CRAFTED_FROM_RECIPES.put(stack.getItem(), recipe);
//                            }
                            registerRecipeToMap(recipe, ITEM_CRAFTED_FROM_RECIPES);
                        }
                );
    }

    public static <T extends Recipe<C>, C extends Container> void  registerRecipeToMap(T recipe,Multimap<Item,Recipe<C>> ITEM_IN_RECIPES) {
        for (var ingredient : recipe.getIngredients()) {
            for (var ingredientStack : ingredient.getItems()) {
                ITEM_IN_RECIPES.put(ingredientStack.getItem(), recipe);
                if (ingredientStack.getItem().hasCraftingRemainingItem()){
                    ITEM_IN_RECIPES.put(ingredientStack.getItem().getCraftingRemainingItem(), recipe);
                }
            }
        }
    }

    public static <T extends Recipe<C>, C extends Container> boolean anyRecipeIntputSameAsOutput(T recipe, RegistryAccess.Frozen registryAccess) {
        for (var ingredient : recipe.getIngredients()) {
            for (var ingredientStack : ingredient.getItems()) {
                if (ingredientStack.getItem() == recipe.getResultItem(registryAccess).getItem()) {
                    return true;
                }
            }
        }
        return false;
    }

    public interface ResolveProgressAndRecipeAccessor<Recipe> {
        ItemStack getResultStack(Recipe recipe);
        Collection<Item> getItemsToResolve();
        Collection<Recipe> getResolvedRecipes();
        Collection<Recipe> getItemsCraftedFromRecipes(Item item);
        Collection<Recipe> getRecipesWhereItemIsInIngredient(Item item);
        UnmodifiableAspectList<Aspect> getAlreadyCalculatedAspectForItem(Item item);
        //first index:slot,second index:possible itemstack
        List<List<ItemStack>> getIngredientItems(Recipe recipe);
        /* only call get(i) get(j) */
        //should have same structure as above,like arr[A][B] and arr[A][B]
        //at least,we will access them.When accessing please give the correct value
        List<List<Function<ItemStack,ItemStack>>> getRemainingItems(Recipe recipe);
        void addResolvedItem(Item item);
        void addResolvedAspectForItem(Item item,UnmodifiableAspectList<Aspect> aspects);
        //you may want to scale 0.75 here?
        AspectList<Aspect> modifyCalculatedAspect(Recipe recipe,AspectList<Aspect> aspectsNotModified);
    }

    public static <Recipe> void resolveItemsCommon(
            ResolveProgressAndRecipeAccessor<Recipe> resolveProgressAndRecipeAccessor
    ) {
        //only newly-introduced(newly solved last turn,or what defined before first turn) items
        // can lead us to new recipes that we can resolve for this turn,
        //and those are all we may solve in this turn
        resolveProgressAndRecipeAccessor.getItemsToResolve().forEach(
                item -> {
                    var resolvedRecipes = resolveProgressAndRecipeAccessor.getResolvedRecipes();
                    var itemCraftedFromRecipes = resolveProgressAndRecipeAccessor.getItemsCraftedFromRecipes(item);
                    resolvedRecipes.addAll(itemCraftedFromRecipes);
                    itemCraftedFromRecipes.clear();
                    var recipesToConsider = resolveProgressAndRecipeAccessor.getRecipesWhereItemIsInIngredient(item);
                    List<Recipe> recipesToRemove = new ArrayList<>(recipesToConsider.size());
                    recipesToConsider.forEach(recipe -> {
                        if (resolvedRecipes.contains(recipe)) {
                            recipesToRemove.add(recipe);
                            return;
                        }
                        var resultStack = resolveProgressAndRecipeAccessor.getResultStack(recipe);//recipe.getResultItem(registryAccess);
                        if (resultStack.isEmpty()) {
                            return;
                        }
                        if (checkResultCalculated(
                                resolveProgressAndRecipeAccessor::getAlreadyCalculatedAspectForItem,
                                recipe,
                                resultStack,
                                resolvedRecipes,
                                recipesToRemove
                        )) {
                            return;
                        }
                        AspectList<Aspect> allAdded = new LinkedHashAspectList<>();
                        AspectList<Aspect> remainingItemAspects = new LinkedHashAspectList<>();
                        var ingredientItems = resolveProgressAndRecipeAccessor.getIngredientItems(recipe);//ingredientItemsGetter.apply(recipe);
                        var remainingItemList = resolveProgressAndRecipeAccessor.getRemainingItems(recipe);//ingredientRemainingItemsGetter.apply(recipe);

                        if (!addCalculatedAspects(
                                resolveProgressAndRecipeAccessor::getAlreadyCalculatedAspectForItem,
                                ingredientItems,
                                remainingItemList,
                                remainingItemAspects,
                                allAdded)
                        ) {
                            return;
                        }

                        remainingItemAspects.forEach(allAdded::reduceAndRemoveIfNotPositive);
                        resolveProgressAndRecipeAccessor.addResolvedItem(item);
//                        resolvedItemAdder.accept(item);
                        allAdded.divideAndCeil(resultStack.getCount());
                        allAdded = resolveProgressAndRecipeAccessor.modifyCalculatedAspect(recipe,allAdded);//resolvedAspectsModifier.apply(recipe,allAdded);
                        allAdded.removeIfNotPositive();
                        resolveProgressAndRecipeAccessor.addResolvedAspectForItem(item,UnmodifiableAspectList.of(allAdded));
                        resolvedRecipes.add(recipe);
                        recipesToRemove.add(recipe);
                    });
                    recipesToConsider.removeAll(recipesToRemove);
                }
        );
    }

    private static boolean addCalculatedAspects(Function<Item, UnmodifiableAspectList<Aspect>> alreadyCalculatedAspectGetter, List<List<ItemStack>> ingredientItems, List<List<Function<ItemStack, ItemStack>>> remainingItemList, AspectList<Aspect> remainingItemAspects, AspectList<Aspect> allAdded) {
        for (var possibleItemsIndex = 0; possibleItemsIndex< ingredientItems.size(); possibleItemsIndex++) {
            var possibleItems = ingredientItems.get(possibleItemsIndex);
            if (possibleItems == null || possibleItems.isEmpty()){
                continue;
            }
            AspectList<Aspect> minimizedButNotEmpty = getMinimizedAspectsForItems(
                    alreadyCalculatedAspectGetter,
                    possibleItems,
                    remainingItemList,
                    possibleItemsIndex,
                    remainingItemAspects
            );
            if (minimizedButNotEmpty == null) {
                return false;
            }
            if (minimizedButNotEmpty.isEmpty()) {
                return false;
            }
            allAdded.addAll(minimizedButNotEmpty);
        }
        return true;
    }

    private static @Nullable("if cant find") AspectList<Aspect> getMinimizedAspectsForItems(
            Function<Item, UnmodifiableAspectList<Aspect>> alreadyCalculatedAspectGetter, List<ItemStack> possibleItems,
            List<List<Function<ItemStack, ItemStack>>> remainingItemList,
            int possibleItemsIndex,
            AspectList<Aspect> remainingItemAspects
    ) {
        @Unmodifiable AspectList<Aspect> minimizedButNotEmpty = UnmodifiableAspectList.EMPTY;
        for (var possibleStackIndex = 0; possibleStackIndex< possibleItems.size(); possibleStackIndex++) {
            var possibleStack = possibleItems.get(possibleStackIndex);
            if (possibleStack == null || possibleStack.isEmpty()){
                continue;
            }
            var remainingItem = remainingItemList.get(possibleItemsIndex)
                    .get(possibleStackIndex)
                    .apply(possibleStack);
            var count = possibleStack.getCount();
            if (remainingItem != null && !remainingItem.isEmpty()){
                var aspectRemaining = alreadyCalculatedAspectGetter.apply(possibleStack.getItem());
                if (aspectRemaining == null){
                    return null;
                }
                remainingItemAspects.addAll(aspectRemaining.multiplyAsNew(remainingItem.getCount()));
            }

            var alreadyCalculated = alreadyCalculatedAspectGetter.apply(possibleStack.getItem());
            if (alreadyCalculated == null) {
                return null;
            }

            alreadyCalculated = alreadyCalculated.multiplyAsNew(count);
            if (!alreadyCalculated.isEmpty()) {
                if (minimizedButNotEmpty.isEmpty()) {
                    minimizedButNotEmpty = alreadyCalculated;
                }else if (minimizedButNotEmpty.visSize() > alreadyCalculated.visSize()) {
                    minimizedButNotEmpty = alreadyCalculated;
                }
            }
            boolean[] notEnoughAspects = {false};
            if (!remainingItemAspects.isEmpty()) {
                AspectList<Aspect> cached = getAspectsReducedRemaining(
                        remainingItemAspects,
                        minimizedButNotEmpty,
                        notEnoughAspects
                );
                if (notEnoughAspects[0]) {
                    return null;
                }
                minimizedButNotEmpty = new UnmodifiableAspectView<>(cached);
            }
        }
        return minimizedButNotEmpty;
    }

    private static @NotNull AspectList<Aspect> getAspectsReducedRemaining(AspectList<Aspect> remainingItemAspects, AspectList<Aspect> minimizedButNotEmpty, boolean[] notEnoughAspects) {
        AspectList<Aspect> cached = new HashAspectList<>();
        remainingItemAspects.forEach(
                (remainingAspect,remaining) -> {
                    int owned = minimizedButNotEmpty.get(remainingAspect);
                    if (owned < remaining) {
                        notEnoughAspects[0] = true;
                        return;
                    }
                    cached.put(remainingAspect,owned - remaining);
                }
        );
        minimizedButNotEmpty.forEach((owned, ownedAmount) -> {
            if (!cached.containsKey(owned)) {
                cached.put(owned,ownedAmount);
            }
        });
        return cached;
    }

    private static <Recipe> boolean checkResultCalculated(
            Function<Item, UnmodifiableAspectList<Aspect>> alreadyCalculatedAspectGetter,
            Recipe recipe,
            ItemStack resultStack,
            Collection<Recipe> resolvedRecipes,
            List<Recipe> recipesToRemove
    ) {
        if (
                alreadyCalculatedAspectGetter.apply(resultStack.getItem()) != null
                //context.getAlreadyCalculatedAspectForItem(resultStack.getItem()) != null
        ) {
            recipesToRemove.add(recipe);
            resolvedRecipes.add(recipe);
            return true;
        }
        return false;
    }
}
