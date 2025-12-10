package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

public interface ArcaneCraftingVisDiscountOwner {

    default float getVisDiscount(ItemStack stack){
        return .1f;
    };
}
