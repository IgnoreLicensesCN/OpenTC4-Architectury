package thaumcraft.common.entities.golems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerTravelingTrunk extends Container {
   private InventoryTrunk mobInv;
   private EntityTravelingTrunk trunk;
   private int numRows;

   public ContainerTravelingTrunk(IInventory iinventory, World par3World, EntityTravelingTrunk trunk) {
      this.trunk = trunk;
      this.mobInv = trunk.inventory;
      this.numRows = trunk.getRows();

      for(int j = 0; j < this.numRows; ++j) {
         for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(this.mobInv, i1 + j * 9, 8 + i1 * 18, 15 + j * 23));
         }
      }

      for(int k = 0; k < 3; ++k) {
         for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlotToContainer(new Slot(iinventory, j1 + k * 9 + 9, 8 + j1 * 18, 118 + k * 18));
         }
      }

      for(int l = 0; l < 9; ++l) {
         this.addSlotToContainer(new Slot(iinventory, l, 8 + l * 18, 176));
      }

      trunk.setOpen(true);
      trunk.worldObj.playSoundAtEntity(trunk, "random.chestopen", 0.5F, trunk.worldObj.rand.nextFloat() * 0.1F + 0.9F);
   }

   public boolean enchantItem(EntityPlayer par1EntityPlayer, int button) {
      if (button == 1) {
         this.trunk.setStay(!this.trunk.getStay());
         return true;
      } else {
         return false;
      }
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

   public boolean canInteractWith(EntityPlayer entityplayer) {
      return true;
   }

   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
      super.onContainerClosed(par1EntityPlayer);
      this.trunk.setOpen(false);
      this.trunk.worldObj.playSoundAtEntity(this.trunk, "random.chestclosed", 0.5F, this.trunk.worldObj.rand.nextFloat() * 0.1F + 0.9F);
   }
}
