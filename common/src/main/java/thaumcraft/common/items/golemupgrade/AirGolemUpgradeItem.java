package thaumcraft.common.items.golemupgrade;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//GolemUpgrade:0
public class AirGolemUpgradeItem extends Item {
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
}
