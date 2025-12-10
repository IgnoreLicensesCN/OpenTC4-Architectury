package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.CraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import java.util.Collections;
import java.util.Map;

public class VoidWandCapItem extends ThaumcraftWandCapItem implements CraftingCostAspectOwner {//itemWandCap:7
    public VoidWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return .8f;
    }

    private final Map<Aspect,Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(9));
    @Override
    public Map<Aspect, Integer> getCraftingCostAspect() {
        return cost;
    }
}
