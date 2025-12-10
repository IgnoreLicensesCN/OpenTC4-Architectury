package thaumcraft.api.rendering;

import net.minecraft.world.item.ItemStack;

public interface ItemColorOwner {
    int getColorFromItemStack(ItemStack stack);
}
