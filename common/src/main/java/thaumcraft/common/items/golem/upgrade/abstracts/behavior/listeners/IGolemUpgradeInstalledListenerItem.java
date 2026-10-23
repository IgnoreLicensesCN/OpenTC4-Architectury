package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

//can only listen to self
public interface IGolemUpgradeInstalledListenerItem{
    void onUpgradeInstalled(BaseGolemEntity golem, ItemStack upgrade);
}
