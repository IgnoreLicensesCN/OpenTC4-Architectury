package thaumcraft.common.items.golem.golems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.items.placeableentity.AbstractUpgradeApplicableGolemPlacerItem;

import static com.linearity.opentc4.Consts.BaseGolemEntityTagAccessors.*;

public abstract class BaseGolemPlacerItem<GolemClass extends BaseGolemEntity> extends AbstractUpgradeApplicableGolemPlacerItem<GolemClass> {
    public BaseGolemPlacerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void applyEntityTags(GolemClass golemPlaced, UseOnContext useOnContext) {
        var usingStack = useOnContext.getItemInHand();
        var stackTag = usingStack.getTag();
        if (stackTag != null){
            int inventorySize = INVENTORY_SIZE.readIntFromCompoundTag(stackTag);
            golemPlaced.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
            {
                byte[] inventoryColors = new byte[inventorySize];
                var inventoryColorsBefore = INVENTORY_COLORS.readFromCompoundTag(stackTag);
                System.arraycopy(inventoryColorsBefore, 0, inventoryColors, 0, Math.min(inventoryColorsBefore.length, inventoryColors.length));
                golemPlaced.setInventoryColors(inventoryColors);
            }
            golemPlaced.restrictTo(
                    useOnContext.getClickedPos(),
                    golemPlaced.getDefaultRestrictionSize()
            );
            golemPlaced.homeLevelResourceLocation = useOnContext.getLevel().dimension().location();
            MARKERS.readFromCompoundTagInto(stackTag,golemPlaced.markers);
        }
        super.applyEntityTags(golemPlaced, useOnContext);
    }
}
