package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.*;

public class BoneStaffRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwnerComponent<Aspect> {
    public BoneStaffRodItem() {
        super(new Properties(), getAspectsCentiVisListWithValue(Aspects.ENTROPY,17 * CENTIVIS_MULTIPLIER));
    }

    private final CentiVisList<Aspect> capacity = (getPrimalAspectCentiVisListWithValueCastedUnmodifiable(175 * CENTIVIS_MULTIPLIER));
    @Override
    @Unmodifiable
    public CentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }
    private final CentiVisList<Aspect> cost = (getPrimalAspectCentiVisListWithValueCastedUnmodifiable(14 * CENTIVIS_MULTIPLIER));
    @Override
    @Unmodifiable
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
