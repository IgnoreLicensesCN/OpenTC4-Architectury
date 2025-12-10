package thaumcraft.common.items.wands.componentbase;

import com.linearity.opentc4.datautils.SimplePair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.InventoryTickableComponent;
import thaumcraft.api.wands.VisContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ThaumcraftAspectRegenWandRodItem extends ThaumcraftWandRodItem implements InventoryTickableComponent {
    public ThaumcraftAspectRegenWandRodItem(Properties properties, Map<Aspect,Integer> canRegenAspectAndValue) {
        super(properties);
        this.canRegenAspectAndValue = canRegenAspectAndValue;
        this.canRegenAspectAndValueAsList = canRegenAspectAndValue.entrySet().stream().map(e -> new SimplePair(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
    protected final Map<Aspect, Integer> canRegenAspectAndValue;
    protected final List<SimplePair<Aspect, Integer>> canRegenAspectAndValueAsList;

    @Override
    public void tickAsComponent(ItemStack usingWand, Level level, Entity entity, int i, boolean bl) {
        var wandStackItem = usingWand.getItem();
        if (wandStackItem instanceof VisContainer container) {
            if (entity.tickCount % 200 == 0){
                var capacity = container.getAllVisCapacity(usingWand);
                var owningVis = container.getAllVisOwning(usingWand);
                for (Map.Entry<Aspect,Integer> entry:capacity.entrySet()) {
                    var owningAspectValue = owningVis.getOrDefault(entry.getKey(),0);
                    if (owningAspectValue < (entry.getValue()/10)) {
                        owningVis.merge(entry.getKey(),1,Integer::sum);
                    }
                }
            }else
            if (entity.tickCount % 200 == 0){
                var owningVis = container.getAllVisOwning(usingWand);
                for (Map.Entry<Aspect,Integer> entry:canRegenAspectAndValue.entrySet()) {
                    var owningAspectValue = owningVis.getOrDefault(entry.getKey(),0);
                    if (owningAspectValue < entry.getValue()) {
                        owningVis.merge(entry.getKey(),1,Integer::sum);
                    }
                }
                container.storeVisOwning(usingWand, owningVis);
            } else if (entity.tickCount % 50 == 0) {


                var owningVis = container.getAllVisOwning(usingWand);
                List<Aspect> candidates = new ArrayList<>();
                for (var entry : canRegenAspectAndValue.entrySet()) {
                    var aspect = entry.getKey();
                    var maxValue = entry.getValue();
                    var current = owningVis.getOrDefault(aspect, 0);

                    if (current < maxValue) {
                        candidates.add(aspect);
                    }
                }

                // 如果全部都满了 → 不回复
                if (!candidates.isEmpty()) {
                    Aspect chosen = candidates.get(level.random.nextInt(candidates.size()));
                    owningVis.merge(chosen, 1, Integer::sum);
                    container.storeVisOwning(usingWand, owningVis);
                }

            }
        }
    }
}
