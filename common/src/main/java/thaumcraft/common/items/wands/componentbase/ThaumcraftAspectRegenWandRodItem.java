package thaumcraft.common.items.wands.componentbase;

import com.linearity.opentc4.utils.collectionlike.ObjectIntPair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.CentiVisList;
import thaumcraft.common.items.abstracts.wandabstraction.component.IInventoryTickableComponentItem;
import thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem;

import java.util.ArrayList;
import java.util.List;

import static thaumcraft.common.items.abstracts.wandabstraction.wand.ICentiVisContainerItem.CENTIVIS_MULTIPLIER;

public abstract class ThaumcraftAspectRegenWandRodItem extends ThaumcraftWandRodItem implements IInventoryTickableComponentItem {
    public ThaumcraftAspectRegenWandRodItem(Properties properties, CentiVisList<Aspect> canRegenCentiVisAndValue) {
        super(properties);
        this.canRegenCentiVisAndValue = canRegenCentiVisAndValue;
    }
    protected final @Unmodifiable CentiVisList<Aspect> canRegenCentiVisAndValue;

    @Override
    public void tickAsComponent(@NotNull ItemStack finalParentStack, @NotNull ItemStack usingWand, @NotNull ItemStack selfStack, Level level, Entity owner, int finalParentAtContainerIndex, boolean parentSelected) {
        regenCentiVisAsComponent(usingWand, owner,canRegenCentiVisAndValue);
    }

    public static void regenCentiVisAsComponent(@NotNull ItemStack usingWand, Entity owner,CentiVisList<Aspect> canRegenCentiVisAndValue) {
        var wandStackItem = usingWand.getItem();
        if (wandStackItem instanceof ICentiVisContainerItem<?> containerNotCasted && containerNotCasted.tryCastAspectClass(Aspect.class)) {
            var container = (ICentiVisContainerItem<Aspect>) containerNotCasted;
            if (owner.tickCount % 200 == 0){
                var owningVis = container.getAllCentiVisOwning(usingWand);
                canRegenCentiVisAndValue.forEach(
                        ((aspect, upperBound) -> {
                            var owningAspectValue = owningVis.getOrDefault(aspect,0);
                            if (owningAspectValue < upperBound) {
                                owningVis.merge(aspect,
                                        CENTIVIS_MULTIPLIER,
                                        (oldValue, newValue) ->
                                                Math.min(oldValue + newValue, upperBound));
                            }
                        })
                );
                container.storeCentiVisOwning(usingWand, owningVis);
            }
        }
    }
}
