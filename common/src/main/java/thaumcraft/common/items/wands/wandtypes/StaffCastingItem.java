package thaumcraft.common.items.wands.wandtypes;

import net.minecraft.network.chat.Component;

public class StaffCastingItem extends WandCastingItem{
    public StaffCastingItem() {
        super();
    }

    @Override
    public boolean canInsertIntoArcaneCraftingTable() {
        return false;
    }

    @Override
    public Component getComponentName() {
        return Component.translatable("item.Wand.staff.obj");
    }
}
