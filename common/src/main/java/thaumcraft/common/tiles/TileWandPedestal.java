package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.nodes.INode;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.ResearchManager;

public class TileWandPedestal extends TileThaumcraft implements ISidedInventory, IAspectContainer {
   private static final int[] slots = new int[]{0};
   private ItemStack[] inventory = new ItemStack[1];
   private String customName;
   int counter = 0;
   boolean somethingChanged = false;
   public boolean draining = false;
   public int drainX = 0;
   public int drainY = 0;
   public int drainZ = 0;
   public int drainColor = 0;
   ArrayList<ChunkCoordinates> nodes = null;

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(2.0F, 2.0F, 2.0F);
   }

   public int getSizeInventory() {
      return 1;
   }

   public ItemStack getStackInSlot(int par1) {
      return this.inventory[par1];
   }

   public ItemStack decrStackSize(int par1, int par2) {
      if (this.inventory[par1] != null) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
          ItemStack itemstack;
          if (this.inventory[par1].stackSize <= par2) {
              itemstack = this.inventory[par1];
            this.inventory[par1] = null;
          } else {
              itemstack = this.inventory[par1].splitStack(par2);
            if (this.inventory[par1].stackSize == 0) {
               this.inventory[par1] = null;
            }

          }
          this.markDirty();
          return itemstack;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.inventory[par1] != null) {
         ItemStack itemstack = this.inventory[par1];
         this.inventory[par1] = null;
         this.markDirty();
         return itemstack;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.inventory[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }

      this.markDirty();
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public String getInventoryName() {
      return this.hasCustomInventoryName() ? this.customName : "container.wandpedestal";
   }

   public boolean hasCustomInventoryName() {
      return this.customName != null && !this.customName.isEmpty();
   }

   public void setGuiDisplayName(String par1Str) {
      this.customName = par1Str;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
      this.inventory = new ItemStack[this.getSizeInventory()];

      for(int i = 0; i < nbttaglist.tagCount(); ++i) {
         NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
         byte b0 = nbttagcompound1.getByte("Slot");
         if (b0 >= 0 && b0 < this.inventory.length) {
            this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
         }
      }

   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      NBTTagList nbttaglist = new NBTTagList();

      for(int i = 0; i < this.inventory.length; ++i) {
         if (this.inventory[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)i);
            this.inventory[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
         }
      }

      nbttagcompound.setTag("Items", nbttaglist);
   }

   public void readFromNBT(NBTTagCompound nbtCompound) {
      super.readFromNBT(nbtCompound);
      if (nbtCompound.hasKey("CustomName")) {
         this.customName = nbtCompound.getString("CustomName");
      }

   }

   public void writeToNBT(NBTTagCompound nbtCompound) {
      super.writeToNBT(nbtCompound);
      if (this.hasCustomInventoryName()) {
         nbtCompound.setString("CustomName", this.customName);
      }

   }

   public int getInventoryStackLimit() {
      return 1;
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.worldObj != null && this.worldObj.isRemote) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }

   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.nodes == null) {
         this.findNodes();
      }

      ++this.counter;
      boolean recalc = false;
      if (this.counter % 20 == 0 && this.somethingChanged && this.nodes != null && !this.nodes.isEmpty() && this.getStackInSlot(0) != null) {
         this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
         this.somethingChanged = false;
      }

      if (this.counter % 5 == 0 && this.nodes != null && !this.nodes.isEmpty() && this.getStackInSlot(0) != null) {
         boolean hasThingy = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) == ConfigBlocks.blockStoneDevice && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord + 1, this.zCoord) == 8;

          if (this.getStackInSlot(0).getItem() instanceof ItemWandCasting) {
            ItemWandCasting wand = (ItemWandCasting)this.getStackInSlot(0).getItem();
            int min = 1;
            if (wand.getCap(this.getStackInSlot(0)).getTag().equals("iron") || wand.getRod(this.getStackInSlot(0)).getTag().equals("wood")) {
               min = 0;
            }

            AspectList as = wand.getAspectsWithRoom(this.getStackInSlot(0));
            this.draining = false;
            if (as != null && as.size() > 0) {
               label152:
               for(ChunkCoordinates co : this.nodes) {
                  TileEntity te = this.worldObj.getTileEntity(co.posX, co.posY, co.posZ);
                  if (te instanceof INode && !(te instanceof TileJarNode)) {
                     INode node = (INode)te;

                     for(Aspect aspect : as.getAspects()) {
                        if (node.getAspects().getAmount(aspect) > min) {
                           wand.addVis(this.getStackInSlot(0), aspect, 1, true);
                           node.takeFromContainer(aspect, 1);
                           this.somethingChanged = true;
                           this.draining = true;
                           if (this.worldObj.isRemote) {
                              this.drainX = co.posX;
                              this.drainY = co.posY;
                              this.drainZ = co.posZ;
                              this.drainColor = aspect.getColor();
                           }
                           break label152;
                        }
                     }

                     if (hasThingy) {
                        for(Aspect aspect : node.getAspects().getAspects()) {
                           if (aspect != null && !aspect.isPrimal()) {
                              AspectList primals = ResearchManager.reduceToPrimals((new AspectList()).add(aspect, 1));

                              for(Aspect aspect2 : as.getAspects()) {
                                 if (primals.getAmount(aspect2) > 0 && node.getAspects().getAmount(aspect) > min) {
                                    wand.addVis(this.getStackInSlot(0), aspect2, 1, true);
                                    node.takeFromContainer(aspect, 1);
                                    this.somethingChanged = true;
                                    this.draining = true;
                                    if (this.worldObj.isRemote) {
                                       this.drainX = co.posX;
                                       this.drainY = co.posY;
                                       this.drainZ = co.posZ;
                                       this.drainColor = aspect.getColor();
                                    }
                                    break label152;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (!this.draining) {
                  recalc = true;
               }
            }
         } else if (this.getStackInSlot(0).getItem() instanceof ItemAmuletVis) {
            ItemAmuletVis amulet = (ItemAmuletVis)this.getStackInSlot(0).getItem();
            int min = 1;
            AspectList as = amulet.getAspectsWithRoom(this.getStackInSlot(0));
            this.draining = false;
            if (as != null && as.size() > 0) {
               label207:
               for(ChunkCoordinates co : this.nodes) {
                  TileEntity te = this.worldObj.getTileEntity(co.posX, co.posY, co.posZ);
                  if (te instanceof INode && !(te instanceof TileJarNode)) {
                     INode node = (INode)te;

                     for(Aspect aspect : as.getAspects()) {
                        if (node.getAspects().getAmount(aspect) > min) {
                           amulet.addVis(this.getStackInSlot(0), aspect, 1, true);
                           node.takeFromContainer(aspect, 1);
                           this.draining = true;
                           if (this.worldObj.isRemote) {
                              this.drainX = co.posX;
                              this.drainY = co.posY;
                              this.drainZ = co.posZ;
                              this.drainColor = aspect.getColor();
                           }
                           break label207;
                        }
                     }

                     if (hasThingy) {
                        for(Aspect aspect : node.getAspects().getAspects()) {
                           if (aspect != null && !aspect.isPrimal()) {
                              AspectList primals = ResearchManager.reduceToPrimals((new AspectList()).add(aspect, 1));

                              for(Aspect aspect2 : as.getAspects()) {
                                 if (primals.getAmount(aspect2) > 0 && node.getAspects().getAmount(aspect) > min) {
                                    amulet.addVis(this.getStackInSlot(0), aspect2, 1, true);
                                    node.takeFromContainer(aspect, 1);
                                    this.draining = true;
                                    if (this.worldObj.isRemote) {
                                       this.drainX = co.posX;
                                       this.drainY = co.posY;
                                       this.drainZ = co.posZ;
                                       this.drainColor = aspect.getColor();
                                    }
                                    break label207;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (!this.draining) {
                  recalc = true;
               }
            }
         }
      }

      if (this.counter % 100 == 0 && (recalc || this.nodes.isEmpty())) {
         this.findNodes();
      }

   }

   private void findNodes() {
      this.nodes = new ArrayList<>();

      for(int xx = -8; xx <= 8; ++xx) {
         for(int yy = -8; yy <= 8; ++yy) {
            for(int zz = -8; zz <= 8; ++zz) {
               TileEntity te = this.worldObj.getTileEntity(this.xCoord + xx, this.yCoord + yy, this.zCoord + zz);
               if (te instanceof INode) {
                  this.nodes.add(new ChunkCoordinates(this.xCoord + xx, this.yCoord + yy, this.zCoord + zz));
               }
            }
         }
      }

   }

   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
   }

   public void openInventory() {
   }

   public void closeInventory() {
   }

   public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
      return par2ItemStack != null && (par2ItemStack.getItem() instanceof ItemWandCasting || par2ItemStack.getItem() instanceof ItemAmuletVis);
   }

   public int[] getAccessibleSlotsFromSide(int par1) {
      return slots;
   }

   public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
      return this.getStackInSlot(par1) == null && (par2ItemStack.getItem() instanceof ItemWandCasting || par2ItemStack.getItem() instanceof ItemAmuletVis);
   }

   public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
      return true;
   }

   public AspectList getAspects() {
      if (this.getStackInSlot(0) != null && this.getStackInSlot(0).getItem() instanceof ItemWandCasting) {
         ItemWandCasting wand = (ItemWandCasting)this.getStackInSlot(0).getItem();
         AspectList al = wand.getAllVis(this.getStackInSlot(0));
         AspectList out = new AspectList();

         for(Aspect a : al.getAspectsSorted()) {
            out.add(a, al.getAmount(a) / 100);
         }

         return out;
      } else if (this.getStackInSlot(0) != null && this.getStackInSlot(0).getItem() instanceof ItemAmuletVis) {
         ItemAmuletVis amulet = (ItemAmuletVis)this.getStackInSlot(0).getItem();
         AspectList al = amulet.getAllVis(this.getStackInSlot(0));
         AspectList out = new AspectList();

         for(Aspect a : al.getAspectsSorted()) {
            out.add(a, al.getAmount(a) / 100);
         }

         return out;
      } else {
         return null;
      }
   }

   public void setAspects(AspectList aspects) {
   }

   public int addToContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      return false;
   }

   public boolean takeFromContainer(AspectList ot) {
      return false;
   }

   public boolean doesContainerContainAmount(Aspect tag, int amount) {
      return false;
   }

   public boolean doesContainerContain(AspectList ot) {
      return false;
   }

   public int containerContains(Aspect tag) {
      return 0;
   }

   public boolean doesContainerAccept(Aspect tag) {
      return true;
   }
}
