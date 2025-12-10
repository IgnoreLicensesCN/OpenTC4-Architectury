package thaumcraft.api.research;

import net.minecraft.entity.player.Player;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IScanEventHandler {
	ScanResult scanPhenomena(ItemStack stack, World world, Player player);
}
