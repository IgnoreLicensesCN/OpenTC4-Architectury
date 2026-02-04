package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.core.Direction;
import thaumcraft.api.tile.TileThaumcraft;
import thaumcraft.common.config.ConfigBlocks;

public class TileArcaneLamp extends TileThaumcraft {
   public Direction facing = Direction.getOrientation(0);

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (Platform.getEnvironment() != Env.CLIENT) {
         int x = this.xCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
         int y = this.yCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
         int z = this.zCoord + this.level().rand.nextInt(16) - this.level().rand.nextInt(16);
         if (y > this.level().getHeightValue(x, z) + 4) {
            y = this.level().getHeightValue(x, z) + 4;
         }

         if (y < 5) {
            y = 5;
         }

         if (this.level().isAirBlock(x, y, z) && this.level().getBlock(x, y, z) != ConfigBlocks.blockAiry && this.level().getBlockLightValue(x, y, z) < 9) {
            this.level().setBlock(x, y, z, ConfigBlocks.blockAiry, 3, 3);
         }
      }

   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = Direction.getOrientation(nbttagcompound.getInteger("orientation"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("orientation", this.facing.ordinal());
   }

   public void removeLights() {
      for(int x = -15; x <= 15; ++x) {
         for(int y = -15; y <= 15; ++y) {
            for(int z = -15; z <= 15; ++z) {
               if (this.level().getBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z) == ConfigBlocks.blockAiry && this.level().getBlockMetadata(this.xCoord + x, this.yCoord + y, this.zCoord + z) == 3) {
                  this.level().setBlockToAir(this.xCoord + x, this.yCoord + y, this.zCoord + z);
               }
            }
         }
      }

   }
}
