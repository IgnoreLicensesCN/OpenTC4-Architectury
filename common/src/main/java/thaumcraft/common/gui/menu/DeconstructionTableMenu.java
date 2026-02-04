package thaumcraft.common.gui.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.gui.ThaumcraftGUI;
import thaumcraft.common.gui.slot.DeconstructionTableResultSlot;
import thaumcraft.common.tiles.crafted.DeconstructionTableBlockEntity;

public class DeconstructionTableMenu extends AbstractContainerMenu {
    protected final Inventory inventory;
    protected final DeconstructionTableBlockEntity deconstructionTable;
//    public DeconstructionTableMenu(
//            int containerID,
//            Inventory inventory){
//        this(containerID,inventory,
//                null);
//
//TODO
//
//   }
    public DeconstructionTableMenu(
            int containerID,
            Inventory inventory,
            DeconstructionTableBlockEntity deconstructionTable){
        this(ThaumcraftGUI.DECONSTRUCTION_TABLE,containerID,inventory,deconstructionTable);
    }
    public DeconstructionTableMenu(
            @Nullable MenuType<DeconstructionTableMenu> menuType,
            int containerID,
            Inventory inventory,
            DeconstructionTableBlockEntity deconstructionTable) {
        super(menuType, containerID);
        this.inventory = inventory;
//        this.containerLevelAccess = containerLevelAccess;
        this.deconstructionTable = deconstructionTable;
        this.addSlot(
                new Slot(
                        deconstructionTable,DeconstructionTableBlockEntity.THE_ONLY_SLOT, 84, 25)//TODO:Offset
        );
        this.addSlot(
                new DeconstructionTableResultSlot(deconstructionTable,1,124,25)//TODO:Offset
        );
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        if (index == 0) {
            // 从自定义槽 → 玩家背包
            if (!this.moveItemStackTo(stack, 1, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // 从玩家背包 → 自定义槽
            if (!this.moveItemStackTo(stack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        var blockPos = deconstructionTable.getBlockPos();
        if (player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) <= 64.0){
            return true;
        }
        var level = deconstructionTable.getLevel();
        return level != null && level.getBlockEntity(blockPos) == deconstructionTable;
    }
}
