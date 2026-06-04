package thaumcraft.api.crafting.crucible;

import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.StrictItemStackMatcher;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableCentiVisList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.CrucibleRecipeResourceLocation;

import java.util.List;
import java.util.function.Function;

public class SimpleCrucibleRecipe extends CrucibleRecipe {

    public final RecipeItemMatcher catalyst;
    public final UnmodifiableAspectList<Aspect> aspectsRequiring;
    private final RecipeItemMatcher outputMatcher;
    private final ItemStack outStack;

    public SimpleCrucibleRecipe(
            @NotNull CrucibleRecipeResourceLocation recipeID,
            RecipeItemMatcher catalystMatcher,
            UnmodifiableAspectList<Aspect> aspectsRequiring,
            ResearchItem research,
            ItemStack out
    ) {
        super(recipeID,research);
        this.catalyst = catalystMatcher;
        this.aspectsRequiring = aspectsRequiring;
        this.outStack = out.copy();
        this.outputMatcher = new StrictItemStackMatcher(out);
    }

    @Override
    public boolean supportsAspectCalculation() {
        return true;
    }

    @Override
    public AspectList<Aspect> getAspectRequirement(ItemStack cat) {
        return aspectsRequiring;
    }

    @Override
    public AspectList<Aspect> getAspectRequirementMin() {
        return aspectsRequiring;
    }

    @Override
    public boolean catalystMatches(ItemStack cat) {
        return catalyst.matches(cat);
    }

    @Override
    public List<ItemStack> getRecipeOutputExample() {
        return outputMatcher.getAvailableItemStackSample();
    }

    @Override
    public ItemStack getRecipeOutput(ItemStack inStack) {
        return outStack;
    }

    @Override
    public boolean matchViaOutput(ItemStack res) {
        return outputMatcher.matches(res);
    }
    @Override
    public String toString() {
        return "CrucibleRecipe{key=" + research + ",catalyst=" + catalyst + ",output=" + outputMatcher + ",aspects=" + aspectsRequiring + "}";
    }

    private List<List<ItemStack>> inputsCache = null;
    @Override
    public @NotNull List<List<ItemStack>> getAspectCalculationInputs() {
        if (inputsCache == null) {
            inputsCache = List.of(catalyst.getAvailableItemStackSample());
        }
        return inputsCache;
    }

    @Override
    public @NotNull ItemStack getAspectCalculationOutput() {
        return outStack;
    }

    private List<List<Function<ItemStack, ItemStack>>> remainingsCache = null;
    @Override
    public @NotNull List<List<Function<ItemStack, ItemStack>>> getAspectCalculationRemaining() {
        if (remainingsCache == null) {
            remainingsCache = List.of(List.of(this::getRecipeOutput));
        }
        return remainingsCache;
    }

    @Override
    public @NotNull AspectList<Aspect> getAspectCalculationAspectsList() {
        return getAspectRequirementMin();
    }

    @Override
    public @NotNull CentiVisList<Aspect> getAspectCalculationCentiVisList() {
        return UnmodifiableCentiVisList.of();
    }
}
