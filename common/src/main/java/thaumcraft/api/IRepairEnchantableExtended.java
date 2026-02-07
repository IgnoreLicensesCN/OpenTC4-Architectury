package thaumcraft.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;



/**
 * @author Azanor
 * ThaumcraftItems, armor and tools with this interface can receive the Repair enchantment.
 * Repairs 1 point of durability every 10 seconds (2 for repair II)
 */
public interface IRepairEnchantableExtended extends IRepairEnchantable {
	
	boolean doRepair(ItemStack stack, Player player, int enchantlevel);

}
