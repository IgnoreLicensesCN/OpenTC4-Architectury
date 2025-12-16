package thaumcraft.api.wands;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IWandComponentsOwner {
    List<Item> getWandComponents(ItemStack stack);
}
