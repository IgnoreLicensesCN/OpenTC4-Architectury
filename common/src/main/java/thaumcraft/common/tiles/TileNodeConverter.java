package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockAiry;
import thaumcraft.common.config.ConfigBlocks;

public class TileNodeConverter extends TileThaumcraft {
   public int count = -1;
   public int status = 0;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      super.updateEntity();
      if (this.count == -1) {
         this.checkStatus();
      }

      if (this.status == 1 && !this.worldObj.isRemote && this.count >= 1000) {
         TileEntity tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
         if (tile instanceof TileNode) {
            AspectList base = ((TileNode)tile).getAspectsBase();
            NodeType type = ((TileNode)tile).getNodeType();
            NodeModifier mod = ((TileNode)tile).getNodeModifier();
            this.worldObj.setBlock(this.xCoord, this.yCoord - 1, this.zCoord, ConfigBlocks.blockAiry, 5, 3);
            TileEntity tilenew = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
            if (tilenew instanceof TileNodeEnergized) {
               ((TileNodeEnergized)tilenew).setNodeModifier(mod);
               ((TileNodeEnergized)tilenew).setNodeType(type);
               ((TileNodeEnergized)tilenew).setAspects(base.copy());
               ((TileNodeEnergized)tilenew).setupNode();
            }

            this.checkStatus();
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 10, 10);
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
         }
      }

      if (this.status == 2 && !this.worldObj.isRemote && this.count <= 50) {
         TileEntity tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
         if (tile instanceof TileNodeEnergized) {
            AspectList base = ((TileNodeEnergized)tile).getAuraBase();
            NodeType type = ((TileNodeEnergized)tile).getNodeType();
            NodeModifier mod = ((TileNodeEnergized)tile).getNodeModifier();
            this.worldObj.setBlock(this.xCoord, this.yCoord - 1, this.zCoord, ConfigBlocks.blockAiry, 0, 3);
            TileEntity tilenew = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
            if (tilenew instanceof TileNode) {
               ((TileNode)tilenew).setNodeModifier(mod);
               ((TileNode)tilenew).setNodeType(type);
               ((TileNode)tilenew).setAspects(base.copy());

               for(Aspect a : ((TileNode)tilenew).getAspects().getAspects()) {
                  ((TileNode)tilenew).takeFromContainer(a, ((TileNode)tilenew).getAspects().getAmount(a));
               }
            }

            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 10, 10);
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.markDirty();
            this.status = 0;
         }
      }

      if (this.status != 0 && this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) {
         if (this.count < 1000) {
            ++this.count;
            if (!this.worldObj.isRemote) {
               TileEntity tilenew = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
               if (tilenew instanceof TileNode) {
                  TileNode nd = (TileNode)tilenew;
                  AspectList al = nd.getAspects();
                  if (al.getAspects().length > 0) {
                     nd.takeFromContainer(al.getAspects()[this.worldObj.rand.nextInt(al.getAspects().length)], 1);
                     if (this.count % 5 == 0 || nd.getAspects().visSize() == 0) {
                        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord - 1, this.zCoord);
                     }
                  }
               }
            }

            if (this.count > 50 && this.worldObj.isRemote) {
               if (this.worldObj.rand.nextBoolean()) {
                  Thaumcraft.proxy.nodeBolt(this.worldObj, (float)this.xCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord - 0.5F, (float)this.zCoord + 0.5F);
               }

               if (this.worldObj.rand.nextBoolean() && this.hasStabilizer()) {
                  Thaumcraft.proxy.nodeBolt(this.worldObj, (float)this.xCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F, (float)this.yCoord - 1.5F, (float)this.zCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord - 0.5F, (float)this.zCoord + 0.5F);
               }
            }
         }
      } else if (this.count > 0) {
         --this.count;
         if (this.count > 50 && this.worldObj.isRemote) {
            if (this.worldObj.rand.nextBoolean()) {
               Thaumcraft.proxy.nodeBolt(this.worldObj, (float)this.xCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F, (float)this.yCoord + 0.5F, (float)this.zCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord - 0.5F, (float)this.zCoord + 0.5F);
            }

            if (this.worldObj.rand.nextBoolean() && this.hasStabilizer()) {
               Thaumcraft.proxy.nodeBolt(this.worldObj, (float)this.xCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F, (float)this.yCoord - 1.5F, (float)this.zCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F, (float)this.xCoord + 0.5F, (float)this.yCoord - 0.5F, (float)this.zCoord + 0.5F);
            }
         }
      }

      if (this.count > 1000) {
         this.count = 1000;
      }

   }

   private boolean hasStabilizer() {
      TileEntity te = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 2, this.zCoord);
      return !this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord - 2, this.zCoord) && te instanceof TileNodeStabilizer;
   }

   public void checkStatus() {
      if (this.count == -1) {
         this.count = 0;
      }

      if (this.status != 2 || this.count <= 50 || this.hasStabilizer() && this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockAiry && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 5) {
         if (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockAiry && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 0 && this.hasStabilizer()) {
            this.status = 1;
            this.markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         } else if (this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockAiry && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 5) {
            this.status = 2;
            this.count = 1000;
            this.markDirty();
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         } else {
            this.status = 0;
         }
      } else {
         BlockAiry.explodify(this.getWorldObj(), this.xCoord, this.yCoord - 1, this.zCoord);
         this.status = 0;
         this.count = 50;
         this.markDirty();
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }

   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      super.readCustomNBT(nbttagcompound);
      this.status = nbttagcompound.getInteger("status");
      this.count = nbttagcompound.getInteger("count");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      super.writeCustomNBT(nbttagcompound);
      nbttagcompound.setInteger("status", this.status);
      nbttagcompound.setInteger("count", this.count);
   }

   public boolean receiveClientEvent(int i, int j) {
      if (i == 10 && j == 10) {
         if (this.worldObj.isRemote) {
            Thaumcraft.proxy.burst(this.worldObj, (double)this.xCoord + (double)0.5F, (double)this.yCoord - (double)0.5F, (double)this.zCoord + (double)0.5F, 1.0F);
            this.worldObj.playSound((double)this.xCoord + (double)0.5F, (double)this.yCoord - (double)0.5F, (double)this.zCoord + (double)0.5F, "thaumcraft:craftfail", 0.5F, 1.0F, false);
         }

         return true;
      } else {
         return super.receiveClientEvent(i, j);
      }
   }
}
