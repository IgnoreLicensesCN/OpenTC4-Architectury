package thaumcraft.common.items.wands.rods.wandrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.INodeHarmfulComponent;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import java.util.Collections;
import java.util.Map;

//dont add crafting method for this,just redirect stick here.
public class WoodWandRodItem extends ThaumcraftWandRodItem implements WorkAsWandRod, INodeHarmfulComponent {
    public WoodWandRodItem() {
        super(new Properties());
    }

    private final @UnmodifiableView Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(25));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }

}
