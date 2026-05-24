package thaumcraft.api.wands;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

import java.util.Map;
public interface IVisCostModifierOwnerComponent {
    float getBaseCostModifier();
    //add cost if negative and decrease post if positive
    @NotNull Object2FloatMap<Aspect> getSpecialCostModifierAspects();
}
