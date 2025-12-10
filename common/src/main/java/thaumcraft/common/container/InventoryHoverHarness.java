package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.blocks.ItemJarFilled;

public class InventoryHoverHarness implements IInventory {
   private ItemStack[] stackList = new ItemStack[1];
   private Container eventHandler;

   public InventoryHoverHarness(Container par1Container) {
      this.eventHandler = par1Container;
   }

   public int getSizeInventory() {
      return this.stackList.length;
   }

   public ItemStack getStackInSlot(int par1) {
      return par1 >= this.getSizeInventory() ? null : this.stackList[par1];
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.stackList[par1] != null) {
         ItemStack var2 = this.stackList[par1];
         this.stackList[par1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public ItemStack decrStackSize(int par1, int par2) {
      if (this.stackList[par1] != null) {
          ItemStack var3;
          if (this.stackList[par1].stackSize <= par2) {
              var3 = this.stackList[par1];
            this.stackList[par1] = null;
          } else {
              var3 = this.stackList[par1].splitStack(par2);
            if (this.stackList[par1].stackSize == 0) {
               this.stackList[par1] = null;
            }

          }
          this.eventHandler.onCraftMatrixChanged(this);
          return var3;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.stackList[par1] = par2ItemStack;
      this.eventHandler.onCraftMatrixChanged(this);
   }

   public int getInventoryStackLimit() {
      return 1;
   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return true;
   }

   public boolean isItemValidForSlot(int i, ItemStack jar) {
      if (jar != null && jar.getItem() instanceof ItemJarFilled && jar.hasTagCompound()) {
         AspectList aspects = ((ItemJarFilled)jar.getItem()).getAspects(jar);
          return aspects != null && aspects.size() > 0 && aspects.getAmount(Aspect.ENERGY) > 0;
      }

      return false;
   }

   public String getInventoryName() {
      return "container.hoverharness";
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public void markDirty() {
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }
}
