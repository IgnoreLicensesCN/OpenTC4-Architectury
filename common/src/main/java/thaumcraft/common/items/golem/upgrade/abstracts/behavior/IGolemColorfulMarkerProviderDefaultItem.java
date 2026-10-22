package thaumcraft.common.items.golem.upgrade.abstracts.behavior;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.entities.golems.BaseGolemEntity;

import static com.linearity.opentc4.Consts.IGolemColorfulMarkerProviderDefaultItemTagAccessors.INVENTORY_COLORS;

public interface IGolemColorfulMarkerProviderDefaultItem extends IGolemColorfulMarkerProviderItem,IGolemAdditionalDataProviderItem {
    @Override
    default void readAdditional(BaseGolemEntity golemPlaced, ItemStack upgradeStack, CompoundTag tag){
        int inventorySize = golemPlaced.inventory.size();
        byte[] inventoryColors = new byte[inventorySize];
        var inventoryColorsBefore = upgradeStack.getTag() == null?new byte[0]:INVENTORY_COLORS.readFromCompoundTag(tag);
        System.arraycopy(inventoryColorsBefore, 0, inventoryColors, 0, Math.min(inventoryColorsBefore.length, inventoryColors.length));
        golemPlaced.setInventoryColors(inventoryColors);
    }

    @Override
    default void writeAdditional(BaseGolemEntity golem, ItemStack upgradeStack, CompoundTag tag){
        INVENTORY_COLORS.writeToCompoundTag(tag, golem.getInventoryColors());
    }
}
