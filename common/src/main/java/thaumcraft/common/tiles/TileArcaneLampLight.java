package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileArcaneLampLight extends TileEntity {
   int x = Integer.MAX_VALUE;
   int y = Integer.MAX_VALUE;
   int z = Integer.MAX_VALUE;
   int count = 0;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void updateEntity() {
      if (Platform.getEnvironment() != Env.CLIENT) {
         if (this.count == 0) {
            this.count = this.level().rand.nextInt(100);
         }

         if (++this.count % 100 == 0 && !(this.level().getTileEntity(this.x, this.y, this.z) instanceof TileArcaneLamp)) {
            this.level().setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
         }
      }

   }

   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.x = nbttagcompound.getInteger("sourceX");
      this.y = nbttagcompound.getInteger("sourceY");
      this.z = nbttagcompound.getInteger("sourceZ");
   }

   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setInteger("sourceX", this.x);
      nbttagcompound.setInteger("sourceY", this.y);
      nbttagcompound.setInteger("sourceZ", this.z);
   }
}
