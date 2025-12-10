package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.research.ResearchManager;

public class TileDeconstructionTable extends TileThaumcraft implements ISidedInventory {
   public Aspect aspect;
   public int breaktime;
   private ItemStack[] itemStacks = new ItemStack[1];
   private String customName;
   private static final int[] sides = new int[]{0};

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
      return this.hasCustomInventoryName() ? this.customName : "container.decontable";
   }

   public boolean hasCustomInventoryName() {
      return this.customName != null && !this.customName.isEmpty();
   }

   public void setGuiDisplayName(String par1Str) {
      this.customName = par1Str;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.aspect = Aspect.getAspect(nbttagcompound.getString("Aspect"));
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

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      if (this.aspect != null) {
         nbttagcompound.setString("Aspect", this.aspect.getTag());
      }

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

   public void readFromNBT(NBTTagCompound nbtCompound) {
      super.readFromNBT(nbtCompound);
      if (nbtCompound.hasKey("CustomName")) {
         this.customName = nbtCompound.getString("CustomName");
      }

   }

   public void writeToNBT(NBTTagCompound nbtCompound) {
      super.writeToNBT(nbtCompound);
      if (this.hasCustomInventoryName()) {
         nbtCompound.setString("CustomName", this.customName);
      }

   }

   public int getInventoryStackLimit() {
      return 64;
   }

   @SideOnly(Side.CLIENT)
   public int getBreakTimeScaled(int par1) {
      return this.breaktime * par1 / 40;
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      boolean flag1 = false;
      if (!this.worldObj.isRemote) {
         if (this.breaktime == 0 && this.canBreak()) {
            this.breaktime = 40;
            flag1 = true;
         }

         if (this.breaktime > 0 && this.canBreak()) {
            --this.breaktime;
            if (this.breaktime == 0) {
               this.breaktime = 0;
               this.breakItem();
               flag1 = true;
            }
         } else {
            this.breaktime = 0;
         }
      }

      if (flag1) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         this.markDirty();
      }

   }

   private boolean canBreak() {
      if (this.itemStacks[0] != null && this.aspect == null) {
         AspectList al = ThaumcraftCraftingManager.getObjectTags(this.itemStacks[0]);
         al = ThaumcraftCraftingManager.getBonusTags(this.itemStacks[0], al);
         return al != null && al.size() != 0;
      } else {
         return false;
      }
   }

   public void breakItem() {
      if (this.canBreak()) {
         AspectList al = ThaumcraftCraftingManager.getObjectTags(this.itemStacks[0]);
         al = ThaumcraftCraftingManager.getBonusTags(this.itemStacks[0], al);
         AspectList primals = ResearchManager.reduceToPrimals(al);
         if (this.worldObj.rand.nextInt(80) < primals.visSize()) {
            this.aspect = primals.getAspects()[this.worldObj.rand.nextInt(primals.getAspects().length)];
         }

         --this.itemStacks[0].stackSize;
         if (this.itemStacks[0].stackSize <= 0) {
            this.itemStacks[0] = null;
         }
      }

   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
      AspectList al = ThaumcraftCraftingManager.getObjectTags(par2ItemStack);
      al = ThaumcraftCraftingManager.getBonusTags(par2ItemStack, al);
      return al != null && al.size() > 0;
   }

   public int[] getAccessibleSlotsFromSide(int par1) {
      return par1 != 1 ? sides : new int[0];
   }

   public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
      return par3 != 1 && this.isItemValidForSlot(par1, par2ItemStack);
   }

   public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
      return true;
   }
}
