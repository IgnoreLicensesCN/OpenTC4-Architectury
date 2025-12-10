package thaumcraft.common.items.wands.wandcaps;

import com.linearity.opentc4.datautils.SimplePair;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.ThaumcraftWandCapItem;

import java.util.List;

public class CopperWandCapItem extends ThaumcraftWandCapItem {//itemWandCap:3
    public CopperWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 1.1f;
    }

    private final List<SimplePair<Aspect,Float>> specialCostModifierAspects = List.of(new SimplePair<>(Aspect.ORDER,1.f),new SimplePair<>(Aspect.ENTROPY,1.f));
    @Override
    public List<SimplePair<Aspect, Float>> getSpecialCostModifierAspects() {
        return specialCostModifierAspects;
    }

}
