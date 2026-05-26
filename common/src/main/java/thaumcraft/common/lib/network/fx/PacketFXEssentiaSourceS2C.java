package thaumcraft.common.lib.network.fx;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.lib.events.EssentiaRemoteDrainHandler;

public class PacketFXEssentiaSourceS2C extends ThaumcraftBaseS2CMessage {

   public static final String ID = "thaumcraft:fx_essentia_source";
   public static MessageType messageType;

   private final int x;
   private final int y;
   private final int z;
   private final byte dx;
   private final byte dy;
   private final byte dz;
   private final int color;

   public PacketFXEssentiaSourceS2C(int x, int y, int z, byte dx, byte dy, byte dz, int color) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dx = dx;
      this.dy = dy;
      this.dz = dz;
      this.color = color;
   }

   @Override
   public MessageType getType() {
      return messageType;
   }

   @Override
   public void write(FriendlyByteBuf buf) {
      encode(this, buf);
   }

   public static void encode(PacketFXEssentiaSourceS2C msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.x);
      buf.writeInt(msg.y);
      buf.writeInt(msg.z);
      buf.writeInt(msg.color);
      buf.writeByte(msg.dx);
      buf.writeByte(msg.dy);
      buf.writeByte(msg.dz);
   }

   public static PacketFXEssentiaSourceS2C decode(FriendlyByteBuf buf) {
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      int color = buf.readInt();
      byte dx = buf.readByte();
      byte dy = buf.readByte();
      byte dz = buf.readByte();
      return new PacketFXEssentiaSourceS2C(x, y, z, dx, dy, dz, color);
   }

   @Override
   public void handle(NetworkManager.PacketContext context) {
      int tx = this.x - this.dx;
      int ty = this.y - this.dy;
      int tz = this.z - this.dz;

      String key = x + ":" + y + ":" + z + ":" + tx + ":" + ty + ":" + tz + ":" + color;

      if (EssentiaRemoteDrainHandler.sourceFX.containsKey(key)) {
         EssentiaRemoteDrainHandler.EssentiaSourceFX sf = EssentiaRemoteDrainHandler.sourceFX.get(key);
         sf.ticks = 15;
         EssentiaRemoteDrainHandler.sourceFX.remove(key);
         EssentiaRemoteDrainHandler.sourceFX.put(key, sf);
      } else {
         EssentiaRemoteDrainHandler.sourceFX.put(
                 key,
                 new EssentiaRemoteDrainHandler.EssentiaSourceFX(
                         new BlockPos(x,y, z),
                         new BlockPos(tx,ty, tz),
                         15,
                         color
                 )
         );
      }
   }
}
