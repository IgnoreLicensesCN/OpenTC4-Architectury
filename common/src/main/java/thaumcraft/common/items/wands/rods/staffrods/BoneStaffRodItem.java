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

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectMapWithValue;

public class BoneStaffRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwner {
    public BoneStaffRodItem() {
        super(new Properties(), Map.of(Aspects.ENTROPY,17 * CENTIVIS_MULTIPLIER));
    }

    private final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(getPrimalAspectMapWithValue(175 * CENTIVIS_MULTIPLIER));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCentiVisCapacity() {
        return capacity;
    }
    private final Map<Aspect, Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(14 * CENTIVIS_MULTIPLIER));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCraftingCostCentiVis() {
        return cost;
    }
}
