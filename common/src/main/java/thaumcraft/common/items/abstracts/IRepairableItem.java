package thaumcraft.common.items.abstracts;

import com.linearity.opentc4.annotations.RecommendedLogicalSide;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.*;
import thaumcraft.api.aspects.aspect.IAspectReducibleToPrimal;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.baseimpl.centivis.LinkedHashCentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.api.listeners.wandconsumption.ThaumcraftWandConsumptionTypes;
import thaumcraft.common.items.wands.WandManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static thaumcraft.common.lib.events.EventHandlerEntity.checkIfCanConsumeForRepair;


/**
 * <p>IRepairable(pre) -> IRepairEnchantable(after)</p>
 * <p>IRepairableExtended(pre) -> IRepairableItem(after)</p>
 * <p>no extend now</p>
 * @author Azanor
 * ThaumcraftItems, armor and tools with this interface can receive the Repair enchantment.
 * Repairs 1 point of durability every 10 seconds (2 for repair II)
 */
public interface IRepairableItem {

	//true if repaired
	default boolean doRepair(ItemStack is, @Nullable LivingEntity living, int enchantlevel){
		int level = enchantlevel + 1;
		if (level > 0) {
			var cost = getRepairCost(is,enchantlevel);
			if (!cost.isEmpty()) {
				boolean doRepair = WandManager.consumeCentiVisFromInventory(
						living, (CentiVisList<Aspect>)(Object)cost, checkIfCanConsumeForRepair, ThaumcraftWandConsumptionTypes.CONSUMPTION_REPAIRING_ITEM
				);
                if (doRepair) {
					if (living != null) {
						is.hurtAndBreak(-level, living,(p) -> {});
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
	@RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
	@UnmodifiableView
	@NotNull
	default CentiVisList<PrimalAspect> getRepairCost(ItemStack is,int repairLevel){
		return getRepairCostDefault(is,repairLevel);
	}
	@RecommendedLogicalSide(RecommendedLogicalSide.LogicalSide.SERVER)
	@UnmodifiableView
	@NotNull
	static CentiVisList<PrimalAspect> getRepairCostDefault(ItemStack is,int repairLevel){
		return repairCostCache.computeIfAbsent(is.getItem(),item -> {
			var basic = ItemBasicAspectGetter.getBasicAspectsServer(is.getItem());
			if (basic.isEmpty()) {
				return UnmodifiableCentiVisList.EMPTY_PRIMAL;
			}
			var reduced = IAspectReducibleToPrimal.reduceToPrimals(basic);
			CentiVisList<PrimalAspect> cost = new LinkedHashCentiVisList<>();
			reduced.forEach(
					(aspect, amount) -> cost.addAll(
							aspect,
							Math.max(repairCentiVisAmountForPrimalAspectDefault(is,aspect,amount,repairLevel),amount)
					)
			);

			return UnmodifiableCentiVisList.of(cost);
		});
	}

	static int repairCentiVisAmountForPrimalAspectDefault(ItemStack is, PrimalAspect aspect,int amount,int repairLevel) {
		return (int)Math.sqrt(amount * 2) * repairLevel;
	}

	static void  onDatapackReload(){
		repairCostCache.clear();
	}

}
