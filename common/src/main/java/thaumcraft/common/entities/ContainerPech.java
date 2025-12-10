package thaumcraft.common.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import thaumcraft.common.container.SlotOutput;
import thaumcraft.common.entities.monster.EntityPech;

public class ContainerPech extends Container {
   private EntityPech pech;
   private InventoryPech inventory;
   private EntityPlayer player;
   private final World theWorld;
   ChestGenHooks chest = ChestGenHooks.getInfo("dungeonChest");

   public ContainerPech(InventoryPlayer par1InventoryPlayer, World par3World, EntityPech par2IMerchant) {
      this.pech = par2IMerchant;
      this.theWorld = par3World;
      this.player = par1InventoryPlayer.player;
      this.inventory = new InventoryPech(par1InventoryPlayer.player, par2IMerchant, this);
      this.pech.trading = true;
      this.addSlotToContainer(new Slot(this.inventory, 0, 36, 29));

      for(int i = 0; i < 2; ++i) {
         for(int j = 0; j < 2; ++j) {
            this.addSlotToContainer(new SlotOutput(this.inventory, 1 + j + i * 2, 106 + 18 * j, 20 + 18 * i));
         }
      }

      for(int var6 = 0; var6 < 3; ++var6) {
         for(int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, j + var6 * 9 + 9, 8 + j * 18, 84 + var6 * 18));
         }
      }

      for(int var7 = 0; var7 < 9; ++var7) {
         this.addSlotToContainer(new Slot(par1InventoryPlayer, var7, 8 + var7 * 18, 142));
      }

   }

   public InventoryPech getMerchantInventory() {
      return this.inventory;
   }

   public void addCraftingToCrafters(ICrafting par1ICrafting) {
      super.addCraftingToCrafters(par1ICrafting);
   }

   public void detectAndSendChanges() {
      super.detectAndSendChanges();
   }

   public boolean enchantItem(EntityPlayer par1EntityPlayer, int par2) {
      if (par2 == 0) {
         this.generateContents();
         return true;
      } else {
         return super.enchantItem(par1EntityPlayer, par2);
      }
   }

   private boolean hasStuffInPack() {
      for(ItemStack stack : this.pech.loot) {
         if (stack != null && stack.stackSize > 0) {
            return true;
         }
      }

      return false;
   }

   private void generateContents() {
      if (!this.theWorld.isRemote && this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(1) == null && this.inventory.getStackInSlot(2) == null && this.inventory.getStackInSlot(3) == null && this.inventory.getStackInSlot(4) == null && this.pech.isValued(this.inventory.getStackInSlot(0))) {
         int value = this.pech.getValue(this.inventory.getStackInSlot(0));
         if (this.theWorld.rand.nextInt(100) <= value / 2) {
            this.pech.setTamed(false);
            this.pech.updateAINextTick = true;
            this.pech.playSound("thaumcraft:pech_trade", 0.4F, 1.0F);
         }

         if (this.theWorld.rand.nextInt(5) == 0) {
            value += this.theWorld.rand.nextInt(3);
         } else if (this.theWorld.rand.nextBoolean()) {
            value -= this.theWorld.rand.nextInt(3);
         }

         EntityPech var10000 = this.pech;
         ArrayList<List> pos = (ArrayList)EntityPech.tradeInventory.get(this.pech.getPechType());

         while(value > 0) {
            int am = Math.min(5, Math.max((value + 1) / 2, this.theWorld.rand.nextInt(value) + 1));
            value -= am;
            if (am == 1 && this.theWorld.rand.nextBoolean() && this.hasStuffInPack()) {
               ArrayList<Integer> loot = new ArrayList<>();

               for(int a = 0; a < this.pech.loot.length; ++a) {
                  if (this.pech.loot[a] != null && this.pech.loot[a].stackSize > 0) {
                     loot.add(a);
                  }
               }

               int r = loot.get(this.theWorld.rand.nextInt(loot.size()));
               ItemStack is = this.pech.loot[r].copy();
               is.stackSize = 1;
               this.mergeItemStack(is, 1, 5, false);
               --this.pech.loot[r].stackSize;
               if (this.pech.loot[r].stackSize <= 0) {
                  this.pech.loot[r] = null;
               }
            } else if (am >= 4 && this.theWorld.rand.nextBoolean()) {
               WeightedRandomChestContent[] contents = this.chest.getItems(this.theWorld.rand);
               WeightedRandomChestContent wc = null;
               int cc = 0;

               do {
                  wc = contents[this.theWorld.rand.nextInt(contents.length)];
                  ++cc;
               } while(cc < 50 && (wc.theItemId == null || wc.itemWeight > 5 || wc.theMaximumChanceToGenerateItem > 1));

               if (wc != null && wc.theItemId != null) {
                  ItemStack is = wc.theItemId.copy();
                  is.onCrafting(this.theWorld, this.player, 0);
                  this.mergeItemStack(is, 1, 5, false);
               } else {
                  value += am;
               }
            } else {
               List it = null;

               do {
                  it = pos.get(this.theWorld.rand.nextInt(pos.size()));
               } while((Integer)it.get(0) != am);

               ItemStack is = ((ItemStack)it.get(1)).copy();
               is.onCrafting(this.theWorld, this.player, 0);
               this.mergeItemStack(is, 1, 5, false);
            }
         }

         this.inventory.decrStackSize(0, 1);
      }

   }

   @SideOnly(Side.CLIENT)
   public void updateProgressBar(int par1, int par2) {
   }

   public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
      return this.pech.isTamed();
   }

   public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(par2);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (par2 == 0) {
            if (!this.mergeItemStack(itemstack1, 5, 41, true)) {
               return null;
            }
         } else if (par2 >= 1 && par2 < 5) {
            if (!this.mergeItemStack(itemstack1, 5, 41, true)) {
               return null;
            }
         } else if (par2 != 0 && par2 >= 5 && par2 < 41 && !this.mergeItemStack(itemstack1, 0, 1, true)) {
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

   public void onContainerClosed(EntityPlayer par1EntityPlayer) {
      super.onContainerClosed(par1EntityPlayer);
      this.pech.trading = false;
      if (!this.theWorld.isRemote) {
         for(int a = 0; a < 5; ++a) {
            ItemStack itemstack = this.inventory.getStackInSlotOnClosing(a);
            if (itemstack != null) {
               EntityItem ei = par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
               if (ei != null) {
                  ei.func_145799_b("PechDrop");
               }
            }
         }
      }

   }

   protected boolean mergeItemStack(ItemStack p_75135_1_, int p_75135_2_, int p_75135_3_, boolean p_75135_4_) {
      boolean flag1 = false;
      int k = p_75135_2_;
      if (p_75135_4_) {
         k = p_75135_3_ - 1;
      }

      if (p_75135_1_.isStackable()) {
         while(p_75135_1_.stackSize > 0 && (!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_)) {
            Slot slot = (Slot)this.inventorySlots.get(k);
            ItemStack itemstack1 = slot.getStack();
            if (itemstack1 != null && itemstack1.getItem() == p_75135_1_.getItem() && (!p_75135_1_.getHasSubtypes() || p_75135_1_.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(p_75135_1_, itemstack1)) {
               int l = itemstack1.stackSize + p_75135_1_.stackSize;
               if (l <= p_75135_1_.getMaxStackSize()) {
                  p_75135_1_.stackSize = 0;
                  itemstack1.stackSize = l;
                  slot.onSlotChanged();
                  flag1 = true;
               } else if (itemstack1.stackSize < p_75135_1_.getMaxStackSize()) {
                  p_75135_1_.stackSize -= p_75135_1_.getMaxStackSize() - itemstack1.stackSize;
                  itemstack1.stackSize = p_75135_1_.getMaxStackSize();
                  slot.onSlotChanged();
                  flag1 = true;
               }
            }

            if (p_75135_4_) {
               --k;
            } else {
               ++k;
            }
         }
      }

      if (p_75135_1_.stackSize > 0) {
         if (p_75135_4_) {
            k = p_75135_3_ - 1;
         } else {
            k = p_75135_2_;
         }

         while(!p_75135_4_ && k < p_75135_3_ || p_75135_4_ && k >= p_75135_2_) {
            Slot slot = (Slot)this.inventorySlots.get(k);
            ItemStack itemstack1 = slot.getStack();
            if (itemstack1 == null) {
               slot.putStack(p_75135_1_.copy());
               slot.onSlotChanged();
               p_75135_1_.stackSize = 0;
               flag1 = true;
               break;
            }

            if (p_75135_4_) {
               --k;
            } else {
               ++k;
            }
         }
      }

      return flag1;
   }
}
