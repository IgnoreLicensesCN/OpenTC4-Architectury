package thaumcraft.api.crafting.arcane;

import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.StrictItemStackMatcher;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils.SameValueList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.ShapedArcaneRecipeResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class SimpleShapedArcaneRecipe extends ShapedArcaneRecipe {
    public SimpleShapedArcaneRecipe(
            ShapedArcaneRecipeResourceLocation id,
            ResearchItem research,
            ItemStack result,
            CentiVisList<Aspect> centiVisCost,
            RecipeItemMatcher[] input,
            int width,
            int height
    ) {
        super(id,
                research,
                _ignored -> result,
                _ignored -> centiVisCost,
                input,
                width,
                height,
                new StrictItemStackMatcher(result),
                result,
                Arrays.stream(input).map(RecipeItemMatcher::getAvailableItemStackSample).toList(),
                Arrays.stream(input).map(
                        matcher -> (List<Function<ItemStack,ItemStack>>)
                                new SameValueList<Function<ItemStack,ItemStack>>(matcher.getAvailableItemStackSample().size(),matcher::getRemainingStack)
                        )
                        .toList(),
                centiVisCost
        );
    }
}
