package thaumcraft.api.wands;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 *  
 * @author azanor
 * 
 * Add this to a tile entity that you wish wands to interact with in some way. 
 *
 */
@Deprecated(forRemoval = true,since = "see WandInteractableBlock and im migrating api")
public interface IWandable {

	int onWandRightClick(Level world, ItemStack wandstack, Player player, int x, int y, int z, int side, int md);
	
	ItemStack onWandRightClick(Level world, ItemStack wandstack, Player player);
	
	void onUsingWandTick(ItemStack wandstack, Player player, int count);
	
	void onWandStoppedUsing(ItemStack wandstack, Level world, Player player, int count);
	
}
