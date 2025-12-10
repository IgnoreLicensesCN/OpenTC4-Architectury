package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemFocusPouch;

public class ContainerFocusPouch extends Container {
   private World worldObj;
   private int posX;
   private int posY;
   private int posZ;
   private int blockSlot;
   public IInventory input = new InventoryFocusPouch(this);
   ItemStack pouch = null;
   EntityPlayer player = null;

   public ContainerFocusPouch(InventoryPlayer iinventory, World par2World, int par3, int par4, int par5) {
      this.worldObj = par2World;
      this.posX = par3;
      this.posY = par4;
      this.posZ = par5;
      this.player = iinventory.player;
      this.pouch = iinventory.getCurrentItem();
      this.blockSlot = iinventory.currentItem + 45;

      for(int a = 0; a < 18; ++a) {
         this.addSlotToContainer(new SlotLimitedByClass(ItemFocusBasic.class, this.input, a, 37 + a % 6 * 18, 51 + a / 6 * 18));
      }

      this.bindPlayerInventory(iinventory);
      if (!par2World.isRemote) {
         try {
            ((InventoryFocusPouch)this.input).stackList = ((ItemFocusPouch)this.pouch.getItem()).getInventory(this.pouch);
         } catch (Exception ignored) {
         }
      }

      this.onCraftMatrixChanged(this.input);
   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 151 + i * 18));
         }
      }

      for(int i = 0; i < 9; ++i) {
         this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 209));
      }

   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
      if (slot == this.blockSlot) {
         return null;
      } else {
         ItemStack stack = null;
         Slot slotObject = (Slot)this.inventorySlots.get(slot);
         if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot < 18) {
               if (!this.input.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 18, this.inventorySlots.size(), true)) {
                  return null;
               }
            } else if (!this.input.isItemValidForSlot(slot, stackInSlot) || !this.mergeItemStack(stackInSlot, 0, 18, false)) {
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
   }

   public boolean canInteractWith(EntityPlayer var1) {
      return true;
   }

   public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer) {
      return par1 == this.blockSlot ? null : super.slotClick(par1, par2, par3, par4EntityPlayer);
   }

   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
      super.onContainerClosed(par1EntityPlayer);
      if (!this.worldObj.isRemote) {
         ((ItemFocusPouch)this.pouch.getItem()).setInventory(this.pouch, ((InventoryFocusPouch)this.input).stackList);
         if (this.player == null) {
            return;
         }

         if (this.player.getHeldItem() != null && this.player.getHeldItem().isItemEqual(this.pouch)) {
            this.player.setCurrentItemOrArmor(0, this.pouch);
         }

         this.player.inventory.markDirty();
      }

   }
}
