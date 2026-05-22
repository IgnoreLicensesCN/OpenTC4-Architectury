package thaumcraft.api;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import com.linearity.opentc4.utils.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.common.lib.events.EventHandlerEntity.checkIfCanConsumeForRepair;


/**
 * <p>IRepairable(pre) -> IRepairEnchantable(after)</p>
 * <p>IRepairableExtended(pre) -> IRepairable(after)</p>
 * <p>no extend now</p>
 * @author Azanor
 * ThaumcraftItems, armor and tools with this interface can receive the Repair enchantment.
 * Repairs 1 point fromAspectVisList durability every 10 seconds (2 for repair II)
 */
public interface IRepairable {

	//true if repaired
	default boolean doRepair(ItemStack is, @Nullable Player player, int enchantlevel){
		int level = enchantlevel + 1;
		if (level > 0) {
			var cost = getRepairCost(is,enchantlevel);
			if (!cost.isEmpty()) {
				boolean doRepair = WandManager.consumeCentiVisFromInventory(
                        player, (CentiVisList<Aspect>)(Object)cost, checkIfCanConsumeForRepair
				);
                if (doRepair) {
					if (player != null) {
						is.hurtAndBreak(-level,player,(p) -> {});
					} else {
						is.setDamageValue(is.getDamageValue() - level);
					}
					return true;
				}
			}
		}
        return false;
	};
	Map<Item,CentiVisList<PrimalAspect>> repairCostCache = new ConcurrentHashMap<>();
	@RecommendedLogicalSide(LogicalSide.SERVER)
	@UnmodifiableView
	@NotNull
	default CentiVisList<PrimalAspect> getRepairCost(ItemStack is,int repairLevel){
		return getRepairCostDefault(is,repairLevel);
	}
	@RecommendedLogicalSide(LogicalSide.SERVER)
	@UnmodifiableView
	@NotNull
	static CentiVisList<PrimalAspect> getRepairCostDefault(ItemStack is,int repairLevel){
		return repairCostCache.computeIfAbsent(is.getItem(),item -> {
			var basic = ItemBasicAspectGetter.getBasicAspectsServer(is.getItem());
			if (basic.isEmpty()) {
				return UnmodifiableCentiVisList.EMPTY_PRIMAL;
			}
			var reduced = ResearchManager.reduceToPrimals(basic);
			CentiVisList<PrimalAspect> cost = new CentiVisList<>();
			reduced.forEach(
					(aspect, amount) -> cost.mergeWithHighest(
							aspect,
							repairCentiVisAmountForPrimalAspectDefault(is,aspect,amount,repairLevel)
					)
			);

			return UnmodifiableCentiVisList.viewOf(cost);
		});
	}

	default int repairCentiVisAmountForPrimalAspect(ItemStack is, PrimalAspect aspect,int amount,int repairLevel) {
		return repairCentiVisAmountForPrimalAspectDefault(is,aspect,amount,repairLevel);
	}
	static int repairCentiVisAmountForPrimalAspectDefault(ItemStack is, PrimalAspect aspect,int amount,int repairLevel) {
		return (int)Math.sqrt(amount * 2) * repairLevel;
	}

}
