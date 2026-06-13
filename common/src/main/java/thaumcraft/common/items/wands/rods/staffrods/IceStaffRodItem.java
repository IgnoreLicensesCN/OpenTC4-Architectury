package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenStaffRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.*;

public class IceStaffRodItem extends ThaumcraftAspectRegenStaffRodItem implements ICraftingCostAspectOwnerComponent<Aspect> {
    public IceStaffRodItem() {
        super(new Properties(), getAspectsCentiVisListWithValue(Aspects.WATER,17 * CENTIVIS_MULTIPLIER));
    }

    private final CentiVisList<Aspect> capacity = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(175 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }
    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(14 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
