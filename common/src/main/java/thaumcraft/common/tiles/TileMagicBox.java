package thaumcraft.common.tiles;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.core.Direction;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.common.container.ContainerMagicBox;

import java.util.ArrayList;

public class TileMagicBox extends TileThaumcraft implements IInventory {
   ArrayList boxContents = new ArrayList<>();
   WorldCoordinates master = null;
   byte sorting = -1;
   short linkedBoxes = -1;
   public static ContainerMagicBox tc;

   public int getSizeInventory() {
      return 27 * (this.getInventory().linkedBoxes + 1);
   }

   private ArrayList getContents() {
      return this.master != null ? this.getInventory().boxContents : this.boxContents;
   }

   private TileMagicBox getInventory() {
      TileEntity tile = null;
      if (this.master != null) {
         tile = this.level().getTileEntity(this.master.x, this.master.y, this.master.z);
      }

      return tile instanceof TileMagicBox ? (TileMagicBox)tile : this;
   }

   public ItemStack getStackInSlot(int par1) {
      return par1 >= this.getContents().size() ? null : (ItemStack)this.getContents().get(par1);
   }

   public ItemStack decrStackSize(int par1, int par2) {
      if (par1 < this.getContents().size() && this.getContents().get(par1) != null) {
         if (((ItemStack)this.getContents().get(par1)).stackSize <= par2) {
            ItemStack var3 = (ItemStack)this.getContents().get(par1);
            this.getContents().remove(par1);
            this.getInventory().markDirty();
            return var3;
         } else {
            ItemStack var3 = ((ItemStack)this.getContents().get(par1)).splitStack(par2);
            if (((ItemStack)this.getContents().get(par1)).stackSize == 0) {
               this.getContents().remove(par1);
            }

            this.getInventory().markDirty();
            return var3;
         }
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if (par1 < this.getContents().size() && this.getContents().get(par1) != null) {
         ItemStack var2 = (ItemStack)this.getContents().get(par1);
         this.getContents().remove(par1);
         return var2;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      if (par1 >= this.getContents().size() && par2ItemStack != null && par2ItemStack.stackSize > 0) {
         this.getContents().add(par2ItemStack);
      } else if (par2ItemStack != null && par2ItemStack.stackSize > 0) {
         this.getContents().set(par1, par2ItemStack);
      } else if (par1 < this.getContents().size()) {
         this.getContents().remove(par1);
      }

      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }

      this.getInventory().markDirty();
   }

   public void markDirty() {
      super.markDirty();
      this.sort();
   }

   public String getInventoryName() {
      return "Magic Box";
   }

   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      NBTTagList var2 = par1NBTTagCompound.getTagList("ThaumcraftItems", 10);
      this.boxContents = new ArrayList<>();

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         this.boxContents.add(ItemStack.loadItemStackFromNBT(var4));
      }

      this.sort();
   }

   public void readCustomNBT(NBTTagCompound par1NBTTagCompound) {
      this.sorting = par1NBTTagCompound.getByte("sort");
      this.master = null;
      if (par1NBTTagCompound.hasKey("w_x")) {
         this.master = new WorldCoordinates();
         this.master.readNBT(par1NBTTagCompound);
      }

   }

   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeToNBT(par1NBTTagCompound);
      NBTTagList var2 = new NBTTagList();

       for (Object boxContent : this.boxContents) {
           if (boxContent != null) {
               NBTTagCompound var4 = new NBTTagCompound();
               ((ItemStack) boxContent).writeToNBT(var4);
               var2.appendTag(var4);
           }
       }

      par1NBTTagCompound.setTag("ThaumcraftItems", var2);
   }

   public void writeCustomNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setByte("sort", this.sorting);
      if (this.master != null) {
         this.master.writeNBT(par1NBTTagCompound);
      }

   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public boolean isUseableByPlayer(Player par1Player) {
      return this.level().getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1Player.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.getInventory() == this && this.linkedBoxes < 0) {
         this.refreshLinks();
      }

   }

   public boolean receiveClientEvent(int par1, int par2) {
      if (par1 == 1) {
         return true;
      } else {
         return par1 == 2 || this.tileEntityInvalid;
      }
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public void invalidate() {
      this.updateContainingBlockInfo();
      super.invalidate();
   }

   public boolean hasCustomInventoryName() {
      return false;
   }

   public boolean isItemValidForSlot(int i, ItemStack itemstack) {
      return true;
   }

   public void sort() {
      if (this.getLevel() != null && this.sorting >= 0) {
         boolean done = false;

         while(!done) {
            done = true;

            for(int i = 0; i < this.getContents().size() - 1; ++i) {
               done = this.swopSlots(i, i + 1);
               if (((ItemStack)this.getContents().get(i)).stackSize < ((ItemStack)this.getContents().get(i)).getMaxStackSize() && ((ItemStack)this.getContents().get(i)).isItemEqual((ItemStack)this.getContents().get(i + 1)) && ItemStack.areItemStackTagsEqual((ItemStack)this.getContents().get(i), (ItemStack)this.getContents().get(i + 1))) {
                  ItemStack is1 = ((ItemStack)this.getContents().get(i)).copy();
                  ItemStack is2 = ((ItemStack)this.getContents().get(i + 1)).copy();
                  int c = Math.min(is1.getMaxStackSize() - is1.stackSize, is2.stackSize);
                  is1.stackSize += c;
                  is2.stackSize -= c;
                  this.getContents().set(i, is1);
                  done = false;
                  if (is2.stackSize <= 0) {
                     this.getContents().remove(i + 1);
                     break;
                  }

                  this.getContents().set(i + 1, is2);
               }
            }
         }

      }
   }

   private boolean swopSlots(int i, int j) {
      if (this.sorting != 0 && this.sorting != 1) {
         if (this.sorting == 2 && ((ItemStack)this.getContents().get(i)).getDisplayName() != null && ((ItemStack)this.getContents().get(j)).getDisplayName() != null) {
            String s1 = "";
            String s2 = "";
            if (GameRegistry.findUniqueIdentifierFor(((ItemStack)this.getContents().get(i)).getItem()) != null) {
               s1 = s1 + GameRegistry.findUniqueIdentifierFor(((ItemStack)this.getContents().get(i)).getItem()).modId;
            } else if (GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(((ItemStack)this.getContents().get(i)).getItem())) != null) {
               s1 = s1 + GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(((ItemStack)this.getContents().get(i)).getItem())).modId;
            }

            if (GameRegistry.findUniqueIdentifierFor(((ItemStack)this.getContents().get(j)).getItem()) != null) {
               s1 = s1 + GameRegistry.findUniqueIdentifierFor(((ItemStack)this.getContents().get(j)).getItem()).modId;
            } else if (GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(((ItemStack)this.getContents().get(j)).getItem())) != null) {
               s1 = s1 + GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(((ItemStack)this.getContents().get(j)).getItem())).modId;
            }

            s1 = s1 + ((ItemStack)this.getContents().get(i)).getDisplayName();
            s2 = s2 + ((ItemStack)this.getContents().get(j)).getDisplayName();
            if (((ItemStack)this.getContents().get(i)).hasTagCompound()) {
               s1 = s1 + ((ItemStack)this.getContents().get(i)).stackTagCompound.hashCode();
            }

            if (((ItemStack)this.getContents().get(j)).hasTagCompound()) {
               s2 = s2 + ((ItemStack)this.getContents().get(j)).stackTagCompound.hashCode();
            }

            int r = s1.compareToIgnoreCase(s2);
            if (r > 0 && this.sorting == 2) {
               ItemStack is1 = ((ItemStack)this.getContents().get(i)).copy();
               ItemStack is2 = ((ItemStack)this.getContents().get(j)).copy();
               this.getContents().set(i, is2);
               this.getContents().set(j, is1);
               return false;
            }
         }
      } else if (((ItemStack)this.getContents().get(i)).getDisplayName() != null && ((ItemStack)this.getContents().get(j)).getDisplayName() != null) {
         String s1 = "";
         String s2 = "";
         s1 = s1 + ((ItemStack)this.getContents().get(i)).getDisplayName();
         s2 = s2 + ((ItemStack)this.getContents().get(j)).getDisplayName();
         if (((ItemStack)this.getContents().get(i)).hasTagCompound()) {
            s1 = s1 + ((ItemStack)this.getContents().get(i)).stackTagCompound.hashCode();
         }

         if (((ItemStack)this.getContents().get(j)).hasTagCompound()) {
            s2 = s2 + ((ItemStack)this.getContents().get(j)).stackTagCompound.hashCode();
         }

         int r = s1.compareToIgnoreCase(s2);
         if (r > 0 && this.sorting == 0 || r < 0 && this.sorting == 1) {
            ItemStack is1 = ((ItemStack)this.getContents().get(i)).copy();
            ItemStack is2 = ((ItemStack)this.getContents().get(j)).copy();
            this.getContents().set(i, is2);
            this.getContents().set(j, is1);
            return false;
         }
      }

      return true;
   }

   public void refreshLinks() {
      if (this.getInventory() == this) {
         this.linkedBoxes = 0;
         ArrayList<WorldCoordinates> list = new ArrayList<>();
         this.findBoxes(this.xCoord, this.yCoord, this.zCoord, list);
         this.linkedBoxes = (short)list.size();
      }
   }

   private void findBoxes(int x, int y, int z, ArrayList list) {
      if (list.size() < 1024) {
         for(int a = 0; a < 6; ++a) {
            Direction dir = Direction.getOrientation(a);
            TileEntity tile = this.level().getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
            if (tile instanceof TileMagicBox) {
               WorldCoordinates wc = new WorldCoordinates(tile);
               if (!list.contains(wc)) {
                  list.add(wc);
                  this.findBoxes(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, list);
               }
            }
         }

      }
   }
}
