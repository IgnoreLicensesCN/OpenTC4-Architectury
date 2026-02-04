package thaumcraft.common.gui.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import thaumcraft.api.wands.IArcaneCraftingWand;

public class ArcaneWorkbenchWandSlot extends Slot {
    public ArcaneWorkbenchWandSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        if (!(itemStack.getItem() instanceof IArcaneCraftingWand craftingWand)) {
            return false;
        }
        return craftingWand.canInsertIntoArcaneCraftingTable(itemStack);
    }
}
