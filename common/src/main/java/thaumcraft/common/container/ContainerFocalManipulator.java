package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.tiles.TileFocalManipulator;

public class ContainerFocalManipulator extends Container {
   private TileFocalManipulator table;
   private int lastBreakTime;

   public ContainerFocalManipulator(InventoryPlayer par1InventoryPlayer, TileFocalManipulator tileEntity) {
      this.table = tileEntity;
      this.addSlotToContainer(new SlotLimitedByClass(ItemFocusBasic.class, tileEntity, 0, 88, 60));

      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 16 + j * 18, 151 + i * 18));
         }
      }

      for(int var5 = 0; var5 < 9; ++var5) {
         this.addSlotToContainer(new Slot(par1InventoryPlayer, var5, 16 + var5 * 18, 209));
      }

   }

   public boolean enchantItem(EntityPlayer p, int button) {
      if (button >= 0 && !this.table.startCraft(button, p)) {
         this.table.getWorldObj().playSoundEffect(this.table.xCoord, this.table.yCoord, this.table.zCoord, "thaumcraft:craftfail", 0.33F, 1.0F);
      }

      return false;
   }

   public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
      return this.table.isUseableByPlayer(par1EntityPlayer);
   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(par2);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (par2 != 0) {
            if (itemstack1.getItem() instanceof ItemFocusBasic) {
               if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                  return null;
               }
            } else if (par2 >= 1 && par2 < 28) {
               if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
                  return null;
               }
            } else if (par2 >= 28 && par2 < 37 && !this.mergeItemStack(itemstack1, 1, 28, false)) {
               return null;
            }
         } else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
            return null;
         }

         if (itemstack1.stackSize == 0) {
            slot.putStack(null);
         } else {
            slot.onSlotChanged();
         }

         if (itemstack1.stackSize == itemstack.stackSize) {
            return null;
         }

         slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
      }

      return itemstack;
   }
}
