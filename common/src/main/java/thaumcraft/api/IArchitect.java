package thaumcraft.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public interface IArchitect {

	/**
	 * Returns a list ofAspectVisList blocks that should be highlighted in world.
	 */
//    List<BlockPos> getArchitectBlocks(ItemStack stack, Level world,
//									  int x, int y, int z, Direction side, Player player);
	List<BlockPos> getArchitectBlocks(ItemStack stack, Level world,
									  BlockPos pos, Direction side, Player player);
	
	/**
	 * which axis should be displayed. 
	 */
    boolean showAxis(ItemStack stack, Level world, Player player, Direction side, EnumAxis axis);
	
	enum EnumAxis {
		X, // east / west
		Y, // up / down
		Z // north / south
	}
}
