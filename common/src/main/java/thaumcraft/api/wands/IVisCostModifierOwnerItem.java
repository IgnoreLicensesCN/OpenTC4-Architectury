package thaumcraft.api.wands;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

public interface IVisCostModifierOwnerItem {
    float getCostDiscountForAspect(ItemStack wandStack, Aspect aspect);
}
