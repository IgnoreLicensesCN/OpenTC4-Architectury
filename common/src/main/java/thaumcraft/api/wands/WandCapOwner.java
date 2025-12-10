package thaumcraft.api.wands;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface WandCapOwner {
    default WandCapPropertiesOwner getWandCapItem(ItemStack stack){
        return getWandCapAsItem(stack) instanceof WandCapPropertiesOwner owner ? owner : null;
    };
    Item getWandCapAsItem(ItemStack stack);
}
