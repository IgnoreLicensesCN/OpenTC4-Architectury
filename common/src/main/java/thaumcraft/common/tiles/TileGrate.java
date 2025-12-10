package thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.entities.EntityItemGrate;

public class TileGrate extends TileEntity implements ISidedInventory {
   public int getSizeInventory() {
      return 1;
   }

   public ItemStack getStackInSlot(int par1) {
      return null;
   }

   public ItemStack decrStackSize(int par1, int par2) {
      return null;
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      return null;
   }

   public void setInventorySlotContents(int par1, ItemStack stack) {
      if (!this.worldObj.isRemote) {
         EntityItemGrate ei = new EntityItemGrate(this.worldObj, (double)this.xCoord + (double)0.5F, (double)this.yCoord + 0.6, (double)this.zCoord + (double)0.5F, stack.copy());
         ei.motionY = -0.1;
         ei.motionX = 0.0F;
         ei.motionZ = 0.0F;
         this.worldObj.spawnEntityInWorld(ei);
      }

   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return false;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
      return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 5;
   }

   public int[] getAccessibleSlotsFromSide(int par1) {
      return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 5 && par1 == ForgeDirection.UP.ordinal() ? new int[]{0} : new int[0];
   }

   public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
      return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 5 && par3 == ForgeDirection.UP.ordinal();
   }

   public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
      return false;
   }

   public String getInventoryName() {
      return "thaumcraft.grate";
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public boolean canUpdate() {
      return false;
   }
}
