package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;

public class TileBrainbox extends TileThaumcraft {
   public ForgeDirection facing;

   public TileBrainbox() {
      this.facing = ForgeDirection.UNKNOWN;
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.facing = ForgeDirection.getOrientation(nbttagcompound.getByte("facing"));
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setByte("facing", (byte)this.facing.ordinal());
   }

   public boolean canUpdate() {
      return false;
   }
}
