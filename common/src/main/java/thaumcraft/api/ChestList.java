package thaumcraft.api;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class ChestList extends AbstractList<ItemStack> {

    //will always return new ItemStack not which inserted
    public ItemStack insertItem(ItemStack stackIn) {
        stackIn = stackIn.copy();
        if (stackIn.isEmpty()) return ItemStack.EMPTY;

        // 1️⃣ 先尝试合并
        for (ItemStack stackInSlot : this) {
            if (!stackInSlot.isEmpty()
                    && ItemStack.isSameItemSameTags(stackInSlot, stackIn)
                    && stackInSlot.getCount() < stackInSlot.getMaxStackSize()) {

                int canMove = Math.min(
                        stackIn.getCount(),
                        stackInSlot.getMaxStackSize() - stackInSlot.getCount()
                );

                stackInSlot.grow(canMove);
                stackIn.shrink(canMove);

                if (stackIn.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
        }

        // 2️⃣ 再找空槽
        for (int i = 0; i < this.size(); i++) {
            ItemStack slot = this.get(i);
            if (slot.isEmpty()) {
                this.set(i, stackIn);
                return ItemStack.EMPTY;
            }
        }

        // 3️⃣ 放不下，返回剩余
        return stackIn;
    }
    //stole from NonNullList
    private final List<ItemStack> list;
    private final ItemStack defaultValue = ItemStack.EMPTY;

    public static ChestList create() {
        return new ChestList(Lists.newArrayList());
    }

//    public static ChestList createWithCapacity(int i) {
//        return new ChestList(Lists.newArrayListWithCapacity(i));
//    }

    public static ChestList withSize(int i, ItemStack object) {
        Validate.notNull(object);
        ItemStack[] objects = new ItemStack[i];
        Arrays.fill(objects, object);
        return new ChestList(Arrays.asList(objects));
    }

    public static ChestList of(ItemStack... objects) {
        return new ChestList(Arrays.asList(objects));
    }

	protected ChestList(List<ItemStack> list) {
        this.list = list;
    }

    @NotNull
    public ItemStack get(int i) {
        return (ItemStack)this.list.get(i);
    }

    public ItemStack set(int i, ItemStack object) {
        Validate.notNull(object);
        return (ItemStack)this.list.set(i, object);
    }

    public void add(int i, ItemStack object) {
        Validate.notNull(object);
        this.list.add(i, object);
    }

    public ItemStack remove(int i) {
        return (ItemStack)this.list.remove(i);
    }

    public int size() {
        return this.list.size();
    }

    public void clear() {
        for (int i = 0; i < this.size(); i++) {
            this.set(i, this.defaultValue);
        }
    }
    //stole from ContainerHelper

    public CompoundTag saveAllItems(CompoundTag compoundTag) {
        return saveAllItems(compoundTag, true);
    }

    public CompoundTag saveAllItems(CompoundTag compoundTag, boolean bl) {
        ListTag listTag = new ListTag();

        for (int i = 0; i < this.size(); i++) {
            ItemStack itemStack = this.get(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag2 = new CompoundTag();
                compoundTag2.putByte("Slot", (byte)i);
                itemStack.save(compoundTag2);
                listTag.add(compoundTag2);
            }
        }

        if (!listTag.isEmpty() || bl) {
            compoundTag.put("Items", listTag);
        }

        return compoundTag;
    }

    public void loadAllItems(CompoundTag compoundTag) {
        ListTag listTag = compoundTag.getList("Items", 10);

        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag compoundTag2 = listTag.getCompound(i);
            int j = compoundTag2.getByte("Slot") & 255;
            if (j >= 0 && j < this.size()) {
                this.set(j, ItemStack.of(compoundTag2));
            }
        }
    }

}
