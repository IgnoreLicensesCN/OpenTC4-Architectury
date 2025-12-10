package thaumcraft.api.wands;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface WandRodOwner {
    default WandRodPropertiesOwner getWandRodItem(ItemStack stack){
        return getWandRodAsItem(stack) instanceof WandRodPropertiesOwner owner ? owner : null;
    };
    Item getWandRodAsItem(ItemStack stack);
}
