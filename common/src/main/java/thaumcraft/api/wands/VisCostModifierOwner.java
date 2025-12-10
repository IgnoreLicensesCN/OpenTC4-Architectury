package thaumcraft.api.wands;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;

import java.util.Map;

public interface VisCostModifierOwner {
    float getBaseCostModifier();
    @NotNull Map<Aspect,Float> getSpecialCostModifierAspects();
}
