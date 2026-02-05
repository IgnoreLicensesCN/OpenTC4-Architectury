package thaumcraft.common.gui.menu.abstracts;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.tiles.crafted.ResearchTableBlockEntity;

public abstract class AbstractThaumcraftMenu<BE extends BlockEntity> extends AbstractContainerMenu {
    protected final BE blockEntity;
    protected final int interactingContainerSize;

    protected AbstractThaumcraftMenu(@Nullable MenuType<?> menuType, int i,BE blockEntity,int interactingContainerSize) {
        super(menuType, i);
        this.blockEntity = blockEntity;
        this.interactingContainerSize = interactingContainerSize;
    }
    protected void addPlayerInventorySlots(Inventory inventory) {
        addPlayerInventoryInnerSlots(inventory, 9, 8, 84);
        addPlayerInventoryHotbarSlots(inventory, 0, 8, 142);
    }

    protected void addPlayerInventoryInnerSlots(
            Inventory inv,
            int startIndex,   // 从 inv 的哪个 slot 开始
            int x,
            int y
    ){
        addPlayerInventoryInnerSlots(inv,startIndex,x,y,18,18);
    }
    protected void addPlayerInventoryInnerSlots(
            Inventory inv,
            int startIndex,   // 从 inv 的哪个 slot 开始
            int x,
            int y,
            int dx,int dy
    ) {
        addSlotGrid(inv, startIndex, 9, 3, x, y, dx, dy);
    }
    protected void addPlayerInventoryHotbarSlots(
            Inventory inv,
            int startIndex,   // 从 inv 的哪个 slot 开始
            int x,
            int y
    ){
        addPlayerInventoryHotbarSlots(inv,startIndex,x,y,18,18);
    }
    protected void addPlayerInventoryHotbarSlots(
            Inventory inv,
            int startIndex,   // 从 inv 的哪个 slot 开始
            int x,
            int y,
            int dx,int dy
    ) {
        addSlotGrid(inv, startIndex, 9, 1, x, y, dx, dy);
    }

    protected void addSlotGrid(
            Container container,
            int startIndex,
            int columns,
            int rows,
            int x,
            int y,
            int dx,
            int dy
    ) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int index = startIndex + col + row * columns;
                this.addSlot(new Slot(
                        container,
                        index,
                        x + col * dx,
                        y + row * dy
                ));
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        var blockPos = blockEntity.getBlockPos();
        if (player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) <= 64.0){
            return true;
        }
        var level = blockEntity.getLevel();
        return level != null && level.getBlockEntity(blockPos) == blockEntity;
    }


    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if (index < interactingContainerSize) {
                // 容器 → 玩家背包
                if (!this.moveItemStackTo(stack,
                        interactingContainerSize,
                        this.slots.size(),
                        true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 玩家背包 → 容器
                if (!this.moveItemStackTo(stack,
                        0,
                        interactingContainerSize,
                        false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return ret;
    }
}
