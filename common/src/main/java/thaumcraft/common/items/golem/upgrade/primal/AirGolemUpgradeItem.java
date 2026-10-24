package thaumcraft.common.items.golem.upgrade.primal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.abilityexample.IGolemSpeedMultiplierModifierItem;
import thaumcraft.common.items.golem.upgrade.abstracts.behavior.attack.rangedattack.IGolemShootCooldownModifierItem;

import java.util.List;

//GolemUpgrade:0
public class AirGolemUpgradeItem extends Item implements
        TravelingTrunkEntity.ITravelingTrunkUpgradeItem,
        IGolemShootCooldownModifierItem,
        IGolemSpeedMultiplierModifierItem
{
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

    @Override
    public float modifyShootCooldown(BaseGolemEntity golem, ItemStack stack, float cooldown, float defaultCooldown) {
        return Math.max(0,cooldown- (defaultCooldown * 8F)/30F /*8F/30F*/);
    }

    @Override
    public float modifySpeedMultiplier(BaseGolemEntity golem, ItemStack upgradeStack, float speedMultiplier, float basicSpeedMultiplier) {
        return speedMultiplier + (0.15F*basicSpeedMultiplier);
    }
}
