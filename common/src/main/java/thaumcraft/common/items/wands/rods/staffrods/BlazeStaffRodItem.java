package thaumcraft.common.items.wands.rods.staffrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem;

import java.util.List;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getAspectsCentiVisListWithValue;

public class BlazeStaffRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwner<Aspect> {
    public BlazeStaffRodItem() {
        super(new Properties(), getAspectsCentiVisListWithValue(List.of(Aspects.FIRE),17 * CENTIVIS_MULTIPLIER));
    }

    private final CentiVisList<Aspect> capacity = getAspectsCentiVisListWithValue(Aspects.getPrimalAspectsCasted(),175 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }
    private static final CentiVisList<Aspect> cost = WandUtils.getAspectsCentiVisListWithValue(Aspects.getPrimalAspectsCasted(),14 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
