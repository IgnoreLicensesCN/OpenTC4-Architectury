package thaumcraft.common.items.wands.rods.staffrods;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenStaffRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.*;

public class BoneStaffRodItem extends ThaumcraftAspectRegenStaffRodItem implements ICraftingCostAspectOwnerComponent<Aspect> {
    public BoneStaffRodItem() {
        super(new Properties(), getAspectsCentiVisListWithValue(Aspects.ENTROPY,17 * CENTIVIS_MULTIPLIER));
    }

    private final CalcCacheableCentiVisList<Aspect> capacity = new CalcCacheableCentiVisList<>(
            getPrimalAspectCentiVisListWithValueCastedUnmodifiable(175 * CENTIVIS_MULTIPLIER),
            true
    );
    @Override
    @Unmodifiable
    public CalcCacheableCentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }
    private final CentiVisList<Aspect> cost = (getPrimalAspectCentiVisListWithValueCastedUnmodifiable(14 * CENTIVIS_MULTIPLIER));
    @Override
    @Unmodifiable
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
