package thaumcraft.common.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.items.misc.ItemBathSalts;
import thaumcraft.common.tiles.TileSpa;

public class ContainerSpa extends Container {
   private TileSpa spa;
   private int lastBreakTime;

   public ContainerSpa(InventoryPlayer par1InventoryPlayer, TileSpa tileEntity) {
      this.spa = tileEntity;
      this.addSlotToContainer(new SlotLimitedByClass(ItemBathSalts.class, tileEntity, 0, 65, 31));

      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(int var5 = 0; var5 < 9; ++var5) {
         this.addSlotToContainer(new Slot(par1InventoryPlayer, var5, 8 + var5 * 18, 142));
      }

   }

   public boolean enchantItem(Player p, int button) {
      if (button == 1) {
         this.spa.toggleMix();
      }

      return false;
   }

   public boolean canInteractWith(Player par1Player) {
      return this.spa.isUseableByPlayer(par1Player);
   }

   public ItemStack transferStackInSlot(Player par1Player, int slot) {
      ItemStack stack = null;
      Slot slotObject = (Slot)this.inventorySlots.get(slot);
      if (slotObject != null && slotObject.getHasStack()) {
         ItemStack stackInSlot = slotObject.getStack();
         stack = stackInSlot.copy();
         if (slot == 0) {
            if (!this.spa.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 1, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.spa.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 0, 1, false)) {
            return null;
         }

         if (stackInSlot.stackSize == 0) {
            slotObject.putStack(null);
         } else {
            slotObject.onSlotChanged();
         }
      }

      return stack;
   }
}
