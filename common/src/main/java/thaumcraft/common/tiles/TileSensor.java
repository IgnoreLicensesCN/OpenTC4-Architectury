package thaumcraft.common.tiles;

import net.minecraft.world.level.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.common.config.ConfigBlocks;

import java.util.ArrayList;
import java.util.WeakHashMap;

public class TileSensor extends BlockEntity {
   public byte note = 0;
   public byte tone = 0;
   public int redstoneSignal = 0;
   public static WeakHashMap noteBlockEvents = new WeakHashMap();

   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("note", this.note);
      par1NBTTagCompound.setByte("tone", this.tone);
   }

   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      this.note = par1NBTTagCompound.getByte("note");
      this.tone = par1NBTTagCompound.getByte("tone");
      if (this.note < 0) {
         this.note = 0;
      }

      if (this.note > 24) {
         this.note = 24;
      }

   }

   public void updateEntity() {
      super.updateEntity();
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (this.redstoneSignal > 0) {
            --this.redstoneSignal;
            if (this.redstoneSignal == 0) {
               this.level().notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockWoodenDevice);
               this.level().notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, ConfigBlocks.blockWoodenDevice);
               this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
         }

         ArrayList<Integer[]> nbe = (ArrayList)noteBlockEvents.get(this.level());
         if (nbe != null) {
            for(Integer[] dat : nbe) {
               if (dat[3] == this.tone && dat[4] == this.note && this.getDistanceFrom((double)dat[0] + (double)0.5F, (double)dat[1] + (double)0.5F, (double)dat[2] + (double)0.5F) <= (double)4096.0F) {
                  this.triggerNote(this.level(), this.xCoord, this.yCoord, this.zCoord, false);
                  this.redstoneSignal = 10;
                  this.level().notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, ConfigBlocks.blockWoodenDevice);
                  this.level().notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, ConfigBlocks.blockWoodenDevice);
                  this.level().markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                  break;
               }
            }
         }
      }

   }

   public double getDistanceFrom(double par1, double par3, double par5) {
      double var7 = (double)this.xCoord + (double)0.5F - par1;
      double var9 = (double)this.yCoord + (double)0.5F - par3;
      double var11 = (double)this.zCoord + (double)0.5F - par5;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateTone() {
      Material var5 = this.level().getBlock(this.xCoord, this.yCoord - 1, this.zCoord).getMaterial();
      this.tone = 0;
      if (var5 == Material.rock) {
         this.tone = 1;
      }

      if (var5 == Material.sand) {
         this.tone = 2;
      }

      if (var5 == Material.glass) {
         this.tone = 3;
      }

      if (var5 == Material.wood) {
         this.tone = 4;
      }

   }

   public void changePitch() {
      this.note = (byte)((this.note + 1) % 25);
      this.markDirty();
   }

   public void triggerNote(Level par1World, int par2, int par3, int par4, boolean sound) {
      if (par1World.getBlock(par2, par3 + 1, par4).getMaterial() == Material.air) {
         byte var6 = -1;
         if (sound) {
            Material var5 = par1World.getBlock(par2, par3 - 1, par4).getMaterial();
            var6 = 0;
            if (var5 == Material.rock) {
               var6 = 1;
            }

            if (var5 == Material.sand) {
               var6 = 2;
            }

            if (var5 == Material.glass) {
               var6 = 3;
            }

            if (var5 == Material.wood) {
               var6 = 4;
            }
         }

         par1World.addBlockEvent(par2, par3, par4, ConfigBlocks.blockWoodenDevice, var6, this.note);
      }

   }
}
