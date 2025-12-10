package thaumcraft.common.items.wands.rods.staffrods;

import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.CraftingCostAspectOwner;
import thaumcraft.api.wands.WandUtils;
import thaumcraft.api.wands.WorkAsStaffRod;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem;

import java.util.Collections;
import java.util.Map;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectMapWithValue;

public class PrimalStaffRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsStaffRod, CraftingCostAspectOwner {
    public PrimalStaffRodItem() {
        super(new Properties().rarity(Rarity.RARE), getPrimalAspectMapWithValue(25));
    }

    private final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(getPrimalAspectMapWithValue(250));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }

    private final Map<Aspect, Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(32));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCraftingCostAspect() {
        return cost;
    }
}
