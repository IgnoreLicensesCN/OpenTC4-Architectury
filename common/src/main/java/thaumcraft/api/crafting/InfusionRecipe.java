package thaumcraft.api.crafting;

import com.linearity.opentc4.OpenTC4;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import com.linearity.opentc4.recipeclean.recipewrapper.RecipeInAndOutSampler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.aspects.UnmodifiableCentiVisList;
import thaumcraft.api.research.ResearchItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import static com.linearity.opentc4.utils.IndexPicker.pickByTime;

/**
 * <p>i have to say sometimes it's a fool idea to write like this.(inspired by nodalDynamics)</p>
 * <p>
 * if two infusion(i mean matrix in world) with same recipe instance(this is a singleton for each recipe).the last recipe Output will override those before!
 * </p>
 * <p>
 *     so there's something
 *     TODO:store real output into infusion matrix.
 * </p>
 * <p>
 *     --IgnoreLicensesCN
 * </p>
 * **/
public class InfusionRecipe implements RecipeInAndOutSampler, CanMatchViaOutputSample, IAspectCalculableRecipe
{
	//RECIPES/////////////////////////////////////////
//	private static final List<RecipeInAndOutSampler> craftingRecipes = new CopyOnWriteArrayList<>();
	public static final List<InfusionRecipe> infusionRecipes = new CopyOnWriteArrayList<>();
	public static final List<InfusionRecipe> unmodifiableInfusionRecipes = Collections.unmodifiableList(infusionRecipes);
	private static final List<ThaumcraftInfusionEnchantmentRecipe> infusionEnchantmentRecipes = new CopyOnWriteArrayList<>();
	private static final List<ThaumcraftInfusionEnchantmentRecipe> unmodifiableInfusionEnchantmentRecipes = Collections.unmodifiableList(infusionEnchantmentRecipes);
	public final AspectList<Aspect> aspects;
	public final ResearchItem research;
	private final RecipeItemMatcher[] components;
	private final RecipeItemMatcher centralInput;
	private final RecipeItemMatcher recipeOutputMatcher;
//	protected Object recipeOutput;

	//the last in ItemStack[] will be in center
	protected final Function<ItemStack[],ItemStack> recipeOutputGenerator;
	protected final int instability;


	private final boolean supportsAspectCalculation;
	private final @NotNull List<List<ItemStack>> inputForAspectCalculation;
	private final @NotNull ItemStack outputForAspectCalculation;
	private final @NotNull List<List<Function<ItemStack,ItemStack>>> remainingForAspectCalculation;
	public InfusionRecipe(
			ResearchItem research,
			Function<ItemStack[],ItemStack> recipeOutputGenerator,
			int inst,
			AspectList<Aspect> aspects,
			RecipeItemMatcher input,
			RecipeItemMatcher[] recipe,
			RecipeItemMatcher outputMatcher
	){
		this(research,recipeOutputGenerator,inst,aspects,input,recipe,outputMatcher,null,null,null);
	}
	public InfusionRecipe(
			ResearchItem research,
			Function<ItemStack[],ItemStack> recipeOutputGenerator,
			int inst,
			AspectList<Aspect> aspects,
			RecipeItemMatcher input,
			RecipeItemMatcher[] recipe,
			RecipeItemMatcher outputMatcher,
			ItemStack outputForAspectCalculation,
			List<List<ItemStack>> inputForAspectCalculation,
			List<List<Function<ItemStack,ItemStack>>> remainingForAspectCalculation
	) {
		this.research = research;
//		this.recipeOutput = output;
		this.recipeOutputGenerator = recipeOutputGenerator;
		this.centralInput = input;
		this.aspects = aspects;
		this.components = recipe;
		this.componentsSampleArr = new ItemStack[components.length];
		this.recipeInputSampleArr = new ItemStack[components.length + 1];
		this.instability = inst;
		this.recipeOutputMatcher = outputMatcher;

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
		this.remainingForAspectCalculation = remainingForAspectCalculation == null?List.of():remainingForAspectCalculation;
	}

	@UnmodifiableView
	public static List<ThaumcraftInfusionEnchantmentRecipe> getInfusionEnchantmentRecipes() {
		return unmodifiableInfusionEnchantmentRecipes;
	}

	public static InfusionRecipe addInfusionCraftingRecipe(InfusionRecipe r) {
		infusionRecipes.add(r);
		return r;
	}

	public static ThaumcraftInfusionEnchantmentRecipe addInfusionEnchantmentRecipe(ThaumcraftInfusionEnchantmentRecipe r) {
//		InfusionEnchantmentRecipe r= new InfusionEnchantmentRecipe(research, enchantment, instability, aspects, recipe);
//        craftingRecipes.add(r);
		infusionEnchantmentRecipes.add(r);
		return r;
	}

	@Nullable
	public static InfusionRecipe getInfusionRecipe(ItemStack res) {
		for (InfusionRecipe r : infusionRecipes) {
			if (r.matchViaOutput(res)) {
				return r;
			}
		}
		return null;
	}

	@UnmodifiableView
	public static List<InfusionRecipe> getInfusionRecipes() {
		return unmodifiableInfusionRecipes;
	}

	/**
     * Used to check if a recipe matches current crafting inventory
     * @param player crafting it
     */
	public boolean matches(List<ItemStack> input, ItemStack central, Level world, Player player) {

			
		if (research.isPlayerCompletedResearch(player)) {
    		return false;
    	}

		if (input.size() != components.length) {
			return false;
		}
		
		ItemStack i2 = central.copy();
		if (!centralInput.matches(i2)) {
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
    
    public ResearchItem getResearch() {
		return research;
    }
    
	public ItemStack getRecipeInput() {
		return pickByTime(centralInput.getAvailableItemStackSample());
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
	public ItemStack[] getOutputSample(ItemStack[] inputSample) {
		return new ItemStack[0];
	}

	@Override
	public boolean matchViaOutput(ItemStack res) {
		return recipeOutputMatcher.matches(res);
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
		return remainingForAspectCalculation;
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


	public void onInfusionStart(Level atLevel, BlockPos matrixPos,Player playerActivatedInfusion) {}
	public void onInfusionEnd(Level atLevel, BlockPos matrixPos,Player playerActivatedInfusion) {}
}
