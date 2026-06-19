package thaumcraft.common.items.abstracts.wandabstraction.wand;

import com.linearity.opentc4.annotations.forvalue.PercentageFloatValue;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;

public interface IVisCostModifierOwnerItem {
    @PercentageFloatValue
    float getCostDiscountForAspect(ItemStack wandStack, Aspect aspect);
}
