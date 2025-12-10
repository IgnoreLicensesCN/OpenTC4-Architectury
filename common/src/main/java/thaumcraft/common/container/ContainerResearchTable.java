package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.IScribeTools;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.ItemResearchNotes;
import thaumcraft.common.tiles.TileResearchTable;

public class ContainerResearchTable extends Container {
   public TileResearchTable tileEntity;
   String[] aspects;
   EntityPlayer player;

   public ContainerResearchTable(InventoryPlayer iinventory, TileResearchTable iinventory1) {
      this.player = iinventory.player;
      this.tileEntity = iinventory1;
      this.aspects = Aspect.aspects.keySet().toArray(new String[0]);
      this.addSlotToContainer(new SlotLimitedByClass(IScribeTools.class, iinventory1, 0, 14, 10));
      this.addSlotToContainer(new SlotLimitedByClass(ItemResearchNotes.class, iinventory1, 1, 70, 10));
      this.bindPlayerInventory(iinventory);
   }

   protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 48 + j * 18, 175 + i * 18));
         }
      }

      for(int i = 0; i < 9; ++i) {
         this.addSlotToContainer(new Slot(inventoryPlayer, i, 48 + i * 18, 233));
      }

   }

   public boolean enchantItem(EntityPlayer par1EntityPlayer, int button) {
      if (button == 1) {
         return true;
      } else if (button == 5) {
         this.tileEntity.duplicate(par1EntityPlayer);
         return true;
      } else {
         return false;
      }
   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slot) {
      ItemStack stack = null;
      Slot slotObject = (Slot)this.inventorySlots.get(slot);
      if (slotObject != null && slotObject.getHasStack()) {
         ItemStack stackInSlot = slotObject.getStack();
         stack = stackInSlot.copy();
         if (slot < 2) {
            if (!this.mergeItemStack(stackInSlot, 2, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.mergeItemStack(stackInSlot, 0, 2, false)) {
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

   protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
      boolean var5 = false;
      int var6 = par2;
      if (par4) {
         var6 = par3 - 1;
      }

      if (par1ItemStack.isStackable()) {
         while(par1ItemStack.stackSize > 0 && (!par4 && var6 < par3 || par4 && var6 >= par2)) {
            Slot var7 = (Slot)this.inventorySlots.get(var6);
            ItemStack var8 = var7.getStack();
            if (var8 != null && var7.isItemValid(par1ItemStack) && var8.getItem() == par1ItemStack.getItem() && (!par1ItemStack.getHasSubtypes() || par1ItemStack.getItemDamage() == var8.getItemDamage()) && ItemStack.areItemStackTagsEqual(par1ItemStack, var8)) {
               int var9 = var8.stackSize + par1ItemStack.stackSize;
               if (var9 <= par1ItemStack.getMaxStackSize()) {
                  par1ItemStack.stackSize = 0;
                  var8.stackSize = var9;
                  var7.onSlotChanged();
                  var5 = true;
               } else if (var8.stackSize < par1ItemStack.getMaxStackSize()) {
                  par1ItemStack.stackSize -= par1ItemStack.getMaxStackSize() - var8.stackSize;
                  var8.stackSize = par1ItemStack.getMaxStackSize();
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
            if (var8 == null && var7.isItemValid(par1ItemStack)) {
               var7.putStack(par1ItemStack.copy());
               var7.onSlotChanged();
               par1ItemStack.stackSize = 0;
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

   public boolean canInteractWith(EntityPlayer player) {
      return this.tileEntity.isUseableByPlayer(player);
   }
}
