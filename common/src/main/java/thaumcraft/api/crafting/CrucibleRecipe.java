package thaumcraft.api.crafting;

import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.RecipeInAndOutSampler;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.function.Function;

public class CrucibleRecipe implements RecipeInAndOutSampler, CanMatchViaOutputSample {

	private final Function<ItemStack,ItemStack> recipeOutputGetter;
	
	public final RecipeItemMatcher catalyst;
	public final AspectList<Aspect> aspects;
	public final String key;
	
	public final int hash;

	private final RecipeItemMatcher outputMatcher;
	private final ItemStack[] inputSample;
	private final ItemStack[][] allSample;

	public CrucibleRecipe(String researchKey, Function<ItemStack,ItemStack> resultGetter, RecipeItemMatcher cat, AspectList<Aspect> tags,RecipeItemMatcher outputMatcher) {
		recipeOutputGetter = resultGetter;
		this.aspects = tags;
		this.key = researchKey;
		this.catalyst = cat;
		StringBuilder hc = new StringBuilder(researchKey /*+result.toString()*/);
		for (var asp:tags.getAspectTypes()) {
			hc.append(asp.getTag()).append(tags.getAmount(asp));
		}
		
		hash = hc.toString().hashCode();
		this.inputSample = catalyst.getAvailableItemStackSample().toArray(new ItemStack[0]);
		this.outputMatcher = outputMatcher;
		this.allSample = new ItemStack[1][];
		allSample[0] = inputSample;

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
//		if (catalyst instanceof ItemStack && ThaumcraftApiHelper.itemMatches((ItemStack) catalyst,cat,false)) {
//			return true;
//		} else
//		if (catalyst instanceof ArrayList && !((ArrayList<ItemStack>) catalyst).isEmpty()) {
//			ItemStack[] ores = ((ArrayList<ItemStack>)catalyst).toArray(new ItemStack[]{});
//            return ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{cat}, ores);
//		}
		return catalyst.matches(cat);
	}
	
	public AspectList<Aspect> removeMatching(AspectList<Aspect> itags) {
		AspectList<Aspect> temptags = new AspectList<>(itags);

//		temptags.aspects.putAll(itags.aspects);
		
		for (var tag:aspects.getAspectTypes()) {
			temptags.reduceAndRemoveIfNegative(tag, aspects.getAmount(tag));
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


	@Override
	public ItemStack[][] getAllInputSample() {
		return allSample;
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
}
