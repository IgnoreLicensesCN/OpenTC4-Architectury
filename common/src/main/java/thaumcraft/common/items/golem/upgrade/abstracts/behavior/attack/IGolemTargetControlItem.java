package thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack;

import net.minecraft.world.entity.LivingEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemTargetControlItem extends IGolemModifierItem {
    boolean canTarget(LivingEntity livingEntity);
}
