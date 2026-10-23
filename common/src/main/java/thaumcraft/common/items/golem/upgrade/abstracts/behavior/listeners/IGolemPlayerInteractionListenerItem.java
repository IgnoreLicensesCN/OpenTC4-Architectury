package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemModifierItem;

public interface IGolemPlayerInteractionListenerItem extends IGolemModifierItem {
    void listenerOnPlayerInteract(BaseGolemEntity golem, ItemStack upgrade, Player player, InteractionHand hand);
}
