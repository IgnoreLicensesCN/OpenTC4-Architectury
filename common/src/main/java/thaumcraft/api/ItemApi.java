package thaumcraft.api;

import com.linearity.opentc4.OpenTC4;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * @author Azanor	
 * 
 * This is used to gain access to the items in my mod. 
 * I only give some examples and it will probably still 
 * require a bit ofAspectVisList work for you to get hold ofAspectVisList everything you need.
 *
 * IgnoreLicensesCN:
 * since we're open,it's suitable to remove it
 *
 */
@Deprecated(forRemoval = true)
public class ItemApi {

	@Deprecated(forRemoval = true)
	public static ItemStack getItem(String itemString, int meta) {
		ItemStack item = null;


		try {
			String itemClass = "thaumcraft.common.config.ConfigItems";
			Object obj = Class.forName(itemClass).getField(itemString).get(null);
			if (obj instanceof Item) {
				item = new ItemStack((Item) obj,1,meta);
			} else if (obj instanceof ItemStack) {
				item = (ItemStack) obj;
			}
		} catch (Exception ex) {
			OpenTC4.LOGGER.warn("[Thaumcraft] Could not retrieve item identified by: " + itemString);
		}

		return item;
	}

	@Deprecated(forRemoval = true)
	public static ItemStack getBlock(String itemString, int meta) {
		ItemStack item = null;

		try {
			String itemClass = "thaumcraft.common.config.ConfigBlocks";
			Object obj = Class.forName(itemClass).getField(itemString).get(null);
			if (obj instanceof Block) {
				item = new ItemStack((Block) obj,1,meta);
			} else if (obj instanceof ItemStack) {
				item = (ItemStack) obj;
			}
		} catch (Exception ex) {
			OpenTC4.LOGGER.warning("[Thaumcraft] Could not retrieve block identified by: " + itemString);
		}

		return item;
	}

	/** 
	 * 
	 * Some examples
	 * 
	 * Casting Wands:
	 * WandCastingItem
	 *  
	 * Resources:
	 * itemEssence, itemWispEssence, itemResource, itemShard, itemNugget, 
	 * itemNuggetChicken, itemNuggetBeef, itemNuggetPork, itemTripleMeatTreat
	 * 
	 * Research:
	 * itemResearchNotes, itemInkwell, itemThaumonomicon
	 * 
	 */

}
