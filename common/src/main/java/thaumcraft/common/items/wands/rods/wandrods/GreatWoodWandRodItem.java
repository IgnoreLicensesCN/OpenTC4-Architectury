package thaumcraft.common.items.wands.rods.wandrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;

//dont add crafting method for this,just redirect stick here.
public class GreatWoodWandRodItem extends ThaumcraftWandRodItem implements WorkAsWandRod, ICraftingCostAspectOwnerComponent<Aspect> {
    public GreatWoodWandRodItem() {
        super(new Properties());
    }

    private final @UnmodifiableView CentiVisList<Aspect> capacity = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(50 * CENTIVIS_MULTIPLIER);
    @Override
    public @UnmodifiableView CentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(3 * CENTIVIS_MULTIPLIER);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }

}
