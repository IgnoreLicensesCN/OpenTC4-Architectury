package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCasted;

public class GreatWoodStaffRodItem extends ThaumcraftWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwner<Aspect> {
    public GreatWoodStaffRodItem() {
        super(new Properties());
    }

    public static final CentiVisList<Aspect> capacity = getPrimalAspectCentiVisListWithValueCasted(125 * CENTIVIS_MULTIPLIER);
    @Override
    public @UnmodifiableView CentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCasted(8 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
