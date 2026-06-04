package thaumcraft.api.crafting.infusion;

import com.linearity.opentc4.annotations.JEILikeOnly;
import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.crafting.AbstractResourceLocationIdentifiedRecipe;
import thaumcraft.api.crafting.SimpleInfusionEnchantmentRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.resourcelocations.InfusionRecipeResourceLocation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
public abstract class InfusionRecipe extends AbstractResourceLocationIdentifiedRecipe<InfusionRecipe, InfusionRecipeResourceLocation>
		implements
		CanMatchViaOutputSample,
		IAspectCalculableRecipe
{
	//RECIPES/////////////////////////////////////////
//	private static final List<RecipeInAndOutSampler> craftingRecipes = new CopyOnWriteArrayList<>();
	public static final List<InfusionRecipe> infusionRecipes = new CopyOnWriteArrayList<>();
	public static final List<InfusionRecipe> unmodifiableInfusionRecipes = Collections.unmodifiableList(infusionRecipes);
	private static final List<SimpleInfusionEnchantmentRecipe> infusionEnchantmentRecipes = new CopyOnWriteArrayList<>();
	private static final List<SimpleInfusionEnchantmentRecipe> unmodifiableInfusionEnchantmentRecipes = Collections.unmodifiableList(infusionEnchantmentRecipes);

	public InfusionRecipe(@NotNull InfusionRecipeResourceLocation recipeID) {
		super(recipeID);
	}


//	protected Object recipeOutput;

	//the last in ItemStack[] will be in center

//	public InfusionRecipe(
//			InfusionRecipeResourceLocation id,
//			ResearchItem research,
//			Function<ItemStack[],ItemStack> recipeOutputGenerator,
//			int inst,
//			AspectList<Aspect> aspects,
//			RecipeItemMatcher input,
//			RecipeItemMatcher[] recipe,
//			RecipeItemMatcher outputMatcher
//	){
//		this(id,research,recipeOutputGenerator,inst,aspects,input,recipe,outputMatcher,null,null,null);
//	}
//	public InfusionRecipe(
//			InfusionRecipeResourceLocation id,
//			ResearchItem research,
//			Function<ItemStack[],ItemStack> recipeOutputGenerator,
//			int inst,
//			AspectList<Aspect> aspects,
//			RecipeItemMatcher input,
//			RecipeItemMatcher[] recipe,
//			RecipeItemMatcher outputMatcher,
//			ItemStack outputForAspectCalculation,
//			List<List<ItemStack>> inputForAspectCalculation,
//			List<List<Function<ItemStack,ItemStack>>> remainingForAspectCalculation
//	) {
//		super(id);
//		this.research = research;
////		this.recipeOutput = output;
//		this.recipeOutputGenerator = recipeOutputGenerator;
//		this.centralInput = input;
//		this.aspects = aspects;
//		this.components = recipe;
//		this.componentsSampleArr = new ItemStack[components.length];
//		this.recipeInputSampleArr = new ItemStack[components.length + 1];
//		this.instability = inst;
//		this.recipeOutputMatcher = outputMatcher;
//
//		this.supportsAspectCalculation =
//				inputForAspectCalculation != null
//						&& outputForAspectCalculation != null
//						&& remainingForAspectCalculation != null;
//		if (!this.supportsAspectCalculation
//				&& !(inputForAspectCalculation == null
//				&& outputForAspectCalculation == null
//				&& remainingForAspectCalculation == null
//		)
//		){
//			OpenTC4.LOGGER.warn(
//					"""
//                            not all aspect calculation elements are null or notnull,
//                            this might be a bug or misunderstanding.
//                            using researchItem:{}
//                            """,research,new Exception());
//		}
//		this.inputForAspectCalculation = inputForAspectCalculation == null?List.of():inputForAspectCalculation;
//		this.outputForAspectCalculation = outputForAspectCalculation == null?ItemStack.EMPTY:outputForAspectCalculation;
//		this.remainingForAspectCalculation = remainingForAspectCalculation == null?List.of():remainingForAspectCalculation;
//	}

	@UnmodifiableView
	public static List<SimpleInfusionEnchantmentRecipe> getInfusionEnchantmentRecipes() {
		return unmodifiableInfusionEnchantmentRecipes;
	}

	public static InfusionRecipe addInfusionCraftingRecipe(InfusionRecipe r) {
		infusionRecipes.add(r);
		return r;
	}

	public static SimpleInfusionEnchantmentRecipe addInfusionEnchantmentRecipe(SimpleInfusionEnchantmentRecipe r) {
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
	public abstract boolean matches(List<ItemStack> input, ItemStack central, Level world, Player player,BlockPos probablyInfusionMatrixPos);

	@UtilityLikeAbstraction(reason = "lazy writing")
	public static boolean simpleMatch(
			List<ItemStack> input,
			ItemStack central,
			Player player,
			ResearchItem research,
			@Unmodifiable List<RecipeItemMatcher> componentMatchers,
			RecipeItemMatcher centralMatcher
	) {
		//infusionEnchantment check XP here
		if (research.isPlayerCompletedResearch(player)) {
			return false;
		}

		if (input.size() != componentMatchers.size()) {
			return false;
		}

		if (!centralMatcher.matches(central)) {
			return false;
		}

		List<ItemStack> stacksToMatch = new ArrayList<>(input);

		ItemStack i2;
		for (RecipeItemMatcher matcher:componentMatchers) {
			boolean matcherMatched=false;
			for (int a=0;a<stacksToMatch.size();a++) {
				i2 = stacksToMatch.get(a).copy();
				if (matcher.matches(i2)) {
					stacksToMatch.remove(a);
					matcherMatched=true;
					break;
				}
			}
			if (!matcherMatched) return false;
		}
		return stacksToMatch.isEmpty();
	}

	@JEILikeOnly
    public ItemStack getExampleRecipeOutput() {
		return getRecipeOutput(pickByTime(this.getExampleRecipeInput()));
    }
	@JEILikeOnly
    public AspectList<Aspect> getAspectsExample() {
		return getAspects(pickByTime(this.getExampleRecipeInput()));
    }
	@JEILikeOnly
    public int getInstabilityExample() {
		return getInstability(pickByTime(this.getExampleRecipeInput()));
    }
    
    public abstract ResearchItem getResearch();
    
	public abstract List<ItemStack> getExampleRecipeInput();

	public abstract ItemStack getRecipeOutput(ItemStack input);
    public abstract AspectList<Aspect> getAspects(ItemStack input);
    public abstract int getInstability(ItemStack input);


	public void onInfusionStart(Level atLevel, BlockPos matrixPos, @Nullable String playerNameActivatedInfusion, ItemStack centralStack) {
		//infusion enchanting cost XP here
		atLevel.playSound(null,matrixPos, ThaumcraftSounds.CRAFT_START, SoundSource.BLOCKS, 0.5F, 1.0F);
	}
	public void onInfusionEnd(Level atLevel, BlockPos matrixPos,@Nullable String playerNameActivatedInfusion, ItemStack centralStack) {

	}

	private static final Map<InfusionRecipeResourceLocation,InfusionRecipe> INFUSION_RECIPES = new ConcurrentHashMap<>();
	@Unmodifiable
	public static final Map<InfusionRecipeResourceLocation,InfusionRecipe> INFUSION_RECIPES_VIEW = Collections.unmodifiableMap(INFUSION_RECIPES);
	@Override
	protected void registerRecipe(InfusionRecipeResourceLocation recipeID) {
		var got = INFUSION_RECIPES.get(recipeID);
		if (got != null) {
			throw new RuntimeException("duplicate recipe ID: " + recipeID + " for " + got + " and " + this);
		}
		INFUSION_RECIPES.put(recipeID, this);
	}

	public abstract List<ItemStack> getRemainingStacks(List<ItemStack> inputsNotCenter);
}
