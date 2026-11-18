package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import io.netty.util.internal.UnstableApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

//TODO:[maybe wont finished]dynamic ui adder?
@UnstableApi(/*i may change signature for this to modify ui*/)
public interface IGolemBeingOpenedGUIListenerItem extends BaseGolemEntity.IGolemUpgradeItem {
    void listenerOnPlayerOpenGUI(BaseGolemEntity golem, ItemStack upgrade, Player player);
}
