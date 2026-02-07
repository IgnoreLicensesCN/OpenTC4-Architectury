package thaumcraft.common.items.wands.rods.wandrods;

import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.INodeHarmfulComponent;
import thaumcraft.api.wands.WorkAsWandRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandRodItem;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCasted;

//dont add crafting method for this,just redirect stick here.
public class WoodWandRodItem extends ThaumcraftWandRodItem implements WorkAsWandRod, INodeHarmfulComponent {
    public WoodWandRodItem() {
        super(new Properties());
    }

    private final @UnmodifiableView CentiVisList<Aspect> capacity = getPrimalAspectCentiVisListWithValueCasted(25 * CENTIVIS_MULTIPLIER);
    @Override
    public @UnmodifiableView CentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

}
