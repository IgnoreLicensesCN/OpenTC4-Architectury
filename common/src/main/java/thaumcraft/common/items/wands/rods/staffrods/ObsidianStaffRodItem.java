package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem;

import java.util.Collections;
import java.util.Map;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectMapWithValue;

public class ObsidianStaffRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwner {
    public ObsidianStaffRodItem() {
        super(new Properties(), Map.of(Aspects.EARTH,17));
    }

    private final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(getPrimalAspectMapWithValue(175));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }

    private final Map<Aspect, Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(14));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCraftingCostAspect() {
        return cost;
    }
}
