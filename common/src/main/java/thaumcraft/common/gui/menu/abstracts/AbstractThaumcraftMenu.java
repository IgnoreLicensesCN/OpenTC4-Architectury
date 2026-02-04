package thaumcraft.common.gui.menu.abstracts;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractThaumcraftMenu extends AbstractContainerMenu {

    protected AbstractThaumcraftMenu(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
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


}
