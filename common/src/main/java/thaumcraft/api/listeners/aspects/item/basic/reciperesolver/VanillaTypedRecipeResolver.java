package thaumcraft.api.listeners.aspects.item.basic.reciperesolver;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.impls.RecipeResolveContext;

import java.util.*;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public abstract class VanillaTypedRecipeResolver<T extends Recipe<C>,C extends Container> extends AbstractRecipeResolver {
    private final RecipeType<T> recipeType;
    public VanillaTypedRecipeResolver(int weight,RecipeType<T> type) {
        super(weight);
        this.recipeType = type;
    }

    @Override
    public void resolveItems(RecipeResolveContext context) {
        var registryAccess = platformUtils.getServer().registryAccess();
        context.itemsResolvedLastTurnView.forEach(
                item -> {
                    RECIPES_RESOLVED.addAll(ITEM_CRAFTED_FROM_RECIPES.removeAll(item));
                    var recipesToConsider = ITEM_IN_RECIPES.get(item);
                    List<Recipe<C>> recipesToRemove = new ArrayList<>(recipesToConsider.size());
                    recipesToConsider.forEach(recipe -> {
                        if (RECIPES_RESOLVED.contains(recipe)) {
                            recipesToRemove.add(recipe);
                            return;
                        }
                        var resultStack = recipe.getResultItem(registryAccess);
                        var resultCount = resultStack.getCount();
                        if (context.getAlreadyCalculatedAspectForItem(resultStack.getItem()) != null) {
                            recipesToRemove.add(recipe);
                            RECIPES_RESOLVED.add(recipe);
                            return;
                        }
                        if (resultCount == 0){
                            return;
                        }
                        AspectList<Aspect> allAdded = new AspectList<>();
                        AspectList<Aspect> remainingItemAspects = new AspectList<>();
                        for (var ingredient : recipe.getIngredients()) {
                            AspectList<Aspect> minimizedButNotEmpty = UnmodifiableAspectList.EMPTY;
                            for (var stack:ingredient.getItems()) {
                                var ingredientItem = stack.getItem();
                                var remainingItem = ingredientItem.getCraftingRemainingItem();
                                var count = stack.getCount();
                                if (remainingItem != null){
                                    var aspectRemaining = context.getAlreadyCalculatedAspectForItem(remainingItem);
                                    if (aspectRemaining == null) {
                                        return;
                                    }
                                    remainingItemAspects.addAll(aspectRemaining.multiply(count));
                                }
                                var alreadyCalculated = context.getAlreadyCalculatedAspectForItem(ingredientItem);
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
                        context.addResolvedItem(item);
                        allAdded = allAdded.divideAndCeil(recipe.getResultItem(registryAccess).getCount());
                        context.addResolvedAspectForItem(item,new UnmodifiableAspectList<>(allAdded));
                        RECIPES_RESOLVED.add(recipe);
                        recipesToRemove.add(recipe);
                    });
                    recipesToConsider.removeAll(recipesToRemove);
                }
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
                                }
                            }
                        }
                );
    }
}
