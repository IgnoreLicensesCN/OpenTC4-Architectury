package thaumcraft.common.items.wands.rods.wandrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import java.util.Collections;
import java.util.Map;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;

//dont add crafting method for this,just redirect stick here.
public class SilverWoodWandRodItem extends ThaumcraftWandRodItem implements WorkAsWandRod, ICraftingCostAspectOwner {
    public SilverWoodWandRodItem() {
        super(new Properties());
    }

    private final @UnmodifiableView Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(100 * CENTIVIS_MULTIPLIER));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getCentiVisCapacity() {
        return capacity;
    }

    private final Map<Aspect,Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(9 * CENTIVIS_MULTIPLIER));
    @Override
    public Map<Aspect, Integer> getCraftingCostCentiVis() {
        return cost;
    }

}
