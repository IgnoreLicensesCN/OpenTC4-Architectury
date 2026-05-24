package thaumcraft.common.items.wands.wandcaps;

import com.linearity.opentc4.simpleutils.UnmodifiableAspectFloatEntry;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import java.util.Map;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

public class SilverWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwnerComponent<Aspect> {//itemWandCap:4
    public SilverWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 1f;
    }

    private final Object2FloatMap<Aspect> specialCostModifierAspects = Object2FloatMap.ofEntries(
            new UnmodifiableAspectFloatEntry<>(Aspects.AIR,.95f),
            new UnmodifiableAspectFloatEntry<>(Aspects.EARTH,.95f),
            new UnmodifiableAspectFloatEntry<>(Aspects.FIRE,.95f),
            new UnmodifiableAspectFloatEntry<>(Aspects.WATER,.95f)
    );
    @Override
    public @NotNull Object2FloatMap<Aspect> getSpecialCostModifierAspects() {
        return specialCostModifierAspects;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(4);
    @Override
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }
}
