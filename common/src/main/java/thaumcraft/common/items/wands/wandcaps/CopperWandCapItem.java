package thaumcraft.common.items.wands.wandcaps;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import java.util.Map;

public class CopperWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwnerComponent<Aspect> {//itemWandCap:3
    public CopperWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 1.1f;
    }

    private final Map<Aspect,Float> specialCostModifierAspects = Map.of(Aspects.ORDER,1.f, Aspects.ENTROPY,1.f);
    @Override
    public @NotNull Map<Aspect,Float> getSpecialCostModifierAspects() {
        return specialCostModifierAspects;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(2);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
