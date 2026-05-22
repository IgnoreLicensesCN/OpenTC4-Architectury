package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

public class VoidWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwnerComponent<Aspect> {//itemWandCap:7
    public VoidWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return .8f;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(9);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
