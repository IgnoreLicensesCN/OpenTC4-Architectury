package thaumcraft.api.wands;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IWandCapOwner {
    default IWandCapPropertiesOwner getWandCapItem(ItemStack stack){
        return getWandCapAsItemStack(stack).getItem() instanceof IWandCapPropertiesOwner owner ? owner : null;
    };
    @NotNull("null -> empty")
    ItemStack getWandCapAsItemStack(ItemStack stack);
}
