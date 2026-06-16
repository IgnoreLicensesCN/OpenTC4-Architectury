package thaumcraft.common.runicshield;

import com.google.common.collect.MapMaker;
import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntRBTreeMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
	Supplier<Object2IntMap<AbstractRunicShieldType<?>>> SORTED_SHIELD_MAP_PROVIDER = () -> new Object2IntRBTreeMap<>(AbstractRunicShieldType::compareTo);
	/**
	 * returns how much charge this item can provide. This is the base shielding value
	 */
	@Unmodifiable
    CalcCacheableObject2IntMap<AbstractRunicShieldType<?>> getRunicCharge(ItemStack itemstack);

	default void addShieldToolTip(ItemStack itemStack, Level level, List<Component> list, TooltipFlag tooltipFlag){
		var providingShield = this.getRunicCharge(itemStack);
		providingShield.wrapped.forEach(
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
