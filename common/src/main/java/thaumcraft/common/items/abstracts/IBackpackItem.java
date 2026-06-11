package thaumcraft.common.items.abstracts;

import net.minecraft.world.item.ItemStack;

public interface IBackpackItem {
    ItemStack getStack(int index,ItemStack backpackStack);
    int getSize(ItemStack backpackStack);
    void setStack(ItemStack stack,int index,ItemStack backpackStack);
}
