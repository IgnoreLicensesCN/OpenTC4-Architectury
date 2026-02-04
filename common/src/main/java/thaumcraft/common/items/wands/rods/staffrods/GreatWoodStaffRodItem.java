package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import java.util.Collections;
import java.util.Map;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;

public class GreatWoodStaffRodItem extends ThaumcraftWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwner {
    public GreatWoodStaffRodItem() {
        super(new Properties());
    }


    public static final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(125 * CENTIVIS_MULTIPLIER));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getCentiVisCapacity() {
        return capacity;
    }

    private final Map<Aspect, Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(8 * CENTIVIS_MULTIPLIER));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCraftingCostCentiVis() {
        return cost;
    }
}
