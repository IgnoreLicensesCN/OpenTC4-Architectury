package thaumcraft.common.items.wands.componentbase;

import com.linearity.opentc4.simpleutils.SimplePair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.CentiVisList;
import thaumcraft.api.wands.InventoryTickableComponent;
import thaumcraft.api.wands.ICentiVisContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;

public abstract class ThaumcraftAspectRegenWandRodItem extends ThaumcraftWandRodItem implements InventoryTickableComponent {
    public ThaumcraftAspectRegenWandRodItem(Properties properties, CentiVisList<Aspect> canRegenCentiVisAndValue) {
        super(properties);
        this.canRegenCentiVisAndValue = canRegenCentiVisAndValue;
        this.canRegenCentiVisAndValueAsList = canRegenCentiVisAndValue.entrySet().stream().map(e -> new SimplePair<>(e.getKey(), e.getValue())).toList();
    }
    protected final CentiVisList<Aspect> canRegenCentiVisAndValue;
    protected final List<SimplePair<Aspect, Integer>> canRegenCentiVisAndValueAsList;

    @Override
    public void tickAsComponent(ItemStack usingWand, Level level, Entity entity, int i, boolean bl) {
        var wandStackItem = usingWand.getItem();
        if (wandStackItem instanceof ICentiVisContainer<?> containerNotCasted && containerNotCasted.tryCastAspectClass(Aspect.class)) {
            var container = (ICentiVisContainer<Aspect>) containerNotCasted;
            if (entity.tickCount % 200 == 0){
                var owningVis = container.getAllCentiVisOwning(usingWand);
                for (Map.Entry<Aspect,Integer> entry: canRegenCentiVisAndValue.entrySet()) {
                    var owningAspectValue = owningVis.getOrDefault(entry.getKey(),0);
                    var upperBound = entry.getValue();
                    if (owningAspectValue < upperBound) {
                        owningVis.merge(entry.getKey(),
                                CENTIVIS_MULTIPLIER,
                                (oldValue, newValue) ->
                                Math.min(oldValue + newValue, upperBound));
                    }
                }
                container.storeCentiVisOwning(usingWand, owningVis);
            }
        }
    }
}
