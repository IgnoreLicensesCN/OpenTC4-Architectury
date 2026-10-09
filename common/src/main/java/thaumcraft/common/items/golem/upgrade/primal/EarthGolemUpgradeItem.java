package thaumcraft.common.items.golem.upgrade.primal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;

import java.util.List;

//GolemUpgrade:1
public class EarthGolemUpgradeItem extends Item implements TravelingTrunkEntity.ITravelingTrunkUpgradeItem {
    public EarthGolemUpgradeItem(Properties properties) {
        super(properties);
    }
    public EarthGolemUpgradeItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("golem_upgrade.earth.desc"));
    }

    @Override
    public int travelingTrunkUpgrade$inventorySizeAddition(TravelingTrunkEntity trunk, ItemStack upgradeStack) {
        return 9;
    }
}
