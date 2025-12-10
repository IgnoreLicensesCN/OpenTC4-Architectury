package thaumcraft.common.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileDeconstructionTable;

public class ContainerDeconstructionTable extends Container {
   private TileDeconstructionTable table;
   private int lastBreakTime;

   public ContainerDeconstructionTable(InventoryPlayer par1InventoryPlayer, TileDeconstructionTable tileEntity) {
      this.table = tileEntity;
      this.addSlotToContainer(new SlotLimitedHasAspects(tileEntity, 0, 64, 16));

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
      par1ICrafting.sendProgressBarUpdate(this, 0, this.table.breaktime);
   }

   public boolean enchantItem(EntityPlayer p, int button) {
      if (button == 1 && this.table.aspect != null) {
         Thaumcraft.proxy.playerKnowledge.addAspectPool(p.getCommandSenderName(), this.table.aspect, (short)1);
         ResearchManager.scheduleSave(p);
         PacketHandler.INSTANCE.sendTo(new PacketAspectPool(this.table.aspect.getTag(), (short) 1, Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(p.getCommandSenderName(), this.table.aspect)), (EntityPlayerMP)p);
         this.table.aspect = null;
         this.table.getWorldObj().markBlockForUpdate(this.table.xCoord, this.table.yCoord, this.table.zCoord);
      }

      return false;
   }

   public void detectAndSendChanges() {
      super.detectAndSendChanges();

       for (Object crafter : this.crafters) {
           ICrafting icrafting = (ICrafting) crafter;
           if (this.lastBreakTime != this.table.breaktime) {
               icrafting.sendProgressBarUpdate(this, 0, this.table.breaktime);
           }
       }

      this.lastBreakTime = this.table.breaktime;
   }

   @SideOnly(Side.CLIENT)
   public void updateProgressBar(int par1, int par2) {
      if (par1 == 0) {
         this.table.breaktime = par2;
      }

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
            AspectList al = ThaumcraftCraftingManager.getObjectTags(itemstack1);
            al = ThaumcraftCraftingManager.getBonusTags(itemstack1, al);
            if (al != null && al.size() > 0) {
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
