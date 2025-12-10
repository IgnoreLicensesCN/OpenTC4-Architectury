package thaumcraft.common.items.wands.wandrods;

import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WorkAsStaffRod;

import java.util.Collections;
import java.util.Map;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectMapWithValue;

public class PrimalStaffRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsStaffRod {
    public PrimalStaffRodItem() {
        super(new Properties().rarity(Rarity.RARE), getPrimalAspectMapWithValue(25));
    }

    private final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(getPrimalAspectMapWithValue(250));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }
}
