package thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import static thaumcraft.common.config.ConfigBlocks.blockChestHungry;

public class TileChestHungry extends TileEntity implements IInventory {
   private ItemStack[] chestContents = new ItemStack[36];
   public float lidAngle;
   public float prevLidAngle;
   public int numUsingPlayers;
   private int ticksSinceSync;

   public int getSizeInventory() {
      return 27;
   }

   public ItemStack getStackInSlot(int par1) {
      return this.chestContents[par1];
   }

   public ItemStack decrStackSize(int itemIndexInChest, int extractAtMostCount) {
      ItemStack result = this.chestContents[itemIndexInChest];
      if (result != null) {
         //directly output if not greater than extractAtMostCount
         if (result.stackSize <= extractAtMostCount){
            this.chestContents[itemIndexInChest] = null;
            this.markDirty();
            return result.stackSize == 0 ? null : result;
         }

         //tc4 vanilla
         result = result.splitStack(extractAtMostCount);
         if (result.stackSize == 0) {
            this.chestContents[itemIndexInChest] = null;
         }
         this.markDirty();
         return result;
      } else {
         return null;
      }

   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.chestContents[par1] != null) {
         ItemStack var2 = this.chestContents[par1];
         this.chestContents[par1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.chestContents[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }

      this.markDirty();
   }

   public String getInventoryName() {
      return blockChestHungry.getLocalizedName();
   }

   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
      this.chestContents = new ItemStack[this.getSizeInventory()];

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         int var5 = var4.getByte("Slot") & 255;
         if (var5 < this.chestContents.length) {
            this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }

   }

   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeToNBT(par1NBTTagCompound);
      NBTTagList var2 = new NBTTagList();

      for(int var3 = 0; var3 < this.chestContents.length; ++var3) {
         if (this.chestContents[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.chestContents[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      par1NBTTagCompound.setTag("Items", var2);
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
   }

   public void updateContainingBlockInfo() {
      super.updateContainingBlockInfo();
   }

   public void updateEntity() {
      super.updateEntity();
      if (++this.ticksSinceSync % 20 * 4 == 0) {
      }

      this.prevLidAngle = this.lidAngle;
      float var1 = 0.1F;
      if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F) {
         double var2 = (double)this.xCoord + (double)0.5F;
         double var4 = (double)this.zCoord + (double)0.5F;
         this.worldObj.playSoundEffect(var2, (double)this.yCoord + (double)0.5F, var4, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
      }

      if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F) {
         float var8 = this.lidAngle;
         if (this.numUsingPlayers > 0) {
            this.lidAngle += var1;
         } else {
            this.lidAngle -= var1;
         }

         if (this.lidAngle > 1.0F) {
            this.lidAngle = 1.0F;
         }

         float var3 = 0.5F;
         if (this.lidAngle < var3 && var8 >= var3) {
            double var4 = (double)this.xCoord + (double)0.5F;
            double var6 = (double)this.zCoord + (double)0.5F;
            this.worldObj.playSoundEffect(var4, (double)this.yCoord + (double)0.5F, var6, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
         }

         if (this.lidAngle < 0.0F) {
            this.lidAngle = 0.0F;
         }
      }

   }

   public boolean receiveClientEvent(int par1, int par2) {
      if (par1 == 1) {
         this.numUsingPlayers = par2;
         return true;
      } else if (par1 == 2) {
         if (this.lidAngle < (float)par2 / 10.0F) {
            this.lidAngle = (float)par2 / 10.0F;
         }

         return true;
      } else {
         return this.tileEntityInvalid;
      }
   }

   public void openInventory() {
      ++this.numUsingPlayers;
      this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, blockChestHungry, 1, this.numUsingPlayers);
   }

   public void closeInventory() {
      --this.numUsingPlayers;
      this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, blockChestHungry, 1, this.numUsingPlayers);
   }

   public void invalidate() {
      this.updateContainingBlockInfo();
      super.invalidate();
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public boolean isItemValidForSlot(int i, ItemStack itemstack) {
      return true;
   }
}
