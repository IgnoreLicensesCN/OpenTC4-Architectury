package thaumcraft.common.items.abstracts.armorcomponents;

import net.minecraft.world.item.ItemStack;

public interface IArmorDamageDecreaseMultiplierComponentItem {
    float getAdditionalDamageDecreaseMultiplier(ItemStack componentStack);
}
