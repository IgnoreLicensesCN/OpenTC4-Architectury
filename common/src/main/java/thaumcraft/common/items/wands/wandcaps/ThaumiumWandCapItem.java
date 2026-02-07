package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCasted;

public class ThaumiumWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwner<Aspect> {//itemWandCap:2
    public ThaumiumWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return .9f;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCasted(6);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
