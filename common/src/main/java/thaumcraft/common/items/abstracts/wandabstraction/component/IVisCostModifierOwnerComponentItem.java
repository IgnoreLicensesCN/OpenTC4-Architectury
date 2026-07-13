package thaumcraft.common.items.abstracts.wandabstraction.component;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

public interface IVisCostModifierOwnerComponentItem {
    //cost will be decreased by this
    float getBaseCostModifier();
    //add cost if negative and decrease post if positive
    @NotNull Object2FloatMap<Aspect> getSpecialCostModifierAspects();
}
