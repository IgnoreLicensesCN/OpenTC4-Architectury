package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemDoHurtTargetListenerItem extends BaseGolemEntity.IGolemUpgradeItem {
    void onDoHurtTarget(BaseGolemEntity golem, ItemStack upgradeStack, Entity entity);
}
