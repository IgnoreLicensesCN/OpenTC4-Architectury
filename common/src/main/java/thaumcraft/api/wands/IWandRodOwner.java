package thaumcraft.api.wands;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IWandRodOwner {
    default IWandRodPropertiesOwner getWandRodItem(ItemStack stack){
        return getWandRodAsItem(stack) instanceof IWandRodPropertiesOwner owner ? owner : null;
    };
    Item getWandRodAsItem(ItemStack stack);
}
