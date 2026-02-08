package thaumcraft.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.research.ResearchManager;

import static thaumcraft.common.lib.events.EventHandlerEntity.checkIfCanConsumeForRepair;


/**
 * <p>IRepairable(pre) -> IRepairEnchantable(after)</p>
 * <p>IRepairableExtended(pre) -> IRepairable(after)</p>
 * <p>no extend now</p>
 * @author Azanor
 * ThaumcraftItems, armor and tools with this interface can receive the Repair enchantment.
 * Repairs 1 point of durability every 10 seconds (2 for repair II)
 */
public interface IRepairable {
	
	default boolean doRepair(ItemStack is, Player player, int enchantlevel){
		int level = enchantlevel + 1;
		if (level > 0) {
			AspectList<Aspect> cost = ThaumcraftCraftingManager.getObjectTags(is);
			if (cost != null && !cost.isEmpty()) {
				cost = ResearchManager.reduceToPrimalsAndCast(cost);
				CentiVisList<Aspect> finalCost = new CentiVisList<>();

				for(Aspect a : cost.getAspectTypes()) {
					if (a != null) {
						finalCost.mergeWithHighest(a, (int)Math.sqrt(cost.getAmount(a) * 2) * level);
					}
				}
				boolean doRepair = WandManager.consumeCentiVisFromInventory(
                        player, finalCost, checkIfCanConsumeForRepair);
                if (doRepair) {
					is.hurtAndBreak(-level,player,(p) -> {});
				}
			}
		}
        return true;
	};

}
