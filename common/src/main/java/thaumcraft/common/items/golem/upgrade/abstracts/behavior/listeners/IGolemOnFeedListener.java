package thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public interface IGolemOnFeedListener extends BaseGolemEntity.IGolemUpgradeItem{
    void onGolemFed(BaseGolemEntity golem, ItemStack upgradeStack, ItemStack usingStack, Player player, InteractionHand interactionHand);
}
