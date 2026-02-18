package thaumcraft.common.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileAlchemyFurnace;

public class ContainerAlchemyFurnace extends Container {
   private TileAlchemyFurnace furnace;
   private int lastCookTime;
   private int lastBurnTime;
   private int lastItemBurnTime;
   private int lastVis;
   private int lastSmelt;

   public ContainerAlchemyFurnace(InventoryPlayer par1InventoryPlayer, TileAlchemyFurnace tileEntity) {
      this.furnace = tileEntity;
      this.addSlotToContainer(new SlotLimitedHasAspects(tileEntity, 0, 80, 8));
      this.addSlotToContainer(new Slot(tileEntity, 1, 80, 48));

      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(int var5 = 0; var5 < 9; ++var5) {
         this.addSlotToContainer(new Slot(par1InventoryPlayer, var5, 8 + var5 * 18, 142));
      }

   }

   public void addCraftingToCrafters(ICrafting par1ICrafting) {
      super.addCraftingToCrafters(par1ICrafting);
      par1ICrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
      par1ICrafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceFuelRemainingBurnTime);
      par1ICrafting.sendProgressBarUpdate(this, 2, this.furnace.furnaceFuelBurnTotalTime);
      par1ICrafting.sendProgressBarUpdate(this, 3, this.furnace.vis);
      par1ICrafting.sendProgressBarUpdate(this, 4, this.furnace.smeltTime);
   }

   public void detectAndSendChanges() {
      super.detectAndSendChanges();

       for (Object crafter : this.crafters) {
           ICrafting icrafting = (ICrafting) crafter;
           if (this.lastCookTime != this.furnace.furnaceCookTime) {
               icrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
           }

           if (this.lastBurnTime != this.furnace.furnaceFuelRemainingBurnTime) {
               icrafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceFuelRemainingBurnTime);
           }

           if (this.lastItemBurnTime != this.furnace.furnaceFuelBurnTotalTime) {
               icrafting.sendProgressBarUpdate(this, 2, this.furnace.furnaceFuelBurnTotalTime);
           }

           if (this.lastVis != this.furnace.vis) {
               icrafting.sendProgressBarUpdate(this, 3, this.furnace.vis);
           }

           if (this.lastSmelt != this.furnace.smeltTime) {
               icrafting.sendProgressBarUpdate(this, 4, this.furnace.smeltTime);
           }
       }

      this.lastCookTime = this.furnace.furnaceCookTime;
      this.lastBurnTime = this.furnace.furnaceFuelRemainingBurnTime;
      this.lastItemBurnTime = this.furnace.furnaceFuelBurnTotalTime;
      this.lastVis = this.furnace.vis;
      this.lastSmelt = this.furnace.smeltTime;
   }

   @SideOnly(Side.CLIENT)
   public void updateProgressBar(int par1, int par2) {
      if (par1 == 0) {
         this.furnace.furnaceCookTime = par2;
      }

      if (par1 == 1) {
         this.furnace.furnaceFuelRemainingBurnTime = par2;
      }

      if (par1 == 2) {
         this.furnace.furnaceFuelBurnTotalTime = par2;
      }

      if (par1 == 3) {
         this.furnace.vis = par2;
      }

      if (par1 == 4) {
         this.furnace.smeltTime = par2;
      }

   }

   public boolean canInteractWith(Player par1Player) {
      return this.furnace.isUseableByPlayer(par1Player);
   }

   public ItemStack transferStackInSlot(Player par1Player, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(par2);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (par2 != 1 && par2 != 0) {
            AspectList<Aspect>al = ThaumcraftCraftingManager.getObjectTags(itemstack1);
            al = ThaumcraftCraftingManager.getBonusAspects(itemstack1, al);
            if (TileAlchemyFurnace.isItemFuel(itemstack1)) {
               if (!this.mergeItemStack(itemstack1, 1, 2, false) && !this.mergeItemStack(itemstack1, 0, 1, false)) {
                  return null;
               }
            } else if (al.size() > 0) {
               if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                  return null;
               }
            } else if (par2 >= 2 && par2 < 29) {
               if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                  return null;
               }
            } else if (par2 < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
               return null;
            }
         } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
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

         slot.onPickupFromSlot(par1Player, itemstack1);
      }

      return itemstack;
   }
}
