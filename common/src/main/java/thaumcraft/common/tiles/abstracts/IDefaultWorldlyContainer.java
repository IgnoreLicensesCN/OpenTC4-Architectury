package thaumcraft.common.tiles.abstracts;

import com.linearity.opentc4.annotations.UtilityLikeAbstraction;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityLikeAbstraction(reason = "i'm lazy to copy-and-paste these methods.")
public interface IDefaultWorldlyContainer extends WorldlyContainer {
    NonNullList<ItemStack> getInventory();

    @Override
    default boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    default boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return canPlaceItem(i, itemStack);
    }

    @Override
    default int getContainerSize() {
        return getInventory().size();
    }


    @Override
    default boolean isEmpty() {
        for (var stackInInventory : getInventory()) {
            if (!stackInInventory.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default @NotNull ItemStack getItem(int i) {
        return getInventory().get(i);
    }

    @Override
    @NotNull
    default ItemStack removeItem(int slot, int amount) {
        ensureInventoryIndexInBound(slot);
        ItemStack stack = getItem(slot);
        if (stack.getCount() <= amount) {
            setItem(slot, ItemStack.EMPTY);
            setChanged();
            return stack;
        } else {
            stack.shrink(amount);
            stack = stack.copy();
            stack.setCount(amount);
            setChanged();
            return stack;
        }
    }

    default void ensureInventoryIndexInBound(int slot) {
        if (isInventoryIndexOutOfBound(slot)) {
            throw new IndexOutOfBoundsException("Index: " + slot);
        }
    }

    int[] getSlots();
    default int getSlotsSize(){
        return getSlots().length;
    }

    default boolean isInventoryIndexOutOfBound(int slot) {
        return slot < 0 || slot >= getSlotsSize();
    }

    @Override
    default int @NotNull [] getSlotsForFace(Direction direction) {
        return getSlots();
    }

    @Override
    @NotNull
    default ItemStack removeItemNoUpdate(int i) {
        var stack = getItem(i);
        setItem(i, ItemStack.EMPTY);
        return stack;
    }

    Level getLevel();

    @Override
    default boolean stillValid(Player player) {
        return getLevel() == player.level();
    }
    void markDirtyAndUpdateSelf();
    void setChanged();

    @Override
    default void setItem(int i, ItemStack itemStack) {
        getInventory().set(i, itemStack);
        setChanged();

        markDirtyAndUpdateSelf();
    }

    @Override
    default void clearContent() {
        getInventory().clear();
        setChanged();
    }
}
