package thaumcraft.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileThaumatoriumTop extends TileThaumcraft implements IAspectContainer, IEssentiaTransport, ISidedInventory {
   public TileThaumatorium thaumatorium = null;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (this.thaumatorium == null) {
         TileEntity tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
         if (tile instanceof TileThaumatorium) {
            this.thaumatorium = (TileThaumatorium)tile;
            this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
         } else {
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 9, 3);
         }
      }

   }

   public int addToContainer(Aspect tt, int am) {
      return this.thaumatorium == null ? am : this.thaumatorium.addToContainer(tt, am);
   }

   public boolean takeFromContainer(Aspect tt, int am) {
      return this.thaumatorium != null && this.thaumatorium.takeFromContainer(tt, am);
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tt, int am) {
      return this.thaumatorium != null && this.thaumatorium.doesContainerContainAmount(tt, am);
   }

   public int containerContains(Aspect tt) {
      return this.thaumatorium == null ? 0 : this.thaumatorium.containerContains(tt);
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }

   public boolean isConnectable(ForgeDirection face) {
      return this.thaumatorium != null && this.thaumatorium.isConnectable(face);
   }

   public boolean canInputFrom(ForgeDirection face) {
      return this.thaumatorium != null && this.thaumatorium.canInputFrom(face);
   }

   public boolean canOutputTo(ForgeDirection face) {
      return false;
   }

   public void setSuction(Aspect aspect, int amount) {
      if (this.thaumatorium != null) {
         this.thaumatorium.setSuction(aspect, amount);
      }
   }

   public Aspect getSuctionType(ForgeDirection loc) {
      return this.thaumatorium == null ? null : this.thaumatorium.getSuctionType(loc);
   }

   public int getSuctionAmount(ForgeDirection loc) {
      return this.thaumatorium == null ? 0 : this.thaumatorium.getSuctionAmount(loc);
   }

   public Aspect getEssentiaType(ForgeDirection loc) {
      return null;
   }

   public int getEssentiaAmount(ForgeDirection loc) {
      return 0;
   }

   public int takeEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return this.thaumatorium == null ? 0 : this.thaumatorium.takeEssentia(aspect, amount, face);
   }

   public int addEssentia(Aspect aspect, int amount, ForgeDirection face) {
      return this.thaumatorium == null ? 0 : this.thaumatorium.addEssentia(aspect, amount, face);
   }

   public int getMinimumSuction() {
      return 0;
   }

   public boolean renderExtendedTube() {
      return false;
   }

   public AspectList getAspects() {
      return this.thaumatorium == null ? null : this.thaumatorium.essentia;
   }

   public void setAspects(AspectList aspects) {
      if (this.thaumatorium != null) {
         this.thaumatorium.setAspects(aspects);
      }
   }

   public int getSizeInventory() {
      return 1;
   }

   public ItemStack getStackInSlot(int par1) {
      return this.thaumatorium == null ? null : this.thaumatorium.getStackInSlot(par1);
   }

   public ItemStack decrStackSize(int par1, int par2) {
      return this.thaumatorium == null ? null : this.thaumatorium.decrStackSize(par1, par2);
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      return this.thaumatorium == null ? null : this.thaumatorium.getStackInSlotOnClosing(par1);
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      if (this.thaumatorium != null) {
         this.thaumatorium.setInventorySlotContents(par1, par2ItemStack);
      }
   }

   public String getInventoryName() {
      return "container.alchemyfurnace";
   }

   public boolean hasCustomInventoryName() {
      return false;
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
      return true;
   }

   public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
      return true;
   }
}
