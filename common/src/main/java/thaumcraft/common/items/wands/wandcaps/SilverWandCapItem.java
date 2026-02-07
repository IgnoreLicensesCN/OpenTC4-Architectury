package thaumcraft.common.items.wands.wandcaps;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import java.util.Map;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCasted;

public class SilverWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwner<Aspect> {//itemWandCap:4
    public SilverWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 1f;
    }

    private final Map<Aspect,Float> specialCostModifierAspects = Map.of(
            Aspects.AIR,.95f,
            Aspects.EARTH,.95f,
            Aspects.FIRE,.95f,
            Aspects.WATER,.95f
    );
    @Override
    public @NotNull Map<Aspect,Float> getSpecialCostModifierAspects() {
        return specialCostModifierAspects;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCasted(4);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
