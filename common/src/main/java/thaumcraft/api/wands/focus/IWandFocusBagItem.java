package thaumcraft.api.wands.focus;

import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.annotations.ModifiableCopy;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IWandFocusBagItem {
    ItemStack extractStackAtFocusBag(ItemStack bagStack,int slot);
    void insertStackToFocusBag(ItemStack bagStack, @Modifiable /*the count will be modified*/ ItemStack stackToInsert);
    @ModifiableCopy
    List<ItemStack> getStacksWithSlotOrder(ItemStack bagStack);//TODO:Change focus action C2S with packet BaubleSlot and InventorySlot
}
