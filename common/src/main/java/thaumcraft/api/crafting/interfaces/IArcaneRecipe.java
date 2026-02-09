package thaumcraft.api.crafting.interfaces;

import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import com.linearity.opentc4.recipeclean.recipewrapper.RecipeInAndOutSampler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.tiles.abstracts.IArcaneWorkbenchContainer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * dont store crafting result or anything here,it's a singleton.
 * --IgnoreLicensesCN
 */
public interface IArcaneRecipe extends RecipeInAndOutSampler, CanMatchViaOutputSample, IAspectCalculableRecipe
{


    List<IArcaneRecipe> arcaneRecipes = new CopyOnWriteArrayList<>();
    List<IArcaneRecipe> unmodifiableArcaneRecipes = Collections.unmodifiableList(arcaneRecipes);
    List<ShapedArcaneRecipe> shapedArcaneRecipes = new CopyOnWriteArrayList<>();
    List<ShapedArcaneRecipe> unmodifiableShapedArcaneRecipes = Collections.unmodifiableList(shapedArcaneRecipes);
    List<ShapelessArcaneRecipe> shapelessArcaneRecipes = new CopyOnWriteArrayList<>();
    List<ShapelessArcaneRecipe> unmodifiableShapelessArcaneRecipes = Collections.unmodifiableList(shapelessArcaneRecipes);

    @UnmodifiableView
    static List<IArcaneRecipe> getIArcaneRecipes() {
        return unmodifiableArcaneRecipes;
    }

    @UnmodifiableView
    static List<ShapedArcaneRecipe> getShapedArcaneRecipes() {
        return unmodifiableShapedArcaneRecipes;
    }

    @UnmodifiableView
    static List<ShapelessArcaneRecipe> getShapelessArcaneRecipes() {
        return unmodifiableShapelessArcaneRecipes;
    }

    static ShapedArcaneRecipe addArcaneCraftingRecipe(ShapedArcaneRecipe r) {
        shapedArcaneRecipes.add(r);
        arcaneRecipes.add(r);
        return r;
    }

    static void registerIArcaneRecipe(IArcaneRecipe recipe) {
        arcaneRecipes.add(recipe);
        if (recipe instanceof ShapedArcaneRecipe shaped) {
            shapedArcaneRecipes.add(shaped);
        }
        if (recipe instanceof ShapelessArcaneRecipe shapeless) {
            shapelessArcaneRecipes.add(shapeless);
        }
    }

    static ShapelessArcaneRecipe addShapelessArcaneCraftingRecipe(ShapelessArcaneRecipe r) {
        shapelessArcaneRecipes.add(r);
        arcaneRecipes.add(r);
        return r;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     * @param player 
     */
    boolean matches(IArcaneWorkbenchContainer var1, Level world, Player player);

    /**
     * Returns an Item that is the result ofAspectVisList this recipe
     */
    ItemStack getCraftingResult(IArcaneWorkbenchContainer var1);

    /**
     * Returns the size ofAspectVisList the recipe area
     */
    int getRecipeSize();

    ItemStack getRecipeOutput();
    CentiVisList<Aspect> getAspects();
    CentiVisList<Aspect> getAspects(IArcaneWorkbenchContainer var1);
    ResearchItem getResearch();

    
}
