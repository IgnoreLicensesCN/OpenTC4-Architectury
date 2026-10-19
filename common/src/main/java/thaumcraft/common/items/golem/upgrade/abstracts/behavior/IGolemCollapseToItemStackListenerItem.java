package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemCollapseToItemStackListenerItem extends IGolemModifierItem {
    void onCollapseToItemStackWithoutSneaking(BaseGolemEntity golem, ItemStack upgradeStack);
    void onCollapseToItemStackWithSneaking(BaseGolemEntity golem, ItemStack upgradeStack);
}
