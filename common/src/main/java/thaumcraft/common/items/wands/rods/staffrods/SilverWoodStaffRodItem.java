package thaumcraft.common.items.wands.wandrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.ThaumcraftWandRodItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SilverWoodStaffRodItem extends ThaumcraftWandRodItem implements WorkAsStaffRod {
    public SilverWoodStaffRodItem() {
        super(new Properties());
    }


    public static final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(250));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }
}
