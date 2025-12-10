package thaumcraft.api.crafting;

import net.minecraft.world.level.Level;

/**
 * 
 * @author Azanor
 * 
 * Blocks that implement this interface act as infusion crafting stabilisers like candles and skulls 
 *
 */
public interface IInfusionStabiliser {
	
	/**
	 * returns true if the block can stabilise things
	 */
    boolean canStabaliseInfusion(Level world, int x, int y, int z);

}
