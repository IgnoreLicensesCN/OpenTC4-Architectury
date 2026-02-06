package thaumcraft.api.research.scan;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IScanEventHandler {
	ScanResult scanPhenomena(ItemStack stack, Level world, Player player);
}
