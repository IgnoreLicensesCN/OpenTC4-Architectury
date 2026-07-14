package thaumcraft.common.items.abstracts;

import com.linearity.opentc4.utils.collectionlike.IntIntPair;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectDisplayItem;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.HashAspectList;

import java.util.concurrent.atomic.AtomicInteger;

public interface IDefaultEssentiaFueledItem
        extends IBundleLikeItem,
        IAspectDisplayItem<Aspect>{

    @Override
    default void bundleOverrideNotEmptyOnSelf(ItemStack bundleStack, ItemStack stackInSlot, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (canInsertStackToBundle(bundleStack,stackInSlot)){
            var extracted = extractStackAtLastOfBundle(bundleStack);
            if (!extracted.isEmpty()){
                slotAccess.set(extracted);
            }
            insertStackToBundle(bundleStack,stackInSlot);
            this.playInsertToBundleSound(player);
            if (!stackInSlot.isEmpty()) {
                player.addItem(stackInSlot);
            }
        }
    }
    @Override
    default boolean canInsertStackToBundle(ItemStack bundleStack, ItemStack stackToInsert) {
        return stackToInsert.getItem() instanceof IEssentiaFuelProviderItem fuelProviderItem && fuelProviderItem.getFuelEssentiaAmount(stackToInsert,getRequiringAspect()) > 0;
    }

    @Override
    default @NotNull @UnmodifiableView AspectList<Aspect> getAspectsToDisplay(ItemStack itemStack) {
        AspectList<Aspect> owningAspects = new HashAspectList<>();
        var stacksInside = getStacksInsideBundle(itemStack);
        if (!stacksInside.isEmpty()) {
            for (var stackInside : stacksInside) {
                if (stackInside.getItem() instanceof IEssentiaFuelProviderItem fuelProviderItem) {
                    owningAspects.addAll(fuelProviderItem.getEssentiaOwning(stackInside));
                }
            }
        }
        return owningAspects;
    }

    default IntIntPair getFuelProgressAndMaxProgress(ItemStack harnessStack) {
        AtomicInteger capacity = new AtomicInteger(0);
        AtomicInteger progress = new AtomicInteger(0);
        var stacksInside = getStacksInsideBundle(harnessStack);
        var requiringAspect = getRequiringAspect();
        stacksInside.forEach(stack -> {
            if (stack.getItem() instanceof IEssentiaFuelProviderItem fuelProviderItem) {
                capacity.addAndGet(fuelProviderItem.getMaxFuelEssentiaAmount(stack, requiringAspect));
                progress.addAndGet(fuelProviderItem.getFuelEssentiaAmount(stack, requiringAspect));
            }
        });
        return new IntIntPair(capacity.get(), progress.get());
    }

    Aspect getRequiringAspect();
    @Override
    default int getBundleMaxItemCount(ItemStack bundleStack) {
        return 1;
    }

    default boolean consumeFuel(ItemStack harnessStack){
        var stacks = getStacksInsideBundle(harnessStack);
        for (var stack : stacks) {
            if (stack.getItem() instanceof IEssentiaFuelProviderItem fuelProviderItem) {
                if (fuelProviderItem.consumeFuelEssentiaAmount(stack,getRequiringAspect(),1) > 0){
                    setStacksInsideBundle(harnessStack,stacks);
                    return true;
                }
            }
        }
        return false;
    }
}
