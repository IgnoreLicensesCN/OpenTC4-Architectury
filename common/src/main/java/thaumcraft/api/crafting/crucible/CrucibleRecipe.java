package thaumcraft.api.crafting.crucible;

import com.linearity.opentc4.recipeclean.recipewrapper.CanMatchViaOutputSample;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import com.linearity.opentc4.utils.FastCrucibleRecipeMatcher;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import tc4tweak.modules.findCrucibleRecipe.FindCrucibleRecipe;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspectlists.*;
import thaumcraft.api.crafting.AbstractResourceLocationIdentifiedRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.resourcelocations.CrucibleRecipeResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

//TODO:Clean
public abstract class CrucibleRecipe
		extends AbstractResourceLocationIdentifiedRecipe<CrucibleRecipe, CrucibleRecipeResourceLocation>
		implements
		CanMatchViaOutputSample,
		IAspectCalculableRecipe {

	private static final List<CrucibleRecipe> crucibleRecipes = new CopyOnWriteArrayList<>();
	private static final List<CrucibleRecipe> unmodifiableCrucibleRecipes = Collections.unmodifiableList(crucibleRecipes);
	private static final FastCrucibleRecipeMatcher fastCrucibleRecipeMatcher = new FastCrucibleRecipeMatcher();

	//if dynamic,plz set this
	// to least aspects and vis size of this recipe
	// and override #matches to match dynamic condition.
	// to ensure fastCrucibleRecipeMatcher works

	public final ResearchItem research;
	public CrucibleRecipe(@NotNull CrucibleRecipeResourceLocation recipeID,ResearchItem research) {
		super(recipeID);
		this.research = research;
	}
	//will auto register
//	public CrucibleRecipe(
//			CrucibleRecipeResourceLocation id,
//            ResearchItem researchKey,
//            Function<ItemStack,ItemStack> resultGetter,
//            RecipeItemMatcher cat,
//			UnmodifiableAspectList<Aspect> tags,
//            RecipeItemMatcher outputMatcher
//    ) {
//		this(id,researchKey,resultGetter,cat,tags,outputMatcher,null,null,null);
//	}
//    public CrucibleRecipe(
//			CrucibleRecipeResourceLocation id,
//            ResearchItem researchKey,
//            Function<ItemStack,ItemStack> resultGetter,
//            RecipeItemMatcher cat,
//			UnmodifiableAspectList<Aspect> tags,
//            RecipeItemMatcher outputMatcher,
//            ItemStack outputForAspectCalculation,
//            List<List<ItemStack>> inputForAspectCalculation,
//            List<List<Function<ItemStack,ItemStack>>> remainingForAspectCalculation
//    ){
//		super(id);
//        this.recipeOutputGetter = resultGetter;
//        this.aspectsRequiring = tags;
//        this.research = researchKey;
//        this.catalyst = cat;
//        StringBuilder hc = new StringBuilder(researchKey.key.toString());
//        for (var asp:tags.keySet()) {
//            hc.append(asp.getAspectKey()).append(tags.get(asp));
//        }
//        this.hash = hc.toString().hashCode();
//        this.inputSample = catalyst.getAvailableItemStackSample().toArray(new ItemStack[0]);
//        this.outputMatcher = outputMatcher;
//        this.supportsAspectCalculation =
//                inputForAspectCalculation != null
//                && outputForAspectCalculation != null
//                && remainingForAspectCalculation != null;
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
//        this.inputForAspectCalculation = inputForAspectCalculation == null?List.of():inputForAspectCalculation;
//        this.outputForAspectCalculation = outputForAspectCalculation == null?ItemStack.EMPTY:outputForAspectCalculation;
//    }

	public static @UnmodifiableView List<CrucibleRecipe> getCrucibleRecipes() {
		return unmodifiableCrucibleRecipes;
	}

	/**
	 * @param stack the recipe result
	 * @return the recipe
	 */
	public static @Nullable CrucibleRecipe getCrucibleRecipe(ItemStack stack) {
		for (CrucibleRecipe crucibleRecipe : getCrucibleRecipes()) {
			if (crucibleRecipe.matchViaOutput(stack)) {
				return crucibleRecipe;
			}
		}
		return null;
	}

	/**
	 * @param hash the unique recipe code
	 * @return the recipe
	 */
	@Deprecated(forRemoval=true)
	public static CrucibleRecipe getCrucibleRecipeFromHash(int hash) {
		return FindCrucibleRecipe.getCrucibleRecipeFromHash(hash);
	}


	public boolean matches(@NotNull AspectList<Aspect> aspectsForRecipe, ItemStack cat) {
		if (!catalystMatches(cat)) return false;
		return aspectRequirementMatches(aspectsForRecipe, cat);
	}

	public boolean aspectRequirementMatches(@NotNull AspectList<Aspect> aspectsForRecipe, ItemStack in) {
		return !getAspectRequirement(in).forEachWithBreak(
				aspectsForRecipe::containsEnough
		);
	}
	//this is for dynamic aspect requirement recipe
	public abstract AspectList<Aspect> getAspectRequirement(ItemStack cat);
	public abstract AspectList<Aspect> getAspectRequirementMin();

	public abstract boolean catalystMatches(ItemStack cat);

	public AspectList<Aspect> removeMatchingReturnNew(AspectList<Aspect> toRemoveMatching,ItemStack in) {
		AspectList<Aspect> result = new LinkedHashAspectList<>();
		result.addAll(toRemoveMatching);
		for (var tag: getAspectRequirement(in).keySet()) {
			result.reduceAndRemoveIfNotPositive(tag, getAspectRequirement(in).get(tag));
		}
		return result;
	}

	public void removeMatching(AspectList<Aspect> aspectsToRemove, ItemStack in) {
		for (var tag: getAspectRequirement(in).keySet()) {
			aspectsToRemove.reduceAndRemoveIfNotPositive(tag, getAspectRequirement(in).get(tag));
		}
	}
	public abstract List<ItemStack> getRecipeOutputExample();

	@Override
	public abstract boolean matchViaOutput(ItemStack res);


	private static final Map<CrucibleRecipeResourceLocation,CrucibleRecipe> CRUCIBLE_RECIPES = new ConcurrentHashMap<>();
	@Unmodifiable
	public static final Map<CrucibleRecipeResourceLocation,CrucibleRecipe> CRUCIBLE_RECIPES_VIEW = Collections.unmodifiableMap(CRUCIBLE_RECIPES);

	//if you want a fake recipe plz override this and do not register.
	 @Override
	protected void registerRecipe(CrucibleRecipeResourceLocation recipeID) {
		var got = CRUCIBLE_RECIPES.get(recipeID);
		if (got != null) {
			throw new RuntimeException("duplicate recipe ID: " + recipeID + " for " + got + " and " + this);
		}
		CRUCIBLE_RECIPES.put(recipeID, this);
		crucibleRecipes.add(this);
		fastCrucibleRecipeMatcher.registerRecipe(this);
	}

	@Nullable
	public static CrucibleRecipe findRecipeCanUse(Player player, ItemStack stack, AspectList<Aspect> aspectListCanUse){
		 return fastCrucibleRecipeMatcher.findRecipeCanUse(player, stack, aspectListCanUse);
	}

	public abstract ItemStack getRecipeOutput(ItemStack inStack);
}
