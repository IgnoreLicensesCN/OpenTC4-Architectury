package thaumcraft.common.items.wands.rods.wandrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.INodeHarmfulComponent;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import java.util.Collections;
import java.util.Map;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;

//dont add crafting method for this,just redirect stick here.
public class WoodWandRodItem extends ThaumcraftWandRodItem implements WorkAsWandRod, INodeHarmfulComponent {
    public WoodWandRodItem() {
        super(new Properties());
    }

    private final @UnmodifiableView Map<Aspect, Integer> capacity = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(25 * CENTIVIS_MULTIPLIER));
    @Override
    public @UnmodifiableView Map<Aspect, Integer> getCentiVisCapacity() {
        return capacity;
    }

}
