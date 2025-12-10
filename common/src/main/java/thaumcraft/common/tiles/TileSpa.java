package thaumcraft.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.ItemBathSalts;
import thaumcraft.common.lib.utils.BlockUtils;

public class TileSpa extends TileThaumcraft implements ISidedInventory, IFluidHandler {
   private ItemStack[] itemStacks = new ItemStack[1];
   private boolean mix = true;
   private String customName;
   private int counter = 0;
   public FluidTank tank = new FluidTank(5000);

   public void toggleMix() {
      this.mix = !this.mix;
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      this.markDirty();
   }

   public boolean getMix() {
      return this.mix;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.mix = nbttagcompound.getBoolean("mix");
      this.tank.setFluid(FluidStack.loadFluidStackFromNBT(nbttagcompound));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setBoolean("mix", this.mix);
      if (this.tank.getFluid() != null) {
         this.tank.getFluid().writeToNBT(nbttagcompound);
      }

   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
      this.itemStacks = new ItemStack[this.getSizeInventory()];

      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
         byte b0 = nbttagcompound1.getByte("Slot");
         if (b0 >= 0 && b0 < this.itemStacks.length) {
            this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
         }
      }

   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.itemStacks.length; ++i) {
         if (this.itemStacks[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)i);
            this.itemStacks[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
         }
      }

      nbttagcompound.setTag("Items", nbttaglist);
   }

   public int getSizeInventory() {
      return 1;
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
          return itemstack;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.itemStacks[par1] != null) {
         ItemStack itemstack = this.itemStacks[par1];
         this.itemStacks[par1] = null;
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
      return par2ItemStack != null && par2ItemStack.getItem() instanceof ItemBathSalts;
   }

   public int[] getAccessibleSlotsFromSide(int par1) {
      return par1 != 1 ? new int[]{0} : new int[0];
   }

   public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
      return par3 != 1;
   }

   public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
      return par3 != 1;
   }

   public String getInventoryName() {
      return "thaumcraft.spa";
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (!this.worldObj.isRemote && this.counter++ % 40 == 0 && !this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && this.hasIngredients()) {
         Block b = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord);
         int m = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord);
         Block tb = null;
         if (this.mix) {
            tb = ConfigBlocks.blockFluidPure;
         } else {
            tb = this.tank.getFluid().getFluid().getBlock();
         }

         if (b == tb && m == 0) {
            for(int xx = -2; xx <= 2; ++xx) {
               for(int zz = -2; zz <= 2; ++zz) {
                  if (this.isValidLocation(this.xCoord + xx, this.yCoord + 1, this.zCoord + zz, true, tb)) {
                     this.consumeIngredients();
                     this.worldObj.setBlock(this.xCoord + xx, this.yCoord + 1, this.zCoord + zz, tb);
                     this.checkQuanta(this.xCoord + xx, this.yCoord + 1, this.zCoord + zz);
                     return;
                  }
               }
            }
         } else if (this.isValidLocation(this.xCoord, this.yCoord + 1, this.zCoord, false, tb)) {
            this.consumeIngredients();
            this.worldObj.setBlock(this.xCoord, this.yCoord + 1, this.zCoord, tb);
            this.checkQuanta(this.xCoord, this.yCoord + 1, this.zCoord);
         }
      }

   }

   private void checkQuanta(int i, int j, int k) {
      Block b = this.worldObj.getBlock(i, j, k);
      if (b instanceof BlockFluidBase) {
         float p = ((BlockFluidBase)b).getQuantaPercentage(this.worldObj, i, j, k);
         if (p < 1.0F) {
            int md = (int)(1.0F / p) - 1;
            if (md >= 0 && md < 16) {
               this.worldObj.setBlockMetadataWithNotify(i, j, k, md, 3);
            }
         }
      }

   }

   private boolean hasIngredients() {
      if (this.mix) {
         if (this.tank.getInfo().fluid == null || !this.tank.getInfo().fluid.containsFluid(new FluidStack(FluidRegistry.WATER, 1000))) {
            return false;
         }

          return this.itemStacks[0] != null && this.itemStacks[0].getItem() instanceof ItemBathSalts;
      } else return this.tank.getInfo().fluid != null && this.tank.getFluid().getFluid().canBePlacedInWorld() && this.tank.getFluidAmount() >= 1000;
   }

   private void consumeIngredients() {
      if (this.mix) {
         this.decrStackSize(0, 1);
      }

      this.drain(ForgeDirection.UNKNOWN, 1000, true);
   }

   private boolean isValidLocation(int x, int y, int z, boolean mustBeAdjacent, Block target) {
      if ((target == Blocks.water || target == Blocks.flowing_water) && this.worldObj.provider.isHellWorld) {
         return false;
      } else {
         Block b = this.worldObj.getBlock(x, y, z);
         Block bb = this.worldObj.getBlock(x, y - 1, z);
         int m = this.worldObj.getBlockMetadata(x, y, z);
         if (bb.isSideSolid(this.worldObj, x, y - 1, z, ForgeDirection.UP) && b.isReplaceable(this.worldObj, x, y, z) && (b != target || m != 0)) {
            return !mustBeAdjacent || BlockUtils.isBlockTouching(this.worldObj, x, y, z, target, 0);
         } else {
            return false;
         }
      }
   }

   public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
      int df = this.tank.fill(resource, doFill);
      if (df > 0 && doFill) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
      }

      return df;
   }

   public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
      return resource != null && resource.isFluidEqual(this.tank.getFluid()) ? this.tank.drain(resource.amount, doDrain) : null;
   }

   public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
      FluidStack fs = this.tank.drain(maxDrain, doDrain);
      if (fs != null && doDrain) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
      }

      return fs;
   }

   public boolean canFill(ForgeDirection from, Fluid fluid) {
      return from != ForgeDirection.UP;
   }

   public boolean canDrain(ForgeDirection from, Fluid fluid) {
      return from != ForgeDirection.UP;
   }

   public FluidTankInfo[] getTankInfo(ForgeDirection from) {
      return new FluidTankInfo[]{this.tank.getInfo()};
   }
}
