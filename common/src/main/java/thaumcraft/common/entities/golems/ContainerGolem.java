package thaumcraft.common.entities.golems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.common.container.ContainerGhostSlots;
import thaumcraft.common.container.SlotGhost;
import thaumcraft.common.container.SlotGhostFluid;
import thaumcraft.common.entities.InventoryMob;

public class ContainerGolem extends ContainerGhostSlots {
   public InventoryMob mobInv;
   public InventoryPlayer playerInv;
   public int currentScroll = 0;
   public int maxScroll = 0;

   public ContainerGolem(InventoryPlayer iinventory, InventoryMob iinventory1) {
      this.mobInv = iinventory1;
      this.playerInv = iinventory;
      ((EntityGolemBase)this.mobInv.ent).paused = true;
      if (ItemGolemCore.hasInventory(((EntityGolemBase) this.mobInv.ent).getCore())) {
         this.bindGolemInventory();
      }

      this.bindPlayerInventory();
   }

   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
      ((EntityGolemBase)this.mobInv.ent).paused = false;
   }

   protected void bindGolemInventory() {
      int slots = this.mobInv.slotCount;
      this.maxScroll = slots / 6 - 1;

      for(int a = 0; a < Math.min(6, slots); ++a) {
         if (((EntityGolemBase) this.mobInv.ent).getCore() == 0) {
            this.addSlotToContainer(new SlotGhost(this.mobInv, a + this.currentScroll * 6, 100 + a / 2 * 28, 16 + a % 2 * 31));
         }

         if (((EntityGolemBase) this.mobInv.ent).getCore() == 5) {
            this.addSlotToContainer(new SlotGhostFluid(this.mobInv, a + this.currentScroll * 6, 100 + a / 2 * 28, 16 + a % 2 * 31));
         } else {
            this.addSlotToContainer(new SlotGhost(this.mobInv, a + this.currentScroll * 6, 100 + a / 2 * 28, 16 + a % 2 * 31, 1));
         }
      }

   }

   public void refreshInventory() {
      this.inventoryItemStacks.clear();
      this.inventorySlots.clear();
      if (ItemGolemCore.hasInventory(((EntityGolemBase) this.mobInv.ent).getCore())) {
         this.bindGolemInventory();
      }

      this.bindPlayerInventory();
   }

   protected void bindPlayerInventory() {
      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(this.playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(int i = 0; i < 9; ++i) {
         this.addSlotToContainer(new Slot(this.playerInv, i, 8 + i * 18, 142));
      }

   }

   public boolean enchantItem(EntityPlayer par1EntityPlayer, int button) {
      if (button == 66 && this.currentScroll > 0) {
         --this.currentScroll;
         this.refreshInventory();
      }

      if (button == 67 && this.currentScroll < this.maxScroll) {
         ++this.currentScroll;
         this.refreshInventory();
      }

      if (button >= 50 && button <= 57) {
         ((EntityGolemBase) this.mobInv.ent).setToggle(button - 50, !((EntityGolemBase) this.mobInv.ent).getToggles()[button - 50]);
      }

      int slots = this.mobInv.slotCount;
      if (button >= 0 && button < slots) {
         int c = ((EntityGolemBase) this.mobInv.ent).getColors(button) - 1;
         if (c < -1) {
            c = 15;
         }

         ((EntityGolemBase) this.mobInv.ent).setColors(button, c);
      }

      if (button >= slots && button < slots * 2) {
         int c = ((EntityGolemBase) this.mobInv.ent).getColors(button - slots) + 1;
         if (c > 15) {
            c = -1;
         }

         ((EntityGolemBase) this.mobInv.ent).setColors(button - slots, c);
      }

      this.mobInv.ent.worldObj.playSoundEffect(this.mobInv.ent.posX, this.mobInv.ent.posY, this.mobInv.ent.posZ, "random.click", 0.2F, 0.8F);
      return true;
   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
      ItemStack stack = null;
      Slot slotObject = (Slot)this.inventorySlots.get(slot);
      int slots = this.mobInv.slotCount;
      if (slotObject != null && slotObject.getHasStack()) {
         ItemStack stackInSlot = slotObject.getStack();
         stack = stackInSlot.copy();
         if (slot < slots) {
            if (!this.mergeItemStack(stackInSlot, slots, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.mergeItemStack(stackInSlot, 0, slots, false)) {
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

   public boolean canInteractWith(EntityPlayer var1) {
      return this.mobInv.canInteractWith(var1);
   }
}
