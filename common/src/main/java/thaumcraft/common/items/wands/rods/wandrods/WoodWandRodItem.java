package thaumcraft.common.items.wands.rods.wandrods;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.INodeHarmfulComponent;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

//TODO:stick to this
public class WoodWandRodItem extends ThaumcraftWandRodItem implements WorkAsWandRod, INodeHarmfulComponent {
    public WoodWandRodItem() {
        super(new Properties());
    }

    private final @UnmodifiableView CalcCacheableCentiVisList<Aspect> capacity =
            new CalcCacheableCentiVisList<>(
                    getPrimalAspectCentiVisListWithValueCastedUnmodifiable(25 * CENTIVIS_MULTIPLIER),
                    true
            );
    @Override
    public @UnmodifiableView CalcCacheableCentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

}
