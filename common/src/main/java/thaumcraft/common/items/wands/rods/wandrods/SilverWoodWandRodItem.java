package thaumcraft.common.items.wands.rods.wandrods;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

public class SilverWoodWandRodItem extends ThaumcraftWandRodItem implements WorkAsWandRod, ICraftingCostAspectOwnerComponent<Aspect> {
    public SilverWoodWandRodItem() {
        super(new Properties());
    }

    private final @UnmodifiableView CalcCacheableCentiVisList<Aspect> capacity =
            new CalcCacheableCentiVisList<>(
                    getPrimalAspectCentiVisListWithValueCastedUnmodifiable(100 * CENTIVIS_MULTIPLIER),
                    true
            );
    @Override
    public @UnmodifiableView CalcCacheableCentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(9 * CENTIVIS_MULTIPLIER);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }

}
