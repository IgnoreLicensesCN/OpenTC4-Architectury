package thaumcraft.common.runicshield;

import com.google.common.collect.MapMaker;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import java.util.List;
import java.util.Map;

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
	Map<Object2IntMap<AbstractRunicShieldType<?>>,
			Map<
					Object2IntMap<AbstractRunicShieldType<?>>,
					Object2IntMap<AbstractRunicShieldType<?>>>
	>
	CACHED_SUM = new MapMaker().weakKeys().makeMap();
	//so in many situations if you cache your map you can get better performance.
	//suitable for most cases
	static Object2IntMap<AbstractRunicShieldType<?>>
	calculateShieldSumAndCache(Object2IntMap<AbstractRunicShieldType<?>> a,Object2IntMap<AbstractRunicShieldType<?>> b){
		return CACHED_SUM.computeIfAbsent(a,_ignored -> new MapMaker().weakKeys().makeMap())
				.computeIfAbsent(b,toAdd -> {
					var result = new Object2IntLinkedOpenHashMap<>(toAdd);
					a.forEach((k,v)->{
						result.mergeInt(k,v,Integer::sum);
					});
					return result;
				});
	}
	
	/**
	 * returns how much charge this item can provide. This is the base shielding value
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
