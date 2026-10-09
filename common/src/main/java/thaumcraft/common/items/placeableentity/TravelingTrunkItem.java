package thaumcraft.common.items.placeableentity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.entities.ThaumcraftEntities;
import thaumcraft.common.entities.golems.TravelingTrunkEntity;

import java.util.List;

public class TravelingTrunkItem extends AbstractUpgradeApplicableGolemPlacerItem<TravelingTrunkEntity> {
    public TravelingTrunkItem(Properties properties) {
        super(properties);
    }

    public TravelingTrunkItem() {
        this(new Properties().stacksTo(1));
    }

    @Override
    public @NotNull EntityType<TravelingTrunkEntity> getEntityTypeToPlace() {
        return ThaumcraftEntities.ThaumcraftEntityTypeInstances.TRAVELING_TRUNK();
    }

    @Override
    public void applyEntityTags(TravelingTrunkEntity entityPlaced, UseOnContext useOnContext) {
        super.applyEntityTags(entityPlaced, useOnContext);

        var usingStack = useOnContext.getItemInHand();
        var tag = usingStack.getTag();
        if (tag != null) {
            entityPlaced.loadInventoryFromNBT(tag);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        var tag = itemStack.getTag();
        if (tag != null) {
            if (tag.contains("Items")) {
                list.add(Component.translatable("item.TrunkSpawner.text.1"));
            }
        }
    }
}
