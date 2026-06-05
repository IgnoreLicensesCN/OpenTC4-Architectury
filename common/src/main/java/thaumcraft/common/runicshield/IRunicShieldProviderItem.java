package thaumcraft.common.runicshield;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.item.ItemStack;

/**
 * 
 * @author Azanor
 * 
 * Armor or bauble slot items that implement this interface can provide runic shielding.
 * Recharging, hardening, etc. is handled internally by thaumcraft. 
 *
 */
//the only way(in interface)to add runicShield capacity
public interface IRunicShieldProviderItem {
	
	/**
	 * returns how much charge this item can provide. This is the base shielding value - any hardening is stored and calculated internally. 
	 */
    Object2IntMap<RunicShieldType> getRunicCharge(ItemStack itemstack);
}
