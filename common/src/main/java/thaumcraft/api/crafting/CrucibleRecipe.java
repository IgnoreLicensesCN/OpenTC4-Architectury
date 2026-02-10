package thaumcraft.api.crafting;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import com.linearity.opentc4.recipeclean.recipewrapper.RecipeInAndOutSampler;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.aspects.UnmodifiableCentiVisList;
import thaumcraft.api.research.ResearchItem;

import java.util.List;
import java.util.function.Function;

import static thaumcraft.api.listeners.aspects.item.basic.reciperesolver.calculateutils.UtilityConsts.RETURN_EMPTY_ITEM_LIST_LIST;

public class CrucibleRecipe implements RecipeInAndOutSampler, CanMatchViaOutputSample, IAspectCalculableRecipe {

	private final Function<ItemStack,ItemStack> recipeOutputGetter;

	public final RecipeItemMatcher catalyst;
	public final AspectList<Aspect> aspects;
	public final ResearchItem research;

	public final int hash;


	private final RecipeItemMatcher outputMatcher;
	private final ItemStack[] inputSample;
	private final boolean supportsAspectCalculation;
    private final @NotNull List<List<ItemStack>> inputForAspectCalculation;
    private final @NotNull ItemStack outputForAspectCalculation;

	public CrucibleRecipe(
            ResearchItem researchKey,
            Function<ItemStack,ItemStack> resultGetter,
            RecipeItemMatcher cat,
            AspectList<Aspect> tags,
            RecipeItemMatcher outputMatcher
    ) {
		this(researchKey,resultGetter,cat,tags,outputMatcher,null,null,null);
	}
    public CrucibleRecipe(
            ResearchItem researchKey,
            Function<ItemStack,ItemStack> resultGetter,
            RecipeItemMatcher cat,
            AspectList<Aspect> tags,
            RecipeItemMatcher outputMatcher,
            ItemStack outputForAspectCalculation,
            List<List<ItemStack>> inputForAspectCalculation,
            List<List<Function<ItemStack,ItemStack>>> remainingForAspectCalculation
    ){

        this.recipeOutputGetter = resultGetter;
        this.aspects = tags;
        this.research = researchKey;
        this.catalyst = cat;
        StringBuilder hc = new StringBuilder(researchKey.key.toString());
        for (var asp:tags.getAspectTypes()) {
            hc.append(asp.getAspectKey()).append(tags.getAmount(asp));
        }
        this.hash = hc.toString().hashCode();
        this.inputSample = catalyst.getAvailableItemStackSample().toArray(new ItemStack[0]);
        this.outputMatcher = outputMatcher;
        this.supportsAspectCalculation =
                inputForAspectCalculation != null
                && outputForAspectCalculation != null
                && remainingForAspectCalculation != null;
		if (!this.supportsAspectCalculation
				&& !(inputForAspectCalculation == null
				&& outputForAspectCalculation == null
				&& remainingForAspectCalculation == null
		)
		){
			OpenTC4.LOGGER.warn(
					"""
                            not all aspect calculation elements are null or notnull,
                            this might be a bug or misunderstanding.
                            using researchItem:{}
                            """,research,new Exception());
		}
        this.inputForAspectCalculation = inputForAspectCalculation == null?List.of():inputForAspectCalculation;
        this.outputForAspectCalculation = outputForAspectCalculation == null?ItemStack.EMPTY:outputForAspectCalculation;
    }



	public boolean matches(AspectList<Aspect> itags, ItemStack cat) {
		if (!catalyst.matches(cat)) return false;
//		if (catalyst instanceof ItemStack &&
//				!ThaumcraftApiHelper.itemMatches((ItemStack) catalyst,cat,false)) {
//			return false;
//		} else
//		if (catalyst instanceof ArrayList && !((ArrayList<ItemStack>) catalyst).isEmpty()) {
//			ItemStack[] ores = ((ArrayList<ItemStack>)catalyst).toArray(new ItemStack[]{});
//			if (!ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{cat},ores)) return false;
//		}
		if (itags==null) return false;
		for (var tag:aspects.getAspectTypes()) {
			if (itags.getAmount(tag)<aspects.getAmount(tag)) return false;
		}
		return true;
	}

	public boolean catalystMatches(ItemStack cat) {
		return catalyst.matches(cat);
	}

	public AspectList<Aspect> removeMatching(AspectList<Aspect> itags) {
		AspectList<Aspect> temptags = new AspectList<>(itags);

//		temptags.aspects.putAll(itags.aspects);

		for (var tag:aspects.getAspectTypes()) {
			temptags.reduceAndRemoveIfNotPositive(tag, aspects.getAmount(tag));
		}

		itags = temptags;
		return itags;
	}

	public ItemStack getRecipeOutput() {
		return getOutputSample(getInputSample())[0];
	}


	@Override
	public ItemStack[] getInputSample() {
		return this.inputSample;
	}


	private final ItemStack[] resultStore = new ItemStack[1];
	@Override
	public ItemStack[] getOutputSample(ItemStack[] inputSample) {
		resultStore[0] = recipeOutputGetter.apply(getInputSample()[0]);
		return resultStore;
	}

	@Override
	public boolean matchViaOutput(ItemStack res) {
		return outputMatcher.matches(res);
	}

	@Override
	public boolean supportsAspectCalculation() {
		return supportsAspectCalculation;
	}

	@Override
	public @NotNull List<List<ItemStack>> getAspectCalculationInputs() {
		if (!supportsAspectCalculation){
			throw new RuntimeException("check supportsAspectCalculation() first!");
		}
		return inputForAspectCalculation;
	}

	@Override
	public @NotNull ItemStack getAspectCalculationOutput() {
		if (!supportsAspectCalculation){
			throw new RuntimeException("check supportsAspectCalculation() first!");
		}
		return outputForAspectCalculation;
	}

	@Override
	public @NotNull List<List<Function<ItemStack,ItemStack>>> getAspectCalculationRemaining() {
		if (!supportsAspectCalculation){
			throw new RuntimeException("check supportsAspectCalculation() first!");
		}
		return RETURN_EMPTY_ITEM_LIST_LIST;
	}

	@Override
	public @NotNull AspectList<Aspect> getAspectCalculationAspectsList() {
		if (!supportsAspectCalculation){
			throw new RuntimeException("check supportsAspectCalculation() first!");
		}
		return aspects;
	}

	@Override
	public @NotNull CentiVisList<Aspect> getAspectCalculationCentiVisList() {
		if (!supportsAspectCalculation){
			throw new RuntimeException("check supportsAspectCalculation() first!");
		}
		return UnmodifiableCentiVisList.EMPTY;
	}
}
