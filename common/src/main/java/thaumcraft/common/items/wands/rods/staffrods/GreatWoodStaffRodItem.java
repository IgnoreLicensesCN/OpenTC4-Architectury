package thaumcraft.common.items.wands.wandrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.ThaumcraftWandRodItem;

import java.util.Collections;
import java.util.Map;

public class GreatWoodStaffRodItem extends ThaumcraftWandRodItem implements WorkAsStaffRod {
    public GreatWoodStaffRodItem() {
        super(new Properties());
    }


    public static final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(125));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }
}
