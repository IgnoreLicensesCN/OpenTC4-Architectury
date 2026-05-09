package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

public interface IWandRodOwnerItem {
    default IWandRodPropertiesOwner getWandRodItem(ItemStack stack){
        return getWandRodAsItemStack(stack).getItem() instanceof IWandRodPropertiesOwner owner ? owner : null;
    };
    ItemStack getWandRodAsItemStack(ItemStack stack);
}
