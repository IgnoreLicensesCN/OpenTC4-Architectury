package thaumcraft.common.items.wands.rods.staffrods;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.*;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenWandRodItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static thaumcraft.api.wands.ICentiVisContainer.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectMapWithValue;

public class PrimalStaffRodItem extends ThaumcraftAspectRegenWandRodItem implements WorkAsStaffRod, ICraftingCostAspectOwner, IWandUpgradeModifier {
    public PrimalStaffRodItem() {
        super(new Properties().rarity(Rarity.RARE), getPrimalAspectMapWithValue(25 * CENTIVIS_MULTIPLIER));
    }

    private final Map<Aspect, Integer> capacity = Collections.unmodifiableMap(getPrimalAspectMapWithValue(250 * CENTIVIS_MULTIPLIER));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCentiVisCapacity() {
        return capacity;
    }

    private final Map<Aspect, Integer> cost = Collections.unmodifiableMap(WandUtils.getPrimalAspectMapWithValue(32 * CENTIVIS_MULTIPLIER));
    @Override
    @UnmodifiableView
    public Map<Aspect, Integer> getCraftingCostCentiVis() {
        return cost;
    }

    @Override
    public Map<FocusUpgradeType, Integer> modifyWandUpgrades(Map<FocusUpgradeType, Integer> wandUpgrades) {
        wandUpgrades.merge(FocusUpgradeType.potency,1,Integer::sum);
        return wandUpgrades;
    }

    @Override
    public void tickAsComponent(ItemStack usingWand, Level level, Entity entity, int i, boolean bl) {
        var wandItem = usingWand.getItem();
        if (wandItem instanceof ICentiVisContainer container){
            if (entity.tickCount % 50 == 0) {
                var owningVis = container.getAllCentiVisOwning(usingWand);
                List<Aspect> candidates = new ArrayList<>();
                for (var entry : canRegenCentiVisAndValue.entrySet()) {
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
                    owningVis.merge(chosen, CENTIVIS_MULTIPLIER,
                            (oldValue, newValue) -> Math.min(
                                    oldValue + newValue,
                                    canRegenCentiVisAndValue.getOrDefault(chosen, 0)
                            )
                            );
                    container.storeCentiVisOwning(usingWand, owningVis);
                }
            }
        }
    }
}
