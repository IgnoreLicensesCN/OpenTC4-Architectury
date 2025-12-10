package thaumcraft.api.rendering;

import net.minecraft.world.item.ItemStack;

//TODO:Leather Armor color and other vanilla things and tc4 dye-able
public interface ItemColorOwner {
    int getColorFromItemStack(ItemStack stack);
}
