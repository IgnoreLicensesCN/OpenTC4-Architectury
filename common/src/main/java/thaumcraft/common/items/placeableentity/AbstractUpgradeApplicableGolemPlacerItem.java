package thaumcraft.common.items.placeableentity;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.golems.AbstractGolemUpgradeApplicableEntity;

import java.util.List;

import static com.linearity.opentc4.Consts.AbstractGolemUpgradeApplicableEntityTagAccessors.UPGRADES;

public abstract class AbstractUpgradeApplicableGolemPlacerItem<
            GolemClass extends AbstractGolemUpgradeApplicableEntity<?>
        > extends AbstractEntityPlacerItem<GolemClass>{
    public AbstractUpgradeApplicableGolemPlacerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void applyEntityTags(GolemClass entityPlaced, UseOnContext useOnContext) {
        super.applyEntityTags(entityPlaced, useOnContext);
        var usingStack = useOnContext.getItemInHand();
        var tag = usingStack.getTag();
        if (tag != null) {
            var upgrades = UPGRADES.readFromCompoundTag(tag);
            entityPlaced.setUpgradeStacks(upgrades);
        }
        var player = useOnContext.getPlayer();
        if (player != null) {
            entityPlaced.setOwnerUUID(player.getUUID());
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        var tag = itemStack.getTag();
        if (tag != null) {
            if (UPGRADES.compoundTagHasKey(tag)) {
                var upgrades = UPGRADES.readFromCompoundTag(tag);
                upgrades.forEach(upgradeStack -> list.add(upgradeStack.getHoverName().copy().withStyle(ChatFormatting.BLUE)));
            }
        }
    }
}
