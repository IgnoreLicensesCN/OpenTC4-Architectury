package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.core.Direction;
import thaumcraft.api.tile.TileThaumcraft;

public class TileBrainbox extends TileThaumcraft {
   public Direction facing;

   public TileBrainbox() {
      this.facing = Direction.UNKNOWN;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = Direction.getOrientation(nbttagcompound.getByte("facing"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setByte("facing", (byte)this.facing.ordinal());
   }

   public boolean canUpdate() {
      return false;
   }
}
