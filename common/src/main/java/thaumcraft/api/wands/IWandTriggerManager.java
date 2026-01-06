package thaumcraft.api.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IWandTriggerManager {

	/**
	 * This class will be called by wands with the proper parameters. It is up to you to decide what to do with them.
	 * true if consume(stop other behind)
	 */
    boolean performTrigger(Level world, ItemStack wand, Player player,
						   BlockPos pos, Direction side);
	
}
