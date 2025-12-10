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
	public final AspectList aspects;
	public final String key;
	
	public final int hash;

	private final RecipeItemMatcher outputMatcher;

	public CrucibleRecipe(String researchKey, Function<ItemStack,ItemStack> resultGetter, RecipeItemMatcher cat, AspectList tags,RecipeItemMatcher outputMatcher) {
		recipeOutputGetter = resultGetter;
		this.aspects = tags;
		this.key = researchKey;
		this.catalyst = cat;
		StringBuilder hc = new StringBuilder(researchKey /*+result.toString()*/);
		for (Aspect tag:tags.getAspectTypes()) {
			hc.append(tag.getTag()).append(tags.getAmount(tag));
		}
		
		hash = hc.toString().hashCode();
		this.inputSample = catalyst.getAvailableItemStackSample().toArray(new ItemStack[0]);
		this.outputMatcher = outputMatcher;

	}
	
		

	public boolean matches(AspectList itags, ItemStack cat) {
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
		for (Aspect tag:aspects.getAspectTypes()) {
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
	
	public AspectList removeMatching(AspectList itags) {
		AspectList temptags = new AspectList(itags);

//		temptags.aspects.putAll(itags.aspects);
		
		for (Aspect tag:aspects.getAspectTypes()) {
			temptags.remove(tag, aspects.getAmount(tag));
		}
		
		itags = temptags;
		return itags;
	}
	
	public ItemStack getRecipeOutput() {
		return getOutputSample(getInputSample())[0];
	}


	private final ItemStack[] inputSample;
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
}
