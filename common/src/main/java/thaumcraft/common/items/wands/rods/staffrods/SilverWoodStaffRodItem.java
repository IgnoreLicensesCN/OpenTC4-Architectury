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

public class SilverWoodStaffRodItem extends ThaumcraftWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwner {
    public SilverWoodStaffRodItem() {
        super(new Properties());
    }


    public static final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(250 * CENTIVIS_MULTIPLIER));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getCentiVisCapacity() {
        return capacity;
    }

    @Override
    public Map<Aspect, Integer> getCraftingCostCentiVis() {
        return WandUtils.getPrimalAspectMapWithValue(24 * CENTIVIS_MULTIPLIER);
    }
}
