package thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import thaumcraft.api.TileThaumcraft;

public class TileThaumcraftInventory extends TileThaumcraft implements ISidedInventory {
   protected ItemStack[] itemStacks = new ItemStack[1];
   protected String customName;
   protected int[] syncedSlots = new int[0];

   public int getSizeInventory() {
      return this.itemStacks.length;
   }

   public ItemStack getStackInSlot(int par1) {
      return this.itemStacks[par1];
   }

   public ItemStack decrStackSize(int par1, int par2) {
      if (this.itemStacks[par1] != null) {
          ItemStack itemstack;
          if (this.itemStacks[par1].stackSize <= par2) {
              itemstack = this.itemStacks[par1];
            this.itemStacks[par1] = null;
          } else {
              itemstack = this.itemStacks[par1].splitStack(par2);
            if (this.itemStacks[par1].stackSize == 0) {
               this.itemStacks[par1] = null;
            }

          }
          this.markDirty();
          return itemstack;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.itemStacks[par1] != null) {
         ItemStack itemstack = this.itemStacks[par1];
         this.itemStacks[par1] = null;
         this.markDirty();
         return itemstack;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.itemStacks[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }

      this.markDirty();
   }

   public String getInventoryName() {
      return this.hasCustomInventoryName() ? this.customName : "container.thaumcraft";
   }

   public boolean hasCustomInventoryName() {
      return this.customName != null && !this.customName.isEmpty();
   }

   public void setGuiDisplayName(String par1Str) {
      this.customName = par1Str;
   }

   private boolean isSyncedSlot(int slot) {
      for(int s : this.syncedSlots) {
         if (s == slot) {
            return true;
         }
      }

      return false;
   }

   public void readCustomNBT(NBTTagCompound nbtCompound) {
      NBTTagList nbttaglist = nbtCompound.getTagList("ItemsSynced", 10);
      this.itemStacks = new ItemStack[this.getSizeInventory()];

      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
         if (this.isSyncedSlot(i)) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");
            if (b0 >= 0 && b0 < this.itemStacks.length) {
               this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
         }
      }

   }

   public void writeCustomNBT(NBTTagCompound nbtCompound) {
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.itemStacks.length; ++i) {
         if (this.itemStacks[i] != null && this.isSyncedSlot(i)) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)i);
            this.itemStacks[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
         }
      }

      nbtCompound.setTag("ItemsSynced", nbttaglist);
   }

   public void readFromNBT(NBTTagCompound nbtCompound) {
      super.readFromNBT(nbtCompound);
      if (nbtCompound.hasKey("CustomName")) {
         this.customName = nbtCompound.getString("CustomName");
      }

      NBTTagList nbttaglist = nbtCompound.getTagList("Items", 10);

      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
         if (!this.isSyncedSlot(i)) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");
            if (b0 >= 0 && b0 < this.itemStacks.length) {
               this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
         }
      }

   }

   public void writeToNBT(NBTTagCompound nbtCompound) {
      super.writeToNBT(nbtCompound);
      if (this.hasCustomInventoryName()) {
         nbtCompound.setString("CustomName", this.customName);
      }

      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.itemStacks.length; ++i) {
         if (this.itemStacks[i] != null && !this.isSyncedSlot(i)) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)i);
            this.itemStacks[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
         }
      }

      nbtCompound.setTag("Items", nbttaglist);
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
      return true;
   }

   public int[] getAccessibleSlotsFromSide(int par1) {
      return new int[]{0};
   }

   public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
      return this.isItemValidForSlot(par1, par2ItemStack);
   }

   public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
      return true;
   }
}
