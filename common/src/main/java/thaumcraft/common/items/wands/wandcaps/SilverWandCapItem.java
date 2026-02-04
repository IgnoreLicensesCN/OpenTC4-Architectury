package thaumcraft.common.items.wands.wandcaps;

import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.wands.ICraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import java.util.Collections;
import java.util.Map;

public class SilverWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwner {//itemWandCap:4
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

    private final Map<Aspect,Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(4));
    @Override
    public Map<Aspect, Integer> getCraftingCostCentiVis() {
        return cost;
    }
}
