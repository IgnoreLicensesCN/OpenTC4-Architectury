package thaumcraft.api.wands;

import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IWandTriggerManager {

	/**
	 * This class will be called by wands with the proper parameters. It is up to you to decide what to do with them.
	 */
    boolean performTrigger(World world, ItemStack wand, Player player,
                           int x, int y, int z, int side, int event);
	
}
