package thaumcraft.common.items.wands.rods.staffrods;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftStaffRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

public class GreatWoodStaffRodItem extends ThaumcraftStaffRodItem implements ICraftingCostAspectOwnerComponent<Aspect> {
    public GreatWoodStaffRodItem() {
        super(new Properties());
    }

    public static final CalcCacheableCentiVisList<Aspect> capacity = new CalcCacheableCentiVisList<>(getPrimalAspectCentiVisListWithValueCastedUnmodifiable(125 * CENTIVIS_MULTIPLIER),true);
    @Override
    public @UnmodifiableView CalcCacheableCentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(8 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
