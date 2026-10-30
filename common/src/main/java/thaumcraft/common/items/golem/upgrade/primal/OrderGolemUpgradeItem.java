package thaumcraft.common.items.golem.upgrade.primal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.IGolemBasicUpgradeItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.filters.IGolemTargetControlItem;

import java.util.List;

import static thaumcraft.common.entities.golems.BaseGolemEntity.*;

//GolemUpgrade:4
public class OrderGolemUpgradeItem extends GolemBasicUpgradeItem implements
        IGolemTargetControlItem,
        IGolemBasicUpgradeItem {
    public OrderGolemUpgradeItem(Properties properties) {
        super(properties);
    }
    public OrderGolemUpgradeItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("golem_upgrade.order.desc"));
    }

    @Override
    public ItemStack travelingTrunkUpgrade$getTravelingTrunkStack(TravelingTrunkEntity trunk, ItemStack upgradeStack,ItemStack travelingTrunkStack) {
        var tag = travelingTrunkStack.getOrCreateTag();
        ContainerHelper.saveAllItems(tag,trunk.inventory);
        trunk.inventory.clear();
        return travelingTrunkStack;
    }

    @Override
    public boolean canTarget(BaseGolemEntity golem, ItemStack upgrade, LivingEntity maybeTarget) {
        byte flags = golem.getAttackControlFlags();
        if (maybeTarget instanceof Enemy){
            if (maybeTarget instanceof Creeper){
                return (flags & FLAG_CAN_ATTACK_CREEPERS) != 0;
            }
            return (flags & FLAG_CAN_ATTACK_HOSTILES) != 0;
        }
        if (maybeTarget instanceof Animal){
            return (flags & FLAG_CAN_ATTACK_ANIMALS) != 0;
        }
        if (maybeTarget instanceof Player player){
            var owner = golem.getOwner();
            if (owner == null || !player.isAlliedTo(owner)){
                return (flags & FLAG_CAN_ATTACK_PLAYERS) != 0;
            }
        }
        return false;
    }
}
