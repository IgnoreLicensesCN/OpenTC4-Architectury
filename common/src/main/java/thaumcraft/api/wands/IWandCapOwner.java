package thaumcraft.api.wands;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IWandCapOwner {
    default IWandCapPropertiesOwner getWandCapItem(ItemStack stack){
        return getWandCapAsItem(stack) instanceof IWandCapPropertiesOwner owner ? owner : null;
    };
    Item getWandCapAsItem(ItemStack stack);
}
