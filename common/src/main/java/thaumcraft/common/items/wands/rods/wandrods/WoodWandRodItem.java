package thaumcraft.common.items.wands.rods.wandrods;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import static thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

public class WoodWandRodItem extends ThaumcraftWandRodItem {
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
