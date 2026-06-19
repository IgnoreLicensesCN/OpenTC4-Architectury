package thaumcraft.common.items.abstracts.wandabstraction.component;

import net.minecraft.world.item.ItemStack;

//different from IArcaneCraftingVisMultiplierProviderComponent
public interface IArcaneCraftingVisDiscountOwnerItem {
    default float getVisDiscount(ItemStack stack){
        return .1f;
    };
}
