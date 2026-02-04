package thaumcraft.api.crafting;

import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.RecipeInAndOutSampler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.linearity.opentc4.utils.IndexPicker.pickByTime;

/**
 * <p>i have to say it's a fool idea to write like this.</p>
 * <p>
 * if two infusion(i mean matrix in world) with same recipe instance(this is a singleton for each recipe).the last recipe Output will override those before!
 * </p>
 * <p>
 *     so there's something
 *     TODO:store real output into infusion matrix.
 * </p>
 * --IgnoreLicensesCN
 * **/
public class InfusionRecipe implements RecipeInAndOutSampler, CanMatchViaOutputSample
{
	protected AspectList<Aspect> aspects;
	protected String research;
	private final RecipeItemMatcher[] components;
	private final ItemStack[][] allSample;
	private final RecipeItemMatcher recipeInput;
	private final RecipeItemMatcher recipeOutputMatcher;
//	protected Object recipeOutput;

	//the last in ItemStack[] will be in center
	protected Function<ItemStack[],ItemStack> recipeOutputGenerator;
	protected int instability;
	
	public InfusionRecipe(String research,Function<ItemStack[],ItemStack> recipeOutputGenerator, int inst,
			AspectList<Aspect> aspects2, RecipeItemMatcher input, RecipeItemMatcher[] recipe,RecipeItemMatcher outputMatcher) {
		this.research = research;
//		this.recipeOutput = output;
		this.recipeOutputGenerator = recipeOutputGenerator;
		this.recipeInput = input;
		this.aspects = aspects2;
		this.components = recipe;
		this.componentsSampleArr = new ItemStack[components.length];
		this.recipeInputSampleArr = new ItemStack[components.length + 1];
		this.instability = inst;
		this.recipeOutputMatcher = outputMatcher;
		this.allSample = new ItemStack[components.length][];
		for (int i = 0; i < components.length; i++){
			allSample[i] = components[i].getAvailableItemStackSample().toArray(new ItemStack[0]);
		}
	}

	/**
     * Used to check if a recipe matches current crafting inventory
     * @param player 
     */
	public boolean matches(List<ItemStack> input, ItemStack central, Level world, Player player) {

			
		if (!research.isEmpty() && !ThaumcraftApiHelper.isResearchComplete(player.getGameProfile().getName(), research)) {
    		return false;
    	}

		if (input.size() != components.length) {
			return false;
		}
		
		ItemStack i2 = central.copy();
		if (!recipeInput.matches(i2)) {
			return false;
		}

		List<ItemStack> ii = new ArrayList<>();
		for (ItemStack is:input) {
			ii.add(is.copy());
		}
		
		for (RecipeItemMatcher matcher:components) {
			boolean b=false;
			for (int a=0;a<ii.size();a++) {
				 i2 = ii.get(a).copy();
				if (matcher.matches(i2)) {
					ii.remove(a);
					b=true;
					break;
				}
			}
			if (!b) return false;
		}
		return ii.isEmpty();
    }
	
//	public static boolean areItemStacksEqual(ItemStack playerInput, ItemStack recipeSpec, boolean fuzzy)
//    {
//		if (playerInput == null) {
//			return recipeSpec == null;
//		}
//		if (recipeSpec == null) return false;
//		if (!ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(playerInput, recipeSpec)) return false;
//		if (fuzzy) {
//			if (ConfigurationHandler.INSTANCE.getInfusionOreDictMode().test(playerInput, recipeSpec)) {
//				return true;
//			}
//		}
//
//		return playerInput.getItem() == recipeSpec.getItem() &&
//				(playerInput.getDamageValue() == recipeSpec.getDamageValue() || recipeSpec.getDamageValue() == 32767) &&
//				playerInput.getCount() <= playerInput.getMaxStackSize();
////		if (playerInput==null && recipeSpec!=null) return false;
////		if (playerInput!=null && recipeSpec==null) return false;
////		if (playerInput==null && recipeSpec==null) return true;
////
////		//nbt
////		boolean t1=ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(playerInput, recipeSpec);
////		if (!t1) return false;
////
////		if (fuzzy) {
////			int od = OreDictionary.getOreID(playerInput);
////			if (od!=-1) {
////				ItemStack[] ores = OreDictionary.getOres(od).toArray(new ItemStack[]{});
////				if (ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{recipeSpec}, ores))
////					return true;
////			}
////		}
////
////		//damage
////		boolean damage = playerInput.getDamageValue() == recipeSpec.getDamageValue() ||
////				recipeSpec.getDamageValue() == OreDictionary.WILDCARD_VALUE;
////
////        return playerInput.getItem() == recipeSpec.getItem() && (damage && playerInput.stackSize <= playerInput.getMaxStackSize());
//    }
	   
    public ItemStack getRecipeOutput() {
		return getRecipeOutput(this.getRecipeInput());
    }
    
    public AspectList<Aspect> getAspects() {
		return getAspects(this.getRecipeInput());
    }

    public int getInstability() {
		return getInstability(this.getRecipeInput());
    }
    
    public String getResearch() {
		return research;
    }
    
	public ItemStack getRecipeInput() {
		return pickByTime(recipeInput.getAvailableItemStackSample());
//		List<ItemStack> sample = recipeInput.getAvailableItemStackSample();
//		return sample.get(indexByTime(sample.size()));
	}

	private final ItemStack[] componentsSampleArr;
	public ItemStack[] getComponents() {
		for (int i=0;i<components.length;i++) {
			componentsSampleArr[i] = pickByTime(components[i].getAvailableItemStackSample());
		}
		return componentsSampleArr;
	}
	
	public ItemStack getRecipeOutput(ItemStack input) {
		recipeInputSampleArr[recipeInputSampleArr.length-1] = input;
		for (int i=0;i<components.length;i++) {
			recipeInputSampleArr[i] = pickByTime(components[i].getAvailableItemStackSample());
		}
		return recipeOutputGenerator.apply(recipeInputSampleArr);
    }
    
    public AspectList<Aspect> getAspects(ItemStack input) {
		return aspects;
    }
    
    public int getInstability(ItemStack input) {
		return instability;
    }

	private final ItemStack[] recipeInputSampleArr;
	@Override
	public ItemStack[] getInputSample() {
		recipeInputSampleArr[recipeInputSampleArr.length-1] = getRecipeInput();
		for (int i=0;i<components.length;i++) {
			recipeInputSampleArr[i] = pickByTime(components[i].getAvailableItemStackSample());
		}
		return recipeInputSampleArr;
	}

	@Override
	public ItemStack[][] getAllInputSample() {
		return allSample;
	}

	@Override
	public ItemStack[] getOutputSample(ItemStack[] inputSample) {
		return new ItemStack[0];
	}

	@Override
	public boolean matchViaOutput(ItemStack res) {
		return recipeOutputMatcher.matches(res);
	}
}
