package thaumcraft.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGhostSlots extends Container {
   public boolean canInteractWith(EntityPlayer entityplayer) {
      return false;
   }

   public ItemStack slotClick(int slotClicked, int button, int mod, EntityPlayer player) {
      ItemStack itemstack = null;
      InventoryPlayer inventoryplayer = player.inventory;
      if ((mod == 0 || mod == 1) && (button == 0 || button == 1)) {
         if (slotClicked == -999) {
            if (inventoryplayer.getItemStack() != null && slotClicked == -999) {
               if (button == 0) {
                  player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack(), false);
                  inventoryplayer.setItemStack(null);
               }

               if (button == 1) {
                  player.dropPlayerItemWithRandomChoice(inventoryplayer.getItemStack().splitStack(1), false);
                  if (inventoryplayer.getItemStack().stackSize == 0) {
                     inventoryplayer.setItemStack(null);
                  }
               }
            }
         } else if (mod == 1) {
            if (slotClicked < 0) {
               return null;
            }

            Slot slot2 = (Slot)this.inventorySlots.get(slotClicked);
            if (slot2 != null && slot2.getStack() != null && slot2 instanceof SlotGhost) {
               if (button == 0) {
                  slot2.putStack(null);
               } else if (button == 1) {
                  ItemStack slotStack = slot2.getStack();
                  slotStack.stackSize += 16;
                  if (slotStack.stackSize > slot2.getSlotStackLimit()) {
                     slotStack.stackSize = slot2.getSlotStackLimit();
                  }
               }
            }
         } else {
            if (slotClicked < 0) {
               return null;
            }

            Slot slot2 = (Slot)this.inventorySlots.get(slotClicked);
            if (slot2 != null) {
               ItemStack slotStack = slot2.getStack();
               ItemStack playerStack = inventoryplayer.getItemStack();
               if (slotStack != null) {
                  itemstack = slotStack.copy();
               }

               if (slotStack == null) {
                  if (playerStack != null && slot2.isItemValid(playerStack)) {
                     int k1 = button == 0 ? playerStack.stackSize : 1;
                     if (k1 > slot2.getSlotStackLimit()) {
                        k1 = slot2.getSlotStackLimit();
                     }

                     if (playerStack.stackSize >= k1) {
                        if (slot2 instanceof SlotGhost) {
                           ItemStack ic = playerStack.copy();
                           ic.stackSize = k1;
                           slot2.putStack(ic);
                        } else {
                           slot2.putStack(playerStack.splitStack(k1));
                        }
                     }

                     if (!(slot2 instanceof SlotGhost) && playerStack.stackSize == 0) {
                        inventoryplayer.setItemStack(null);
                     }
                  }
               } else if (slot2.canTakeStack(player) || slot2 instanceof SlotGhost) {
                  if (playerStack == null) {
                     if (slot2 instanceof SlotGhost) {
                        int k1 = button == 0 ? 1 : -1;
                        if (slotStack.stackSize - k1 <= slot2.getSlotStackLimit()) {
                           slot2.decrStackSize(k1);
                        }

                        if (slotStack.stackSize == 0) {
                           slot2.putStack(null);
                        }
                     } else {
                        int k1 = button == 0 ? slotStack.stackSize : (slotStack.stackSize + 1) / 2;
                        ItemStack itemstack3 = slot2.decrStackSize(k1);
                        inventoryplayer.setItemStack(itemstack3);
                        if (slotStack.stackSize == 0) {
                           slot2.putStack(null);
                        }

                        slot2.onPickupFromSlot(player, inventoryplayer.getItemStack());
                     }
                  } else if (slot2.isItemValid(playerStack)) {
                     if (slotStack.getItem() == playerStack.getItem() && slotStack.getItemDamage() == playerStack.getItemDamage() && ItemStack.areItemStackTagsEqual(slotStack, playerStack)) {
                        int k1 = button == 0 ? playerStack.stackSize : 1;
                        if (k1 > slot2.getSlotStackLimit() - slotStack.stackSize) {
                           k1 = slot2.getSlotStackLimit() - slotStack.stackSize;
                        }

                        if (k1 > playerStack.getMaxStackSize() - slotStack.stackSize) {
                           k1 = playerStack.getMaxStackSize() - slotStack.stackSize;
                        }

                        if (!(slot2 instanceof SlotGhost)) {
                           playerStack.splitStack(k1);
                           if (playerStack.stackSize == 0) {
                              inventoryplayer.setItemStack(null);
                           }
                        }

                        slotStack.stackSize += k1;
                     } else if (playerStack.stackSize <= slot2.getSlotStackLimit()) {
                        slot2.putStack(playerStack.copy());
                        if (!(slot2 instanceof SlotGhost)) {
                           inventoryplayer.setItemStack(slotStack);
                        }
                     }
                  } else if (slotStack.getItem() == playerStack.getItem() && playerStack.getMaxStackSize() > 1 && (!slotStack.getHasSubtypes() || slotStack.getItemDamage() == playerStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(slotStack, playerStack)) {
                     int k1 = slotStack.stackSize;
                     if (k1 > 0 && k1 + playerStack.stackSize <= playerStack.getMaxStackSize()) {
                        if (!(slot2 instanceof SlotGhost)) {
                           playerStack.stackSize += k1;
                        }

                        slotStack = slot2.decrStackSize(k1);
                        if (slotStack.stackSize == 0) {
                           slot2.putStack(null);
                        }

                        if (!(slot2 instanceof SlotGhost)) {
                           slot2.onPickupFromSlot(player, inventoryplayer.getItemStack());
                        }
                     }
                  }
               }

               slot2.onSlotChanged();
            }
         }
      } else if (mod == 2 && button >= 0 && button < 9) {
         Slot slot2 = (Slot)this.inventorySlots.get(slotClicked);
         if (slot2.canTakeStack(player)) {
            ItemStack slotStack = inventoryplayer.getStackInSlot(button);
            boolean flag = slotStack == null || slot2.inventory == inventoryplayer && slot2.isItemValid(slotStack);
            int k1 = -1;
            if (!flag) {
               k1 = inventoryplayer.getFirstEmptyStack();
               flag |= k1 > -1;
            }

            if (slot2.getHasStack() && flag) {
               ItemStack itemstack3 = slot2.getStack();
               if (!(slot2 instanceof SlotGhost)) {
                  inventoryplayer.setInventorySlotContents(button, itemstack3.copy());
               }

               if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(slotStack)) && slotStack != null) {
                  if (k1 > -1) {
                     if (!(slot2 instanceof SlotGhost)) {
                        inventoryplayer.addItemStackToInventory(slotStack);
                     }

                     slot2.decrStackSize(itemstack3.stackSize);
                     slot2.putStack(null);
                     if (!(slot2 instanceof SlotGhost)) {
                        slot2.onPickupFromSlot(player, itemstack3);
                     }
                  }
               } else {
                  slot2.decrStackSize(itemstack3.stackSize);
                  slot2.putStack(slotStack);
                  if (!(slot2 instanceof SlotGhost)) {
                     slot2.onPickupFromSlot(player, itemstack3);
                  }
               }
            } else if (!slot2.getHasStack() && slotStack != null && slot2.isItemValid(slotStack)) {
               if (!(slot2 instanceof SlotGhost)) {
                  inventoryplayer.setInventorySlotContents(button, null);
               }

               slot2.putStack(slotStack);
            }
         }
      } else if (mod == 3 && player.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotClicked >= 0) {
         Slot slot2 = (Slot)this.inventorySlots.get(slotClicked);
         if (slot2 != null && slot2.getHasStack()) {
            ItemStack slotStack = slot2.getStack().copy();
            slotStack.stackSize = slotStack.getMaxStackSize();
            if (!(slot2 instanceof SlotGhost)) {
               inventoryplayer.setItemStack(slotStack);
            }
         }
      } else if (mod == 3) {
         Slot slot2 = (Slot)this.inventorySlots.get(slotClicked);
         if (slot2 instanceof SlotGhost && button == 3) {
            slot2.putStack(null);
         }
      } else if (mod == 4 && inventoryplayer.getItemStack() == null && slotClicked >= 0) {
         Slot slot2 = (Slot)this.inventorySlots.get(slotClicked);
         if (slot2 != null && slot2.getHasStack() && (slot2.canTakeStack(player) || slot2 instanceof SlotGhost)) {
            ItemStack slotStack = slot2.decrStackSize(button == 0 ? 1 : slot2.getStack().stackSize);
            if (!(slot2 instanceof SlotGhost)) {
               slot2.onPickupFromSlot(player, slotStack);
               player.dropPlayerItemWithRandomChoice(slotStack, false);
            }
         }
      } else if (mod == 6 && slotClicked >= 0) {
         Slot slot2 = (Slot)this.inventorySlots.get(slotClicked);
         ItemStack slotStack = inventoryplayer.getItemStack();
         if (slotStack != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player))) {
            int l = button == 0 ? 0 : this.inventorySlots.size() - 1;
            int k1 = button == 0 ? 1 : -1;

            for(int l1 = 0; l1 < 2; ++l1) {
               for(int i2 = l; i2 >= 0 && i2 < this.inventorySlots.size() && slotStack.stackSize < slotStack.getMaxStackSize(); i2 += k1) {
                  Slot slot3 = (Slot)this.inventorySlots.get(i2);
                  if (!(slot3 instanceof SlotGhost) && !(slot3 instanceof SlotGhostFluid) && slot3.getHasStack() && func_94527_a(slot3, slotStack, true) && slot3.canTakeStack(player) && this.func_94530_a(slotStack, slot3) && (l1 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize())) {
                     int j2 = Math.min(slotStack.getMaxStackSize() - slotStack.stackSize, slot3.getStack().stackSize);
                     ItemStack itemstack5 = slot3.decrStackSize(j2);
                     if (!(slot2 instanceof SlotGhost)) {
                        slotStack.stackSize += j2;
                     }

                     if (itemstack5.stackSize <= 0) {
                        slot3.putStack(null);
                     }

                     if (!(slot2 instanceof SlotGhost)) {
                        slot3.onPickupFromSlot(player, itemstack5);
                     }
                  }
               }
            }
         }

         this.detectAndSendChanges();
      }

      return itemstack;
   }
}
