package thaumcraft.api.nodes;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 
 * @author Azanor
 * 
 * Equipped head slot items that extend this class will make nodes visible in world.
 *
 */

public interface IRevealer {
	
	/*
	 * If this method returns true the nodes will be visible.
	 */
    boolean showNodes(ItemStack itemstack, LivingEntity player);
	

}
