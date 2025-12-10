package thaumcraft.common.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import thaumcraft.api.TileThaumcraft;

public class TileInfusionPillar extends TileThaumcraft {
   public byte orientation = 0;

   public boolean canUpdate() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1, this.xCoord + 1, this.yCoord + 2, this.zCoord + 1);
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      this.orientation = nbttagcompound.getByte("orientation");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setByte("orientation", this.orientation);
   }
}
