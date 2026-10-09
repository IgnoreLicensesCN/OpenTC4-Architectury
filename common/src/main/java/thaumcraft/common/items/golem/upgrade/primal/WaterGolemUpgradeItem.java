package thaumcraft.common.items.golem.upgrade.primal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;

import java.util.List;

//GolemUpgrade:3
public class WaterGolemUpgradeItem extends Item implements TravelingTrunkEntity.ITravelingTrunkUpgradeItem {
    public WaterGolemUpgradeItem(Properties properties) {
        super(properties);
    }
    public WaterGolemUpgradeItem() {
        this(new Properties().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("golem_upgrade.water.desc"));
    }

    @Override
    public float travelingTrunkUpgrade$modifyHurtDamage(TravelingTrunkEntity trunk, ItemStack upgradeStack, DamageSource source, float amount) {
        return Float.NaN;
    }

    @Override
    public void travelingTrunkUpgrade$tick(TravelingTrunkEntity trunk, ItemStack upgradeStack) {
        if (trunk.tickCount%50==0){
            trunk.heal(1);
        }
    }

    @Override
    public InteractionResult travelingTrunkUpgrade$modifyInteraction(TravelingTrunkEntity trunk, ItemStack upgradeStack, Player player, InteractionHand interactionHand) {
        if (trunk.isOwner(player.getUUID())) {
            return InteractionResult.PASS;
        }
        return TravelingTrunkEntity.ITravelingTrunkUpgradeItem.super.travelingTrunkUpgrade$modifyInteraction(trunk, upgradeStack, player, interactionHand);
    }
}
