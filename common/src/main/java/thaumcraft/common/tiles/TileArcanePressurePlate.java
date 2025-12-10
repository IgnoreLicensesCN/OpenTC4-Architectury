package thaumcraft.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileArcanePressurePlate extends TileOwned {
   public byte setting = 0;

   public boolean canUpdate() {
       return super.canUpdate();
   }

   public void readCustomNBT(NBTTagCompound nbttagcompound) {
      super.readCustomNBT(nbttagcompound);
      this.setting = nbttagcompound.getByte("setting");
   }

   public void writeCustomNBT(NBTTagCompound nbttagcompound) {
      super.writeCustomNBT(nbttagcompound);
      nbttagcompound.setByte("setting", this.setting);
   }

   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      if (this.worldObj != null && this.worldObj.isRemote) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }

   }
}
