package thaumcraft.common.items.wands.rods.staffrods;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableCentiVisList;
import thaumcraft.common.items.abstracts.wandabstraction.component.ICraftingCostAspectOwnerComponentItem;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenStaffRodItem;

import static thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.*;

public class BoneStaffRodItem extends ThaumcraftAspectRegenStaffRodItem implements ICraftingCostAspectOwnerComponentItem<Aspect> {
    public BoneStaffRodItem() {
        super(new Properties(), UnmodifiableCentiVisList.of(Aspects.ENTROPY,17 * CENTIVIS_MULTIPLIER));
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
