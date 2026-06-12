package thaumcraft.common.items.wands.wandcaps;

import com.linearity.opentc4.utils.collectionlike.UnmodifiableAspectFloatEntry;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.Aspects;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.ICraftingCostAspectOwnerComponent;
import thaumcraft.common.items.wands.componentbase.ThaumcraftWandCapItem;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

public class SilverWandCapItem extends ThaumcraftWandCapItem implements ICraftingCostAspectOwnerComponent<Aspect> {//itemWandCap:4
    public SilverWandCapItem() {
        super(new Properties());
    }


    @Override
    public float getBaseCostModifier() {
        return 0;
    }

    private final Object2FloatMap<Aspect> specialCostModifierAspects = Object2FloatMap.ofEntries(
            new UnmodifiableAspectFloatEntry<>(Aspects.AIR,.05f),
            new UnmodifiableAspectFloatEntry<>(Aspects.EARTH,.05f),
            new UnmodifiableAspectFloatEntry<>(Aspects.FIRE,.05f),
            new UnmodifiableAspectFloatEntry<>(Aspects.WATER,.05f)
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
