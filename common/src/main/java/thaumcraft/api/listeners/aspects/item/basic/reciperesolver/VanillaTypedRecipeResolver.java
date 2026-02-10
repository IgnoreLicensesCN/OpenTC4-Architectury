package thaumcraft.api.listeners.aspects.item.basic.reciperesolver;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils.SameValueList;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.calcstage.RecipeResolveContext;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils.UtilityConsts.VANILLA_RETURN_ITEMS;
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
        resolveItemsCommon(
                context.itemsResolvedLastTurnView,
                RECIPES_RESOLVED,
                ITEM_CRAFTED_FROM_RECIPES,
                ITEM_IN_RECIPES,
                r -> r.getResultItem(registryAccess),
                context::getAlreadyCalculatedAspectForItem,
                r -> r.getIngredients().stream()
                        .map(ingredient -> Arrays.asList(ingredient.getItems()))
                        .toList(),
                r -> VANILLA_RETURN_ITEMS_LIST_LIST,
                context::addResolvedItem,
                context::addResolvedAspectForItem
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
                            for (var ingredient : recipe.getIngredients()) {
                                for (var ingredientStack : ingredient.getItems()) {
                                    if (ingredientStack.getItem() == recipe.getResultItem(registryAccess).getItem()) {
                                        return;
                                    }
                                }
                            }
                            var stack = recipe.getResultItem(registryAccess);
                            if (stack.hasTag()) {
                                ITEM_WITH_NBT_CRAFTED_FROM_RECIPES.computeIfAbsent(stack.getItem(), i -> HashMultimap.create())
                                        .put(stack.getTag(), recipe);
                            }
//                            else {
                                ITEM_CRAFTED_FROM_RECIPES.put(stack.getItem(), recipe);
//                            }
                            for (var ingredient : recipe.getIngredients()) {
                                for (var ingredientStack : ingredient.getItems()) {
                                    ITEM_IN_RECIPES.put(ingredientStack.getItem(), recipe);
                                    if (ingredientStack.getItem().hasCraftingRemainingItem()){
                                        ITEM_IN_RECIPES.put(ingredientStack.getItem().getCraftingRemainingItem(),recipe);
                                    }
                                }
                            }
                        }
                );
    }

    public static <Recipe> void resolveItemsCommon(
            Collection<Item> toSolve,
            Collection<Recipe> resolvedRecipes,
            Multimap<Item, Recipe> itemCraftedFromRecipes,
            Multimap<Item, Recipe> itemInRecipes,
            Function<Recipe, ItemStack> resultItemGetter,
            Function<Item, UnmodifiableAspectList<Aspect>> alreadyCalculatedAspectGetter,
            Function<Recipe, @Unmodifiable List<List<ItemStack>>> ingredientItemsGetter,
            Function<Recipe, @Unmodifiable /* only call get(i) get(j) */
                    //should have same structure as above,like arr[A][B] and arr[A][B]
                    List<List<Function<ItemStack, ItemStack>>>> ingredientRemainingItemsGetter,
            Consumer<Item> resolvedItemAdder,
            BiConsumer<Item, UnmodifiableAspectList<Aspect>> resolvedAspectAdder
    ) {
        resolveItemsCommon(
                toSolve,
                resolvedRecipes,
                itemCraftedFromRecipes,
                itemInRecipes,
                resultItemGetter,
                alreadyCalculatedAspectGetter,
                ingredientItemsGetter,
                ingredientRemainingItemsGetter,
                resolvedItemAdder,
                resolvedAspectAdder,
                aspList -> aspList
        );
    }

    public static <Recipe> void resolveItemsCommon(
            Collection<Item> toSolve,
            Collection<Recipe> resolvedRecipes,
            Multimap<Item,Recipe> itemCraftedFromRecipes,
            Multimap<Item,Recipe> itemInRecipes,
            Function<Recipe, ItemStack> resultItemGetter,
            Function<Item, UnmodifiableAspectList<Aspect>> alreadyCalculatedAspectGetter,
            Function<Recipe,@Unmodifiable List<List<ItemStack>>> ingredientItemsGetter,
            Function<Recipe,@Unmodifiable /* only call get(i) get(j) */
                    //should have same structure as above,like arr[A][B] and arr[A][B]
                    //at least,we will access them.When accessing please give the correct value
                    List<List<Function<ItemStack,ItemStack>>>> ingredientRemainingItemsGetter,
            Consumer<Item> resolvedItemAdder,
            BiConsumer<Item,UnmodifiableAspectList<Aspect>> resolvedAspectAdder,
            Function<AspectList<Aspect>, AspectList<Aspect>> resolvedAspectsModifier
    ) {
        toSolve.forEach(
                item -> {
                    resolvedRecipes.addAll(itemCraftedFromRecipes.removeAll(item));
                    var recipesToConsider = itemInRecipes.get(item);
                    List<Recipe> recipesToRemove = new ArrayList<>(recipesToConsider.size());
                    recipesToConsider.forEach(recipe -> {
                        if (resolvedRecipes.contains(recipe)) {
                            recipesToRemove.add(recipe);
                            return;
                        }
                        var resultStack = resultItemGetter.apply(recipe);//recipe.getResultItem(registryAccess);
                        var resultCount = resultStack.getCount();
                        if (
                                alreadyCalculatedAspectGetter.apply(resultStack.getItem()) != null
                                //context.getAlreadyCalculatedAspectForItem(resultStack.getItem()) != null
                        ) {
                            recipesToRemove.add(recipe);
                            resolvedRecipes.add(recipe);
                            return;
                        }
                        if (resultCount == 0){
                            return;
                        }
                        AspectList<Aspect> allAdded = new AspectList<>();
                        AspectList<Aspect> remainingItemAspects = new AspectList<>();
                        var ingredientItems = ingredientItemsGetter.apply(recipe);
                        var remainingItemList = ingredientRemainingItemsGetter.apply(recipe);

                        for (var possibleItemsIndex=0; possibleItemsIndex<ingredientItems.size(); possibleItemsIndex++) {
                            var possibleItems = ingredientItems.get(possibleItemsIndex);
                            if (possibleItems == null || possibleItems.isEmpty()){
                                continue;
                            }
                            AspectList<Aspect> minimizedButNotEmpty = UnmodifiableAspectList.EMPTY;
                            for (var possibleStackIndex=0;possibleStackIndex<possibleItems.size(); possibleStackIndex++) {
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
                                        return;
                                    }
                                    remainingItemAspects.addAll(aspectRemaining.multiplyAsNew(remainingItem.getCount()));
                                }

                                var alreadyCalculated = alreadyCalculatedAspectGetter.apply(possibleStack.getItem());
                                if (alreadyCalculated == null) {
                                    return;
                                }

                                alreadyCalculated = alreadyCalculated.multiplyAsNew(count);
                                if (!alreadyCalculated.isEmpty()) {
                                    if (minimizedButNotEmpty.isEmpty()) {
                                        minimizedButNotEmpty = alreadyCalculated;
                                    }else if (minimizedButNotEmpty.visSize() > alreadyCalculated.visSize()) {
                                        minimizedButNotEmpty = alreadyCalculated;
                                    }
                                }
                            }
                            if (minimizedButNotEmpty.isEmpty()) {
                                return;
                            }
                            allAdded.addAll(minimizedButNotEmpty);
                        }

                        remainingItemAspects.forEach(allAdded::reduceAndRemoveIfNotPositive);
                        resolvedItemAdder.accept(item);
                        allAdded = allAdded.divideAndCeil(resultStack.getCount());
                        allAdded = resolvedAspectsModifier.apply(allAdded);
                        resolvedAspectAdder.accept(item,new UnmodifiableAspectList<>(allAdded));
                        resolvedRecipes.add(recipe);
                        recipesToRemove.add(recipe);
                    });
                    recipesToConsider.removeAll(recipesToRemove);
                }
        );
    }
}
