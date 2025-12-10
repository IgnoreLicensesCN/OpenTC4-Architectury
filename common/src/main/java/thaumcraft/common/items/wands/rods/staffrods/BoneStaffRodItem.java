package thaumcraft.common.items.wands.wandrods;

import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.WorkAsStaffRod;

import java.util.Map;

import static thaumcraft.api.wands.WandUtils.getPrimalAspectMapWithValue;

public class BoneStaffRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsStaffRod {
    public BoneStaffRodItem() {
        super(new Properties(), getPrimalAspectMapWithValue(175));
    }

    private final Map<Aspect, Integer> capacity = Map.of(Aspect.ENTROPY,17);
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getAspectCapacity() {
        return capacity;
    }
}
