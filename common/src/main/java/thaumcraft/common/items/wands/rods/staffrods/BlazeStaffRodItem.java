package thaumcraft.common.items.wands.rods.staffrods;

import com.linearity.opentc4.utils.collectionlike.obj2intcalc.CalcCacheableCentiVisList;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.common.items.abstracts.wandabstraction.component.ICraftingCostAspectOwnerComponentItem;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenStaffRodItem;

import java.util.List;

import static thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getAspectsCentiVisListWithValue;

public class BlazeStaffRodItem extends ThaumcraftAspectRegenStaffRodItem
        implements ICraftingCostAspectOwnerComponentItem<Aspect> {
    public BlazeStaffRodItem() {
        super(new Properties(), getAspectsCentiVisListWithValue(List.of(Aspects.FIRE),17 * CENTIVIS_MULTIPLIER));
    }

    private final CalcCacheableCentiVisList<Aspect> capacity =
            new CalcCacheableCentiVisList<>(getAspectsCentiVisListWithValue(Aspects.getPrimalAspectsCasted(),175 * CENTIVIS_MULTIPLIER),true);
    @Override
    @UnmodifiableView
    public CalcCacheableCentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }
    private static final CentiVisList<Aspect> cost = WandUtils.getAspectsCentiVisListWithValue(Aspects.getPrimalAspectsCasted(),14 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
