package thaumcraft.api.crafting;

import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import tc4tweak.ConfigurationHandler;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;

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
public class InfusionRecipe
{
	protected AspectList aspects;
	protected String research;
	private ItemStack[] components;
	private ItemStack recipeInput;
	protected Object recipeOutput;
	protected int instability;
	
	public InfusionRecipe(String research, Object output, int inst,
			AspectList aspects2, ItemStack input, ItemStack[] recipe) {
		this.research = research;
		this.recipeOutput = output;
		this.recipeInput = input;
		this.aspects = aspects2;
		this.components = recipe;
		this.instability = inst;
	}

	/**
     * Used to check if a recipe matches current crafting inventory
     * @param player 
     */
	public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, Player player) {
		if (getRecipeInput()==null) return false;
			
		if (!research.isEmpty() && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research)) {
    		return false;
    	}
		
		ItemStack i2 = central.copy();
		if (getRecipeInput().getItemDamage()==OreDictionary.WILDCARD_VALUE) {
			i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
		}
		
		if (!areItemStacksEqual(i2, getRecipeInput(), true)) return false;
		
		ArrayList<ItemStack> ii = new ArrayList<>();
		for (ItemStack is:input) {
			ii.add(is.copy());
		}
		
		for (ItemStack comp:getComponents()) {
			boolean b=false;
			for (int a=0;a<ii.size();a++) {
				 i2 = ii.get(a).copy();
				if (comp.getItemDamage()==OreDictionary.WILDCARD_VALUE) {
					i2.setItemDamage(OreDictionary.WILDCARD_VALUE);
				}
				if (areItemStacksEqual(i2, comp,true)) {
					ii.remove(a);
					b=true;
					break;
				}
			}
			if (!b) return false;
		}
		return ii.isEmpty();
    }
	
	public static boolean areItemStacksEqual(ItemStack playerInput, ItemStack recipeSpec, boolean fuzzy)
    {
		if (playerInput == null) {
			return recipeSpec == null;
		}
		if (recipeSpec == null) return false;
		if (!ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(playerInput, recipeSpec)) return false;
		if (fuzzy) {
			if (ConfigurationHandler.INSTANCE.getInfusionOreDictMode().test(playerInput, recipeSpec)) {
				return true;
			}
		}

		return playerInput.getItem() == recipeSpec.getItem() &&
				(playerInput.getItemDamage() == recipeSpec.getItemDamage() || recipeSpec.getItemDamage() == 32767) &&
				playerInput.stackSize <= playerInput.getMaxStackSize();
//		if (playerInput==null && recipeSpec!=null) return false;
//		if (playerInput!=null && recipeSpec==null) return false;
//		if (playerInput==null && recipeSpec==null) return true;
//
//		//nbt
//		boolean t1=ThaumcraftApiHelper.areItemStackTagsEqualForCrafting(playerInput, recipeSpec);
//		if (!t1) return false;
//
//		if (fuzzy) {
//			int od = OreDictionary.getOreID(playerInput);
//			if (od!=-1) {
//				ItemStack[] ores = OreDictionary.getOres(od).toArray(new ItemStack[]{});
//				if (ThaumcraftApiHelper.containsMatch(false, new ItemStack[]{recipeSpec}, ores))
//					return true;
//			}
//		}
//
//		//damage
//		boolean damage = playerInput.getItemDamage() == recipeSpec.getItemDamage() ||
//				recipeSpec.getItemDamage() == OreDictionary.WILDCARD_VALUE;
//
//        return playerInput.getItem() == recipeSpec.getItem() && (damage && playerInput.stackSize <= playerInput.getMaxStackSize());
    }
	   
    public Object getRecipeOutput() {
		return getRecipeOutput(this.getRecipeInput());
    }
    
    public AspectList getAspects() {
		return getAspects(this.getRecipeInput());
    }

    public int getInstability() {
		return getInstability(this.getRecipeInput());
    }
    
    public String getResearch() {
		return research;
    }
    
	public ItemStack getRecipeInput() {
		return recipeInput;
	}

	public ItemStack[] getComponents() {
		return components;
	}
	
	public Object getRecipeOutput(ItemStack input) {
		return recipeOutput;
    }
    
    public AspectList getAspects(ItemStack input) {
		return aspects;
    }
    
    public int getInstability(ItemStack input) {
		return instability;
    }
}
