package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemCollapseToItemStackListenerItem extends BaseGolemEntity.IGolemUpgradeItem {
    void onCollapseToItemStackWithoutSneaking(BaseGolemEntity golem, ItemStack upgradeStack);
    void onCollapseToItemStackWithSneaking(BaseGolemEntity golem, ItemStack upgradeStack);
}
