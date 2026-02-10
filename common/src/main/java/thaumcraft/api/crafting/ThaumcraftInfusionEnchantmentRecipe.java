package thaumcraft.api.crafting;

import com.linearity.opentc4.recipeclean.itemmatch.EnchantmentItemMatcher;
import com.linearity.opentc4.recipeclean.itemmatch.RecipeItemMatcher;
import com.linearity.opentc4.recipeclean.recipewrapper.IAspectCalculableRecipe;
import com.linearity.opentc4.recipeclean.recipewrapper.RecipeInAndOutSampler;
import com.linearity.opentc4.utils.vanilla1710.Vanilla1710Utils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.crafting.interfaces.IInfusionAspectsModifiable;
import thaumcraft.api.research.ResearchItem;

import java.util.*;

import static com.linearity.opentc4.OpenTC4.platformUtils;
import static com.linearity.opentc4.utils.IndexPicker.indexByTime;
import static com.linearity.opentc4.utils.IndexPicker.pickByTime;

//TODO:no more rely on this special class case if anyone want to create infusion enchantment recipe(even supports another class copied this but different source code)
public class ThaumcraftInfusionEnchantmentRecipe extends InfusionRecipe
		implements RecipeInAndOutSampler,
		IAspectCalculableRecipe,
		IInfusionAspectsModifiable
{
	
	public final AspectList<Aspect> basicCostAspects;
	public final RecipeItemMatcher[] components;
	public final Enchantment enchantment;
	public final int recipeXP;
	public final int instability;
	private final ItemStack[] inputSampleArr;
	
	public ThaumcraftInfusionEnchantmentRecipe(
			ResearchItem research,
			Enchantment toApply,
			AspectList<Aspect> basicCostAspects,
			int inst,
			RecipeItemMatcher[] recipe
	) {
        super(
				research, itemStacks -> {
                    var inCenter = itemStacks[itemStacks.length - 1];
                    if (inCenter == null || inCenter.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    inCenter = inCenter.copy();
                    if (!toApply.canEnchant(inCenter)) {
                        return ItemStack.EMPTY;
                    }
                    var currentLevel = EnchantmentHelper.getItemEnchantmentLevel(toApply, inCenter);
                    if (toApply.getMaxLevel() <= currentLevel) {
                        return ItemStack.EMPTY;
                    }
                    EnchantmentHelper.setEnchantmentLevel(inCenter.getOrCreateTag(), currentLevel + 1);
                    return inCenter;

                },
				inst,
				basicCostAspects,
				EnchantmentItemMatcher.of(toApply),
				recipe,
				EnchantmentItemMatcher.of(toApply)
		);
		this.enchantment = toApply;
		this.components = recipe;
		this.inputSampleArr = new ItemStack[components.length + 1];
		this.inputSampleArr[components.length] = new ItemStack(Items.ENCHANTED_BOOK);
		this.basicCostAspects = basicCostAspects;
		this.instability = inst;
		this.recipeXP = Math.max(1, toApply.getMinCost(1)/3);
	}

	/**
     * Used to check if a recipe matches current crafting inventory
     * @param player 
     */
	public boolean matches(List<ItemStack> input, ItemStack central, Level world, Player player) {
		if (research.isPlayerCompletedResearch(player)) {
    		return false;
    	}
		
		if (!enchantment.canEnchant(central) || !Vanilla1710Utils.isItemTool(central)) {
			return false;
		}//Vanilla1710Utils.isItemTool已经实现了
				
		Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(central);//以前是<Integer,Integer>,key是附魔id,value是等级，现在是<Enchantment,Integer>
        for (Map.Entry<Enchantment,Integer> enchantmentLevelEntry: map1.entrySet()) {
            int lvl = enchantmentLevelEntry.getValue();
            Enchantment ench = enchantmentLevelEntry.getKey();
			boolean enchantmentEqualFlag = Objects.equals(ench,enchantment);
            if (enchantmentEqualFlag &&
					lvl >= ench.getMaxLevel()) {
				return false;
			}
            if (!enchantmentEqualFlag &&
                    (!enchantment.isCompatibleWith(ench) ||
                            !ench.isCompatibleWith(enchantment))) {
                return false;
            }
        }
		
		ItemStack i2;
		
		ArrayList<ItemStack> inputCopy = new ArrayList<>();
		for (ItemStack is:input) {
			inputCopy.add(is.copy());
		}
		
		for (RecipeItemMatcher comp:components) {
			boolean b=false;
			for (int a=0;a<inputCopy.size();a++) {
				 i2 = inputCopy.get(a).copy();
//				if (comp.getMaxDamage() == 0) {
//					i2.setDamageValue(0);
//				}
				if (comp.matches(i2)) {
					inputCopy.remove(a);
					b=true;
					break;
				}
			}
			if (!b) return false;
		}
//		System.out.println(inputCopy.size());
		return inputCopy.isEmpty();
    }
	
//	protected boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1)
//    {
//		if (stack0==null && stack1!=null) return false;
//		if (stack0!=null && stack1==null) return false;
//		if (stack0==null && stack1==null) return true;
//		boolean t1=ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(stack0, stack1);
//		if (!t1) return false;
////		if (fuzzy) {
////			throw new RuntimeException("Fuzzy matches for " + stack0 + " and " + stack1 + " use tag instead!");
////			if (od!=-1) {
////				ItemStack[] ores = OreDictionary.getOres(od).toArray(new ItemStack[]{});
////				if (ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{stack1}, ores))
////					return true;
////			}
////		}
//        return stack0.getItem() == stack1.getItem() && (stack0.getDamageValue() == stack1.getDamageValue() && stack0.getCount() <= stack0.getMaxStackSize());
//    }

	protected boolean isItemStackMatchTag(ItemStack stack, String tag) {
		return platformUtils.isItemStackMatchTag(stack, tag);
	}

	protected boolean isItemStackMatchTags(ItemStack stack, String[] tags) {
		for (String tag:tags) {
			if(platformUtils.isItemStackMatchTag(stack, tag)){
				return true;
			};
		}
		return false;
	}
	
   
    public Enchantment getEnchantment() {
		return enchantment;
    	
    }
    
    public AspectList<Aspect> getAspects() {
		return basicCostAspects;
    }

	public int calcInstability(ItemStack recipeInput) {
		int i = 0;
		Map<Enchantment,Integer> map1 = EnchantmentHelper.getEnchantments(recipeInput);
        for (Map.Entry<Enchantment,Integer> entry: map1.entrySet()) {
			Enchantment ench = entry.getKey();
			int lvl = entry.getValue();
            i += lvl;
        }
		return (i/2) + instability;
	}

	public int calcXP(ItemStack recipeInput) {
		return recipeXP * (1 + EnchantmentHelper.getEnchantments(recipeInput).get(enchantment));
	}

	public AspectList<Aspect> getAspectsModified(ItemStack recipeInput,AspectList<Aspect> basicCostAspects) {
		float mod = EnchantmentHelper.getEnchantments(recipeInput).get(enchantment);
		Map<Enchantment,Integer> map1 = EnchantmentHelper.getEnchantments(recipeInput);
		for (Map.Entry<Enchantment,Integer> entry: map1.entrySet()) {
			Enchantment ench = entry.getKey();
			int lvl = entry.getValue();
            if (ench != enchantment)
                mod += lvl * .1f;
        }
		LinkedHashMap<Aspect,Integer> aspsResult = new LinkedHashMap<>(basicCostAspects.aspectView);
		float finalMod = mod;
		aspsResult.forEach((asp, amount)-> aspsResult.put(asp, (int) (amount * finalMod)));
		return new UnmodifiableAspectList<>(aspsResult);
	}

	@Override
	public ItemStack[] getInputSample() {
		//setting central a book
		int lvlBefore = indexByTime(enchantment.getMaxLevel() - 1);
		if (lvlBefore == 0) {
			enchantmentMapBefore.clear();
		}else {
			enchantmentMapBefore.put(enchantment, lvlBefore);
		}
		EnchantmentHelper.setEnchantments(
				enchantmentMapBefore
				,inputSampleArr[inputSampleArr.length-1]
		);


		for (int i=0;i<inputSampleArr.length;i++) {
			inputSampleArr[i] = pickByTime(components[i].getAvailableItemStackSample());
		}
		return inputSampleArr;
	}

	private final ItemStack[] enchantmentSampleBook = new ItemStack[]{new ItemStack(Items.ENCHANTED_BOOK)};
	private final Map<Enchantment,Integer> enchantmentMap = new HashMap<>();
	private final Map<Enchantment,Integer> enchantmentMapBefore = new HashMap<>();
	@Override
	public ItemStack[] getOutputSample(ItemStack[] inputSample) {
		int lvlBefore = indexByTime(enchantment.getMaxLevel() - 1);

		enchantmentMap.put(enchantment,lvlBefore + 1);
		EnchantmentHelper.setEnchantments(
				enchantmentMap
				,enchantmentSampleBook[0]
		);
		return enchantmentSampleBook;
	}

	@Override
	public boolean supportsAspectCalculation() {
		return false;
	}

	@Override
	public @NotNull List<List<ItemStack>> getAspectCalculationInputs() {
		throw new RuntimeException("check supportsAspectCalculation() first!");
	}

	@Override
	public @NotNull ItemStack getAspectCalculationOutput() {
		throw new RuntimeException("check supportsAspectCalculation() first!");
	}

	@Override
	public @NotNull List<List<ItemStack>> getAspectCalculationRemaining() {
		throw new RuntimeException("check supportsAspectCalculation() first!");
	}

	@Override
	public @NotNull AspectList<Aspect> getAspectCalculationAspectsList() {
		throw new RuntimeException("check supportsAspectCalculation() first!");
	}

	@Override
	public @NotNull CentiVisList<Aspect> getAspectCalculationCentiVisList() {
		throw new RuntimeException("check supportsAspectCalculation() first!");
	}
}
