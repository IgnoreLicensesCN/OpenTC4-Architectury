package thaumcraft.api.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Azanor
 * 
 * Blocks that implement this interface act as infusion crafting stabilisers like candles and skulls 
 *
 */
public interface IInfusionStabiliser {

	boolean canStabaliseInfusion(Level world, BlockPos pos);

}
