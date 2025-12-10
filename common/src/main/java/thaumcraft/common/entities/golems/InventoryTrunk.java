package thaumcraft.common.entities.golems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryTrunk implements IInventory {
   public ItemStack[] inventory;
   public EntityTravelingTrunk ent;
   public boolean inventoryChanged;
   public int slotCount;
   public int stacklimit = 64;

   public InventoryTrunk(EntityTravelingTrunk entity, int slots) {
      this.slotCount = slots;
      this.inventory = new ItemStack[36];
      this.inventoryChanged = false;
      this.ent = entity;
   }

   public InventoryTrunk(EntityTravelingTrunk entity, int slots, int lim) {
      this.slotCount = slots;
      this.inventory = new ItemStack[36];
      this.inventoryChanged = false;
      this.stacklimit = lim;
      this.ent = entity;
   }

   public int getInventorySlotContainItem(Item i) {
      for(int j = 0; j < this.inventory.length; ++j) {
         if (this.inventory[j] != null && this.inventory[j].getItem() == i) {
            return j;
         }
      }

      return -1;
   }

   public int getFirstEmptyStack() {
      for(int i = 0; i < this.inventory.length; ++i) {
         if (this.inventory[i] == null) {
            return i;
         }
      }

      return -1;
   }

   public ItemStack decrStackSize(int i, int j) {
      ItemStack[] aitemstack = this.inventory;
      if (aitemstack[i] != null) {
         if (aitemstack[i].stackSize <= j) {
            ItemStack itemstack = aitemstack[i];
            aitemstack[i] = null;
            return itemstack;
         } else {
            ItemStack itemstack1 = aitemstack[i].splitStack(j);
            if (aitemstack[i].stackSize == 0) {
               aitemstack[i] = null;
            }

            return itemstack1;
         }
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int i, ItemStack itemstack) {
      ItemStack[] aitemstack = this.inventory;
      aitemstack[i] = itemstack;
   }

   public NBTTagList writeToNBT(NBTTagList nbttaglist) {
      for(int i = 0; i < this.inventory.length; ++i) {
         if (this.inventory[i] != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte)i);
            this.inventory[i].writeToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
         }
      }

      return nbttaglist;
   }

   public void readFromNBT(NBTTagList nbttaglist) {
      this.inventory = new ItemStack[this.inventory.length];

      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
         int j = nbttagcompound.getByte("Slot") & 255;
         ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
         if (itemstack.getItem() != null && j >= 0 && j < this.inventory.length) {
            this.inventory[j] = itemstack;
         }
      }

   }

   public int getSizeInventory() {
      return this.slotCount;
   }

   public ItemStack getStackInSlot(int i) {
      ItemStack[] aitemstack = this.inventory;
      return aitemstack[i];
   }

   public int getInventoryStackLimit() {
      return this.stacklimit;
   }

   public void dropAllItems() {
      for(int i = 0; i < this.inventory.length; ++i) {
         if (this.inventory[i] != null) {
            this.ent.entityDropItem(this.inventory[i], 0.0F);
            this.inventory[i] = null;
         }
      }

   }

   public boolean isUseableByPlayer(EntityPlayer entityplayer) {
      return false;
   }

   public ItemStack getStackInSlotOnClosing(int var1) {
      return null;
   }

   public boolean isItemValidForSlot(int i, ItemStack itemstack) {
      return true;
   }

   public String getInventoryName() {
      return "Inventory";
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public void markDirty() {
      this.inventoryChanged = true;
   }

   public void openInventory() {
      if (this.ent instanceof EntityTravelingTrunk) {
         this.ent.setOpen(true);
      }

   }

   public void closeInventory() {
      if (this.ent instanceof EntityTravelingTrunk) {
         this.ent.setOpen(false);
      }

   }
}
