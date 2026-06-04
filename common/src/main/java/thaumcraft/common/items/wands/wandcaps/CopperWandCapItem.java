package thaumcraft.common.items.wands.wandcaps;

import com.linearity.opentc4.simpleutils.UnmodifiableAspectFloatEntry;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

public class CopperWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwnerComponent<Aspect> {//itemWandCap:3
    public CopperWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return -0.1f;
    }

    private final Object2FloatMap<Aspect> specialCostModifierAspects = Object2FloatMap.ofEntries(
            new UnmodifiableAspectFloatEntry<>(Aspects.ORDER,0),
            new UnmodifiableAspectFloatEntry<>(Aspects.ENTROPY,0)
            );
    @Override
    public @NotNull Object2FloatMap<Aspect> getSpecialCostModifierAspects() {
        return specialCostModifierAspects;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(2);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
