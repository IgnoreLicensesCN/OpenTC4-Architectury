package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.CraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import java.util.Collections;
import java.util.Map;

public class GreatWoodStaffRodItem extends ThaumcraftWandRodItem implements WorkAsStaffRod, CraftingCostAspectOwner {
    public GreatWoodStaffRodItem() {
        super(new Properties());
    }


    public static final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(125));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }

    private final Map<Aspect, Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(8));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCraftingCostAspect() {
        return cost;
    }
}
