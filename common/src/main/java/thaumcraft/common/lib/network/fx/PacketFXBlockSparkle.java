package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;

public class PacketFXBlockSparkle implements IMessage, IMessageHandler<PacketFXBlockSparkle,IMessage> {
   private int x;
   private int y;
   private int z;
   private int color;

   public PacketFXBlockSparkle() {
   }

   public PacketFXBlockSparkle(int x, int y, int z, int color) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.color = color;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeInt(this.color);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.color = buffer.readInt();
   }

   public IMessage onMessage(PacketFXBlockSparkle message, MessageContext ctx) {
      Thaumcraft.proxy.blockSparkle(Thaumcraft.proxy.getClientWorld(), message.x, message.y, message.z, message.color, 7);
      return null;
   }
}
