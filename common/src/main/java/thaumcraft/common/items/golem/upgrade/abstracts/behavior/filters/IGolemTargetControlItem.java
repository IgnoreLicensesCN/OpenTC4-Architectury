package thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

//provide for targetGoal
public interface IGolemTargetControlItem extends BaseGolemEntity.IGolemUpgradeItem {
    boolean canTarget(BaseGolemEntity golem, ItemStack upgrade, LivingEntity maybeTarget);
}
