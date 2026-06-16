package thaumcraft.common.items.abstracts;

import com.linearity.opentc4.annotations.Modifiable;
import com.linearity.opentc4.annotations.ModifiableCopy;
import com.linearity.opentc4.annotations.StoleFrom;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.linearity.opentc4.Consts.BundleLikeItemTagAccessors.BUNDLE_STACKS;

//yeah guys i failed to make a backpack item so using this
//changed weight to item count(at least for default impl)
@StoleFrom({"minecraft","BundleItem"})
public interface IBundleLikeItem {
    int getBundleMaxItemCount(ItemStack bundleStack);
    default @ModifiableCopy List<ItemStack> getStacksInsideBundle(ItemStack bundleStack){
        var tag = bundleStack.getTag();
        if(tag == null){
            return new ArrayList<>();
        }
        return BUNDLE_STACKS.readFromCompoundTag(tag);
    };
    default void setStacksInsideBundle(ItemStack bundleStack, List<ItemStack> stacks){
        var tag = bundleStack.getOrCreateTag();
        BUNDLE_STACKS.writeToCompoundTag(tag, stacks);
    }
    default ItemStack getStackAtBundle(ItemStack bundleStack, int index){
        var tag = bundleStack.getTag();
        if(tag == null){
            return ItemStack.EMPTY;
        }
        var stacks = BUNDLE_STACKS.readFromCompoundTag(tag);
        if (index >= stacks.size()) {
            return ItemStack.EMPTY;
        }
        return stacks.get(index);
    }
    //return remaining stack
    default void insertStackToBundle(ItemStack bundleStack, @Modifiable /*the count will be modified*/ ItemStack stackToInsert){

        if (!canInsertStackToBundle(bundleStack,stackToInsert)) {
            return;
        }
        var stacksGot = getStacksInsideBundle(bundleStack);
        int sizeRemaining = getBundleMaxItemCount(bundleStack);
        for (ItemStack stack : stacksGot) {
            sizeRemaining -= stack.getCount();
        }
        if (sizeRemaining <= 0) {
            return;
        }

        var stacks = new ArrayList<ItemStack>(stacksGot.size() + 1);

        stacks.addAll(stacksGot);
        //stack on others
        for (var alreadyInStack:stacks) {
            if (ItemStack.isSameItemSameTags(alreadyInStack, stackToInsert)) {
                int canInsert = Math.min(
                        Math.min(
                                sizeRemaining,
                                alreadyInStack.getMaxStackSize() -  alreadyInStack.getCount()
                        )
                        ,       stackToInsert.getCount()
                );
                if (canInsert > 0) {
                    alreadyInStack.setCount(canInsert+alreadyInStack.getCount());
                    stackToInsert.shrink(canInsert);
                }
                if (stackToInsert.isEmpty()) {
                    break;
                }
                sizeRemaining -= canInsert;
                if (sizeRemaining <= 0) {
                    break;
                }
            }
        }
        if (!stackToInsert.isEmpty() && sizeRemaining > 0) {
            stacks.add(stackToInsert.split(sizeRemaining));//extract as many as possible so sizeRemaining
        }
        setStacksInsideBundle(bundleStack, stacks);
    }
    //does not check size
    default boolean canInsertStackToBundle(ItemStack bundleStack, ItemStack stackToInsert){
        return true;
    }
    default @Modifiable ItemStack extractStackAtLastOfBundle(ItemStack bundleStack){
        var stacks = getStacksInsideBundle(bundleStack);
        if (stacks.isEmpty()) {
            return ItemStack.EMPTY;
        }
        var stackGot = stacks.removeLast();
        setStacksInsideBundle(bundleStack, stacks);
        return stackGot;
    }
    default @Modifiable ItemStack extractStackAtBundle(ItemStack bundleStack, int index){
        var stacks = new ArrayList<>(getStacksInsideBundle(bundleStack));
        if (index >= stacks.size()) {
            return ItemStack.EMPTY;
        }
        var stackGot = stacks.remove(index);
        setStacksInsideBundle(bundleStack, stacks);
        return stackGot;
    }

    default void playRemoveOneFromBundleSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    default void playInsertToBundleSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    default void playBundleDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    default boolean bundleOverrideStackedOnOther(ItemStack harnessStack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        }
        ItemStack stackInSlot = slot.getItem();
        if (stackInSlot.isEmpty()) {
            var extracted = extractStackAtLastOfBundle(harnessStack);
            if (!extracted.isEmpty()) {
                this.playRemoveOneFromBundleSound(player);
                slot.safeInsert(extracted);
            }
        } else if (stackInSlot.getItem().canFitInsideContainerItems()) {
            int countBeforeInsert = stackInSlot.getCount();
            insertStackToBundle(harnessStack, stackInSlot);
            if (stackInSlot.getCount() != countBeforeInsert){
                this.playInsertToBundleSound(player);
            }
        }
        return true;
    }
    default boolean bundleOverrideOtherStackedOnMe(ItemStack harnessStack, ItemStack stackInSlot, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY){
            return false;
        }
        if (!slot.allowModification(player)){
            return false;
        }

        if (stackInSlot.isEmpty()) {
            var extracted = extractStackAtLastOfBundle(harnessStack);
            if (!extracted.isEmpty()){
                this.playRemoveOneFromBundleSound(player);
                slotAccess.set(extracted);
            }
        } else {
            int countBeforeInsert = stackInSlot.getCount();
            insertStackToBundle(harnessStack, stackInSlot);
            if (stackInSlot.getCount() != countBeforeInsert){
                this.playInsertToBundleSound(player);
            }
        }

        return true;
    }
}
