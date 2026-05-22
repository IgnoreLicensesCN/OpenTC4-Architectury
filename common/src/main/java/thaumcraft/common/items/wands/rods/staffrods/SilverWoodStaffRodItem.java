package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;

public class SilverWoodStaffRodItem extends ThaumcraftWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwnerComponent<Aspect> {
    public SilverWoodStaffRodItem() {
        super(new Properties());
    }


    public static final CentiVisList<Aspect> capacity = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(250 * CENTIVIS_MULTIPLIER);
    @Override
    public @UnmodifiableView CentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

    public static final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(24 * CENTIVIS_MULTIPLIER);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
