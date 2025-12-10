package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.items.relics.ItemHandMirror;

public class ContainerHandMirror extends Container {
   private World worldObj;
   private int posX;
   private int posY;
   private int posZ;
   public IInventory input = new InventoryHandMirror(this);
   ItemStack mirror = null;
   EntityPlayer player = null;

   public ContainerHandMirror(InventoryPlayer iinventory, World par2World, int par3, int par4, int par5) {
      this.worldObj = par2World;
      this.posX = par3;
      this.posY = par4;
      this.posZ = par5;
      this.player = iinventory.player;
      this.mirror = iinventory.getCurrentItem();
      this.addSlotToContainer(new Slot(this.input, 0, 80, 24));
      this.bindPlayerInventory(iinventory);
      this.onCraftMatrixChanged(this.input);
   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(int i = 0; i < 9; ++i) {
         this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
      }

   }

   public void onCraftMatrixChanged(IInventory par1IInventory) {
      if (this.input.getStackInSlot(0) != null && ItemStack.areItemStacksEqual(this.input.getStackInSlot(0), this.mirror)) {
         this.player.openContainer = this.player.inventoryContainer;
      } else if (!this.worldObj.isRemote && this.input.getStackInSlot(0) != null && this.player != null && ItemHandMirror.transport(this.mirror, this.input.getStackInSlot(0), this.player, this.worldObj)) {
         this.input.setInventorySlotContents(0, null);

          for (Object crafter : this.crafters) {
              ((ICrafting) crafter).sendSlotContents(this, 0, null);
          }
      }

   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
      ItemStack stack = null;
      Slot slotObject = (Slot)this.inventorySlots.get(slot);
      if (slotObject != null && slotObject.getHasStack() && !(slotObject.getStack().getItem() instanceof ItemHandMirror)) {
         ItemStack stackInSlot = slotObject.getStack();
         stack = stackInSlot.copy();
         if (slot == 0) {
            if (!this.mergeItemStack(stackInSlot, 1, this.inventorySlots.size(), true, 64)) {
               return null;
            }
         } else if (!this.mergeItemStack(stackInSlot, 0, 1, false, 64)) {
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
      return true;
   }

   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
      super.onContainerClosed(par1EntityPlayer);
      if (!this.worldObj.isRemote) {
         for(int var2 = 0; var2 < 1; ++var2) {
            ItemStack var3 = this.input.getStackInSlotOnClosing(var2);
            if (var3 != null) {
               par1EntityPlayer.dropPlayerItemWithRandomChoice(var3, false);
            }
         }
      }

   }

   protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4, int limit) {
      boolean var5 = false;
      int var6 = par2;
      if (par4) {
         var6 = par3 - 1;
      }

      if (par1ItemStack.isStackable()) {
         while(par1ItemStack.stackSize > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2)) {
            Slot var7 = (Slot)this.inventorySlots.get(var6);
            ItemStack var8 = var7.getStack();
            if (var8 != null && var8.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == var8.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, var8)) {
               int var9 = var8.stackSize + par1ItemStack.stackSize;
               if (var9 <= Math.min(par1ItemStack.getMaxStackSize(), limit)) {
                  par1ItemStack.stackSize = 0;
                  var8.stackSize = var9;
                  var7.onSlotChanged();
                  var5 = true;
               } else if (var8.stackSize < Math.min(par1ItemStack.getMaxStackSize(), limit)) {
                  par1ItemStack.stackSize -= Math.min(par1ItemStack.getMaxStackSize(), limit) - var8.stackSize;
                  var8.stackSize = Math.min(par1ItemStack.getMaxStackSize(), limit);
                  var7.onSlotChanged();
                  var5 = true;
               }
            }

            if (par4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      if (par1ItemStack.stackSize > 0) {
         if (par4) {
            var6 = par3 - 1;
         } else {
            var6 = par2;
         }

         while(!par4 && var6 < par3 || par4 && var6 >= par2) {
            Slot var7 = (Slot)this.inventorySlots.get(var6);
            ItemStack var8 = var7.getStack();
            if (var8 == null) {
               ItemStack res = par1ItemStack.copy();
               res.stackSize = Math.min(res.stackSize, limit);
               var7.putStack(res);
               var7.onSlotChanged();
               par1ItemStack.stackSize -= res.stackSize;
               var5 = true;
               break;
            }

            if (par4) {
               --var6;
            } else {
               ++var6;
            }
         }
      }

      return var5;
   }
}
