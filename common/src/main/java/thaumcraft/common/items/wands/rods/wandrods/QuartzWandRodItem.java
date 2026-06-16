package thaumcraft.common.items.wands.rods.wandrods;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.*;

public class QuartzWandRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsWandRod, ICraftingCostAspectOwnerComponent<Aspect> {
    public QuartzWandRodItem() {
        super(new Properties(), getAspectsCentiVisListWithValue(Aspects.ORDER,7 * CENTIVIS_MULTIPLIER));
    }

    private final CalcCacheableCentiVisList<Aspect> capacity = new CalcCacheableCentiVisList<>(
            getPrimalAspectCentiVisListWithValueCastedUnmodifiable(75 * CENTIVIS_MULTIPLIER),
            true
    );
    @Override
    @UnmodifiableView
    public CalcCacheableCentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(6 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
