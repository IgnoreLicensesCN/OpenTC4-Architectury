package thaumcraft.api.crafting;

import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.RecipeInAndOutSampler;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.AspectList;

/**
 * dont store crafting result or anything here,it's a singleton.
 * --IgnoreLicensesCN
 */
public interface IArcaneRecipe extends RecipeInAndOutSampler, CanMatchViaOutputSample
{
	
	
    /**
     * Used to check if a recipe matches current crafting inventory
     * @param player 
     */
    boolean matches(Container var1, Level world, Player player);

    /**
     * Returns an Item that is the result of this recipe
     */
    ItemStack getCraftingResult(Container var1);

    /**
     * Returns the size of the recipe area
     */
    int getRecipeSize();

    ItemStack getRecipeOutput();
    AspectList getAspects();
    AspectList getAspects(Container var1);
    String getResearch();

    
}
