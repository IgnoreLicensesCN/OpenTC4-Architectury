package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemRangedAttackPerformerItem extends IGolemModifierItem {
    void performRangedAttackForGolem( /*seldom but could be not golem?*/ LivingEntity shooter, ItemStack upgradeStack, LivingEntity target, float f);
}
