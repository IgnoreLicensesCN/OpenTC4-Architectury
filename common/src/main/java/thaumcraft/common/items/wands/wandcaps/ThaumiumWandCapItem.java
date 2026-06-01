package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

public class ThaumiumWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwnerComponent<Aspect> {//itemWandCap:2
    public ThaumiumWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return .9f;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(6);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
