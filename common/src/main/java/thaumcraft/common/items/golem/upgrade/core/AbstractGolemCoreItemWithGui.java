package thaumcraft.common.items.golem.upgrade.core;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.listeners.IGolemPlayerInteractionListenerItem;

import static dev.architectury.registry.menu.MenuRegistry.openExtendedMenu;

@UtilityLikeAbstraction
public abstract class AbstractGolemCoreItemWithGui extends AbstractGolemCoreItem implements IGolemPlayerInteractionListenerItem {
    public AbstractGolemCoreItemWithGui() {
        super();
    }
    public AbstractGolemCoreItemWithGui(Properties properties) {
        super(properties);
    }

    @Override
    public void listenerOnPlayerInteract(BaseGolemEntity golem, ItemStack upgrade, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            openMenuFor(golem,upgrade,serverPlayer);
        }
    }

    public abstract void openMenuFor(BaseGolemEntity golem,ItemStack upgrade,ServerPlayer player);
}
