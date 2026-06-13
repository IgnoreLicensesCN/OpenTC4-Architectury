package thaumcraft.common.items.abstracts;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 
 * @author Azanor
 * 
 * Equipped head slot items that extend this class will be able to perform most functions that 
 * goggles of revealing can apart from view nodes which is handled by IRevealer.
 *
 */
//TODO:Make renderer judge with this in a method so that it can be mixin
public interface IGoggles {
	
	/*
	 * If this method returns true things like block essentia contents will be shown.
	 */
    default boolean showAsWearingGogglesOfRevealing(ItemStack itemstack, LivingEntity player){
		return true;
	};

}
