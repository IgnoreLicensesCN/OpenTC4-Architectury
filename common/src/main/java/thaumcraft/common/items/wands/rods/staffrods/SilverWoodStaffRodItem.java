package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.CraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import java.util.Collections;
import java.util.Map;

public class SilverWoodStaffRodItem extends ThaumcraftWandRodItem implements WorkAsStaffRod, CraftingCostAspectOwner {
    public SilverWoodStaffRodItem() {
        super(new Properties());
    }


    public static final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(250));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }

    @Override
    public Map<Aspect, Integer> getCraftingCostAspect() {
        return WandUtils.getPrimalAspectMapWithValue(24);
    }
}
