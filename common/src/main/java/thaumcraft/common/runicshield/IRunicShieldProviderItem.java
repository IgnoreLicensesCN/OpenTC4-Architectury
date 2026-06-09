package thaumcraft.common.runicshield;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import java.util.List;

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
	@Unmodifiable
    Object2IntMap<AbstractRunicShieldType<?>> getRunicCharge(ItemStack itemstack);

	default void addShieldToolTip(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag){
		var providingShield = this.getRunicCharge(itemStack);
		providingShield.forEach(
				(shieldType,shieldAmount) -> {
					var style = shieldType.getShieldName().getStyle();
					var sign = "+";
					if (shieldAmount < 0) {
						sign = "-";
					}
					list.add(shieldType.getShieldName().copy()
							.append(Component.literal(" " + sign + shieldAmount).withStyle(style))
					);
				}
		);
	}
}
