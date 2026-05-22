package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;

//different from IArcaneCraftingVisMultiplierProviderComponent
public interface IArcaneCraftingVisDiscountOwnerItem {
    default float getVisDiscount(ItemStack stack){
        return .1f;
    };
}
