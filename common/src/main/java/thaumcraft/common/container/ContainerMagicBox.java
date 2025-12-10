package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.common.tiles.TileMagicBox;

public class ContainerMagicBox extends Container {
   private TileMagicBox box;
   public IInventory playerInv;
   private int numRows;

   public ContainerMagicBox(IInventory par1IInventory, TileMagicBox par2IInventory) {
      this.box = par2IInventory;
      this.numRows = par2IInventory.getSizeInventory() / 9;
      this.playerInv = par1IInventory;
      par2IInventory.openInventory();
      this.bindBoxInventory(0);
      this.bindPlayerInventory();
      if (this.box.getWorldObj() != null && this.box.getWorldObj().isRemote) {
         TileMagicBox var10000 = this.box;
         TileMagicBox.tc = this;
      }

   }

   void bindBoxInventory(int row) {
      for(int j = 0; j < 3; ++j) {
         for(int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(this.box, k + (j + row) * 9, 8 + k * 18, 18 + j * 18));
         }
      }

   }

   void bindPlayerInventory() {
      for(int j = 0; j < 3; ++j) {
         for(int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(this.playerInv, k + j * 9 + 9, 8 + k * 18, 103 + j * 18));
         }
      }

      for(int var3 = 0; var3 < 9; ++var3) {
         this.addSlotToContainer(new Slot(this.playerInv, var3, 8 + var3 * 18, 161));
      }

   }

   public void refreshInventory() {
      this.inventoryItemStacks.clear();
      this.inventorySlots.clear();
      this.bindBoxInventory(0);
      this.bindPlayerInventory();
   }

   public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
      return this.box.isUseableByPlayer(par1EntityPlayer);
   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(par2);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (par2 < this.numRows * 9) {
            if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
            return null;
         }

         if (itemstack1.stackSize == 0) {
            slot.putStack(null);
         } else {
            slot.onSlotChanged();
         }
      }

      return itemstack;
   }

   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
      super.onContainerClosed(par1EntityPlayer);
      this.box.closeInventory();
   }
}
