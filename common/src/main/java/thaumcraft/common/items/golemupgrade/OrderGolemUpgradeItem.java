package thaumcraft.common.items.golemupgrade;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;

import java.util.List;

//GolemUpgrade:4
public class OrderGolemUpgradeItem extends Item implements TravelingTrunkEntity.ITravelingTrunkUpgradeItem {
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
        //TODO:Order upgrade keeps inv
        return TravelingTrunkEntity.ITravelingTrunkUpgradeItem.super.travelingTrunkUpgrade$getTravelingTrunkStack(trunk, upgradeStack,travelingTrunkStack);
    }
}
