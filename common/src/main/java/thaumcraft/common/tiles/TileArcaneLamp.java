package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.config.ConfigBlocks;

public class TileArcaneLamp extends TileThaumcraft {
   public ForgeDirection facing = ForgeDirection.getOrientation(0);

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (!this.worldObj.isRemote) {
         int x = this.xCoord + this.worldObj.rand.nextInt(16) - this.worldObj.rand.nextInt(16);
         int y = this.yCoord + this.worldObj.rand.nextInt(16) - this.worldObj.rand.nextInt(16);
         int z = this.zCoord + this.worldObj.rand.nextInt(16) - this.worldObj.rand.nextInt(16);
         if (y > this.worldObj.getHeightValue(x, z) + 4) {
            y = this.worldObj.getHeightValue(x, z) + 4;
         }

         if (y < 5) {
            y = 5;
         }

         if (this.worldObj.isAirBlock(x, y, z) && this.worldObj.getBlock(x, y, z) != ConfigBlocks.blockAiry && this.worldObj.getBlockLightValue(x, y, z) < 9) {
            this.worldObj.setBlock(x, y, z, ConfigBlocks.blockAiry, 3, 3);
         }
      }

   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = ForgeDirection.getOrientation(nbttagcompound.getInteger("orientation"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("orientation", this.facing.ordinal());
   }

   public void removeLights() {
      for(int x = -15; x <= 15; ++x) {
         for(int y = -15; y <= 15; ++y) {
            for(int z = -15; z <= 15; ++z) {
               if (this.worldObj.getBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z) == ConfigBlocks.blockAiry && this.worldObj.getBlockMetadata(this.xCoord + x, this.yCoord + y, this.zCoord + z) == 3) {
                  this.worldObj.setBlockToAir(this.xCoord + x, this.yCoord + y, this.zCoord + z);
               }
            }
         }
      }

   }
}
