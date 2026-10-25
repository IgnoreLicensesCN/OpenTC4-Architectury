package thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

//provide for targetGoal
public interface IGolemTargetControlItem extends IGolemModifierItem {
    boolean canTarget(BaseGolemEntity golem, ItemStack upgrade, LivingEntity maybeTarget);
}
