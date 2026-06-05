package thaumcraft.api.crafting.arcane;

import com.linearity.opentc4.annotations.JEILikeOnly;
import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.crafting.AbstractResourceLocationIdentifiedRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.AbstractArcaneRecipeResourceLocation;
import thaumcraft.common.tiles.abstracts.IArcaneWorkbenchContainer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * dont store crafting result or anything here,it's a singleton.
 * --IgnoreLicensesCN
 */
public abstract class AbstractArcaneRecipe 
        extends AbstractResourceLocationIdentifiedRecipe<AbstractArcaneRecipe, AbstractArcaneRecipeResourceLocation> 
        implements CanMatchViaOutputSample,
        IAspectCalculableRecipe
{
    public static final List<AbstractArcaneRecipe> arcaneRecipes = new CopyOnWriteArrayList<>();
    public static final List<AbstractArcaneRecipe> unmodifiableArcaneRecipes = Collections.unmodifiableList(arcaneRecipes);
    public static final List<ShapedArcaneRecipe> shapedArcaneRecipes = new CopyOnWriteArrayList<>();
    public static final List<ShapedArcaneRecipe> unmodifiableShapedArcaneRecipes = Collections.unmodifiableList(shapedArcaneRecipes);
    public static final List<ShapelessArcaneRecipe> shapelessArcaneRecipes = new CopyOnWriteArrayList<>();
    public static final List<ShapelessArcaneRecipe> unmodifiableShapelessArcaneRecipes = Collections.unmodifiableList(shapelessArcaneRecipes);

    public AbstractArcaneRecipe(@NotNull AbstractArcaneRecipeResourceLocation recipeID) {
        super(recipeID);
    }


    private static final Map<AbstractArcaneRecipeResourceLocation,AbstractArcaneRecipe> ARCANE_RECIPES = new ConcurrentHashMap<>();
    @Unmodifiable
    public static final Map<AbstractArcaneRecipeResourceLocation,AbstractArcaneRecipe> ARCANE_RECIPES_VIEW = Collections.unmodifiableMap(ARCANE_RECIPES);

    @UnmodifiableView
    public static List<AbstractArcaneRecipe> getAbstractArcaneRecipes() {
        return unmodifiableArcaneRecipes;
    }

    @UnmodifiableView
    public static List<ShapedArcaneRecipe> getShapedArcaneRecipes() {
        return unmodifiableShapedArcaneRecipes;
    }

    @UnmodifiableView
    public static List<ShapelessArcaneRecipe> getShapelessArcaneRecipes() {
        return unmodifiableShapelessArcaneRecipes;
    }

    public static ShapedArcaneRecipe addArcaneCraftingRecipe(ShapedArcaneRecipe r) {
        shapedArcaneRecipes.add(r);
        arcaneRecipes.add(r);
        return r;
    }

    public static void registerAbstractArcaneRecipe(AbstractArcaneRecipe recipe) {
        arcaneRecipes.add(recipe);
        if (recipe instanceof ShapedArcaneRecipe shaped) {
            shapedArcaneRecipes.add(shaped);
        }
        if (recipe instanceof ShapelessArcaneRecipe shapeless) {
            shapelessArcaneRecipes.add(shapeless);
        }
    }

    public static ShapelessArcaneRecipe addShapelessArcaneCraftingRecipe(ShapelessArcaneRecipe r) {
        shapelessArcaneRecipes.add(r);
        arcaneRecipes.add(r);
        return r;
    }

    @Override
    protected void registerRecipe(AbstractArcaneRecipeResourceLocation recipeID) {
        var got = ARCANE_RECIPES.get(recipeID);
        if (got != null) {
            throw new RuntimeException("duplicate recipe ID: " + recipeID + " for " + got + " and " + this);
        }
        ARCANE_RECIPES.put(recipeID, this);
    }

    public final @NotNull AbstractArcaneRecipeResourceLocation getRecipeID() {
        return recipeID;
    }

    /**
     * Used to check if a recipe matches current crafting inventory(and research condition meets and more)
     */
    public abstract boolean matches(IArcaneWorkbenchContainer var1, Level world, Player player);

    /**
     * Returns an Item that is the result of this recipe
     */
    public abstract ItemStack getCraftingResult(IArcaneWorkbenchContainer var1);

    public void afterCrafting(IArcaneWorkbenchContainer var1, Level world, Player player){};

    /**
     * Returns the size of the recipe area
     */
    public abstract int getRecipeSize();

    @JEILikeOnly
    public abstract ItemStack getRecipeOutputExample();
    @JEILikeOnly
    public abstract CentiVisList<Aspect> getAspectsExample();
    public abstract CentiVisList<Aspect> getAspects(IArcaneWorkbenchContainer var1);
    public abstract ResearchItem getResearch();

}
