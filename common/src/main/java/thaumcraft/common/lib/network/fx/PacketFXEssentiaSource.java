package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChunkCoordinates;
import thaumcraft.common.lib.events.EssentiaHandler;

public class PacketFXEssentiaSource implements IMessage, IMessageHandler<PacketFXEssentiaSource,IMessage> {
   private int x;
   private int y;
   private int z;
   private byte dx;
   private byte dy;
   private byte dz;
   private int color;

   public PacketFXEssentiaSource() {
   }

   public PacketFXEssentiaSource(int x, int y, int z, byte dx, byte dy, byte dz, int color) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dx = dx;
      this.dy = dy;
      this.dz = dz;
      this.color = color;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeInt(this.color);
      buffer.writeByte(this.dx);
      buffer.writeByte(this.dy);
      buffer.writeByte(this.dz);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.color = buffer.readInt();
      this.dx = buffer.readByte();
      this.dy = buffer.readByte();
      this.dz = buffer.readByte();
   }

   public IMessage onMessage(PacketFXEssentiaSource message, MessageContext ctx) {
      int tx = message.x - message.dx;
      int ty = message.y - message.dy;
      int tz = message.z - message.dz;
      String key = message.x + ":" + message.y + ":" + message.z + ":" + tx + ":" + ty + ":" + tz + ":" + message.color;
      if (EssentiaHandler.sourceFX.containsKey(key)) {
         EssentiaHandler.EssentiaSourceFX sf = EssentiaHandler.sourceFX.get(key);
         sf.ticks = 15;
         EssentiaHandler.sourceFX.remove(key);
         EssentiaHandler.sourceFX.put(key, sf);
      } else {
         EssentiaHandler.sourceFX.put(key, new EssentiaHandler.EssentiaSourceFX(new ChunkCoordinates(message.x, message.y, message.z), new ChunkCoordinates(tx, ty, tz), 15, message.color));
      }

      return null;
   }
}
