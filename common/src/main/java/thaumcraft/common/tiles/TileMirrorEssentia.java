package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.lib.utils.Utils;

public class TileMirrorEssentia extends TileThaumcraft implements IAspectSource {
   public boolean linked = false;
   public int linkX;
   public int linkY;
   public int linkZ;
   public int linkDim;
   public ForgeDirection linkedFacing;
   int count;
   int inc;

   public TileMirrorEssentia() {
      this.linkedFacing = ForgeDirection.UNKNOWN;
      this.count = 0;
      this.inc = 40;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.linked = nbttagcompound.getBoolean("linked");
      this.linkX = nbttagcompound.getInteger("linkX");
      this.linkY = nbttagcompound.getInteger("linkY");
      this.linkZ = nbttagcompound.getInteger("linkZ");
      this.linkDim = nbttagcompound.getInteger("linkDim");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setBoolean("linked", this.linked);
      nbttagcompound.setInteger("linkX", this.linkX);
      nbttagcompound.setInteger("linkY", this.linkY);
      nbttagcompound.setInteger("linkZ", this.linkZ);
      nbttagcompound.setInteger("linkDim", this.linkDim);
   }

   public void restoreLink() {
      if (this.isDestinationValid()) {
         World targetWorld = MinecraftServer.getServer().worldServerForDimension(this.linkDim);
         if (targetWorld == null) {
            return;
         }

         TileEntity te = targetWorld.getTileEntity(this.linkX, this.linkY, this.linkZ);
         if (te instanceof TileMirrorEssentia) {
            TileMirrorEssentia tm = (TileMirrorEssentia)te;
            tm.linked = true;
            tm.linkX = this.xCoord;
            tm.linkY = this.yCoord;
            tm.linkZ = this.zCoord;
            tm.linkDim = this.worldObj.provider.dimensionId;
            targetWorld.markBlockForUpdate(tm.xCoord, tm.yCoord, tm.zCoord);
            this.linkedFacing = ForgeDirection.getOrientation(targetWorld.getBlockMetadata(this.linkX, this.linkY, this.linkZ));
            this.linked = true;
            this.markDirty();
            tm.markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }
      }

   }

   public void invalidateLink() {
      World targetWorld = DimensionManager.getWorld(this.linkDim);
      if (targetWorld != null) {
         if (Utils.isChunkLoaded(targetWorld, this.linkX, this.linkZ)) {
            TileEntity te = targetWorld.getTileEntity(this.linkX, this.linkY, this.linkZ);
            if (te instanceof TileMirrorEssentia) {
               TileMirrorEssentia tm = (TileMirrorEssentia)te;
               tm.linked = false;
               tm.linkedFacing = ForgeDirection.UNKNOWN;
               this.markDirty();
               tm.markDirty();
               targetWorld.markBlockForUpdate(this.linkX, this.linkY, this.linkZ);
            }

         }
      }
   }

   public boolean isLinkValid() {
      if (!this.linked) {
         return false;
      } else {
         World targetWorld = DimensionManager.getWorld(this.linkDim);
         if (targetWorld == null) {
            return false;
         } else {
            TileEntity te = targetWorld.getTileEntity(this.linkX, this.linkY, this.linkZ);
            if (te instanceof TileMirrorEssentia) {
               TileMirrorEssentia tm = (TileMirrorEssentia)te;
               if (!tm.linked) {
                  this.linked = false;
                  this.markDirty();
                  this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                  return false;
               } else if (tm.linkX == this.xCoord && tm.linkY == this.yCoord && tm.linkZ == this.zCoord && tm.linkDim == this.worldObj.provider.dimensionId) {
                  return true;
               } else {
                  this.linked = false;
                  this.markDirty();
                  this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                  return false;
               }
            } else {
               this.linked = false;
               this.markDirty();
               this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
               return false;
            }
         }
      }
   }

   public boolean isLinkValidSimple() {
      if (!this.linked) {
         return false;
      } else {
         World targetWorld = DimensionManager.getWorld(this.linkDim);
         if (targetWorld == null) {
            return false;
         } else {
            TileEntity te = targetWorld.getTileEntity(this.linkX, this.linkY, this.linkZ);
            if (te instanceof TileMirrorEssentia) {
               TileMirrorEssentia tm = (TileMirrorEssentia)te;
               if (!tm.linked) {
                  return false;
               } else {
                  return tm.linkX == this.xCoord && tm.linkY == this.yCoord && tm.linkZ == this.zCoord && tm.linkDim == this.worldObj.provider.dimensionId;
               }
            } else {
               return false;
            }
         }
      }
   }

   public boolean isDestinationValid() {
      World targetWorld = DimensionManager.getWorld(this.linkDim);
      if (targetWorld == null) {
         return false;
      } else {
         TileEntity te = targetWorld.getTileEntity(this.linkX, this.linkY, this.linkZ);
         if (te instanceof TileMirrorEssentia) {
            TileMirrorEssentia tm = (TileMirrorEssentia)te;
            return !tm.isLinkValid();
         } else {
            this.linked = false;
            this.markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return false;
         }
      }
   }

   public AspectList getAspects() {
      return null;
   }

   public void setAspects(AspectList aspects) {
   }

   public boolean doesContainerAccept(Aspect tag) {
      return false;
   }

   public int addToContainer(Aspect tag, int amount) {
      return 0;
   }

   public boolean takeFromContainer(Aspect tag, int amount) {
      if (this.isLinkValid() && amount <= 1) {
         World targetWorld = DimensionManager.getWorld(this.linkDim);
         if (this.linkedFacing == ForgeDirection.UNKNOWN && targetWorld != null) {
            this.linkedFacing = ForgeDirection.getOrientation(targetWorld.getBlockMetadata(this.linkX, this.linkY, this.linkZ) % 6);
         }

         TileEntity te = targetWorld.getTileEntity(this.linkX, this.linkY, this.linkZ);
         return te instanceof TileMirrorEssentia && EssentiaHandler.drainEssentia(te, tag, this.linkedFacing, 8, true);
      } else {
         return false;
      }
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

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (!this.worldObj.isRemote && this.count++ % this.inc == 0) {
         if (!this.isLinkValidSimple()) {
            if (this.inc < 600) {
               this.inc += 20;
            }

            this.restoreLink();
         } else {
            this.inc = 40;
         }
      }

   }
}
