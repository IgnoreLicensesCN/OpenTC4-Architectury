package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

public interface IArcaneCraftingVisDiscountOwner {

    default float getVisDiscount(ItemStack stack){
        return .1f;
    };
}
