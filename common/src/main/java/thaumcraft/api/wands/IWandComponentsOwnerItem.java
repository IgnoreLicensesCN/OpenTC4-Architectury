package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IWandComponentsOwnerItem {
    List<ItemStack> getWandComponents(ItemStack stack);
}
