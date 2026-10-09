package thaumcraft.common.items.golem.upgrade.primal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;

import java.util.List;

//GolemUpgrade:0
public class AirGolemUpgradeItem extends Item implements TravelingTrunkEntity.ITravelingTrunkUpgradeItem {
    public AirGolemUpgradeItem(Properties properties) {
        super(properties);
    }
    public AirGolemUpgradeItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("golem_upgrade.air.desc"));
    }

    @Override
    public double travelingTrunkUpgrade$wantedMovementMultiplier(TravelingTrunkEntity trunk, ItemStack upgradeStack) {
        return TravelingTrunkEntity.ITravelingTrunkUpgradeItem.super.travelingTrunkUpgrade$wantedMovementMultiplier(trunk, upgradeStack) * 4 / 3;
    }
}
