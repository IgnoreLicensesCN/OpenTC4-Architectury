package thaumcraft.common.items.wands.rods.wandrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem;

import java.util.Collections;
import java.util.Map;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectMapWithValue;

public class BoneWandRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsWandRod, ICraftingCostAspectOwner {
    public BoneWandRodItem() {
        super(new Properties(), Map.of(Aspects.ENTROPY,7));
    }

    private final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(getPrimalAspectMapWithValue(75));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }

    private final Map<Aspect, Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(6));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCraftingCostAspect() {
        return cost;
    }
}
