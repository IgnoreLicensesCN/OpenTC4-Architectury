package thaumcraft.common.items.wands.wandcaps;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import java.util.Collections;
import java.util.Map;

public class CopperWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwner {//itemWandCap:3
    public CopperWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 1.1f;
    }

    private final Map<Aspect,Float> specialCostModifierAspects = Map.of(Aspects.ORDER,1.f, Aspects.ENTROPY,1.f);
    @Override
    public @NotNull Map<Aspect,Float> getSpecialCostModifierAspects() {
        return specialCostModifierAspects;
    }

    private final Map<Aspect,Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(2));
    @Override
    public Map<Aspect, Integer> getCraftingCostCentiVis() {
        return cost;
    }
}
