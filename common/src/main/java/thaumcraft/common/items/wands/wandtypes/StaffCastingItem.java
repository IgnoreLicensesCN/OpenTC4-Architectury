package thaumcraft.common.items.wands.wandtypes;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class StaffCastingItem extends WandCastingItem{
    public StaffCastingItem() {
        super();
    }

    @Override
    public boolean canInsertIntoArcaneCraftingTable(ItemStack wandStack) {
        return false;
    }

    @Override
    public Component getComponentName() {
        return Component.translatable("item.Wand.staff.obj");
    }
}
