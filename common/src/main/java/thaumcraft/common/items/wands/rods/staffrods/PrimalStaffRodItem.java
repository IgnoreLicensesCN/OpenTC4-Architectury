package thaumcraft.common.items.wands.rods.staffrods;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.api.wands.*;
import thaumcraft.api.wands.focus.upgrade.FocusUpgradeType;
import thaumcraft.api.wands.focus.upgrade.ThaumcraftFocusUpgradeTypes;
import thaumcraft.common.items.wands.componentbase.ThaumcraftAspectRegenStaffRodItem;

import java.util.ArrayList;
import java.util.List;

import static thaumcraft.api.wands.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;
import static thaumcraft.api.wands.WandUtils.getPrimalAspectCentiVisListWithValueCastedUnmodifiable;

public class PrimalStaffRodItem extends ThaumcraftAspectRegenStaffRodItem implements ICraftingCostAspectOwnerComponent<Aspect>, IWandUpgradeModifier {
    public PrimalStaffRodItem() {
        super(new Properties().rarity(Rarity.RARE), getPrimalAspectCentiVisListWithValueCastedUnmodifiable(25 * CENTIVIS_MULTIPLIER));
    }

    private final CentiVisList<Aspect> capacity = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(250 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCentiVisCapacity() {
        return capacity;
    }

    private final CentiVisList<Aspect> cost = getPrimalAspectCentiVisListWithValueCastedUnmodifiable(32 * CENTIVIS_MULTIPLIER);
    @Override
    @UnmodifiableView
    public CentiVisList<Aspect> getCraftingCostCentiVis() {
        return cost;
    }

    @Override
    public Object2IntMap<FocusUpgradeType> modifyWandUpgrades(ItemStack componentStack, Object2IntMap<FocusUpgradeType> wandUpgrades) {
        wandUpgrades.merge(ThaumcraftFocusUpgradeTypes.POTENCY,1,Integer::sum);
        return wandUpgrades;
    }

    @Override
    public void tickAsComponent(@NotNull ItemStack finalParentStack, @NotNull ItemStack usingWand, @NotNull ItemStack selfStack, Level level, Entity owner, int finalParentAtContainerIndex, boolean parentSelected) {
        var wandItem = usingWand.getItem();
        if (wandItem instanceof ICentiVisContainerItem<? extends Aspect> containerNotCasted){
            var container = (ICentiVisContainerItem<Aspect>)containerNotCasted;
            if (owner.tickCount % 50 == 0) {
                var owningVis = container.getAllCentiVisOwning(usingWand);
                List<Aspect> candidates = new ArrayList<>(canRegenCentiVisAndValue.size());
                canRegenCentiVisAndValue.forEach(((aspect, maxValue) -> {
                    var current = owningVis.getOrDefault(aspect, 0);

                    if (current < maxValue) {
                        candidates.add(aspect);
                    }
                }));

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
