package thaumcraft.common.items.wands.wandcaps;

import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.common.items.abstracts.wandabstraction.component.ICraftingCostAspectOwnerComponentItem;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

public class VoidWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwnerComponentItem<Aspect> {//itemWandCap:7
    public VoidWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier(ItemStack stack) {
        return -0.2F;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(9);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
