package thaumcraft.common.items.wands.wandcaps;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.INodeHarmfulComponent;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import java.util.Collections;
import java.util.Map;

public class IronWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwner, INodeHarmfulComponent {//itemWandCap:0
    public IronWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 1.1f;
    }

    private final Map<Aspect,Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(1));
    @Override
    public Map<Aspect, Integer> getCraftingCostAspect() {
        return cost;
    }
}
