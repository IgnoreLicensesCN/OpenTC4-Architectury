package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;

public class PacketFXBlockZap implements IMessage, IMessageHandler<PacketFXBlockZap,IMessage> {
   private float x;
   private float y;
   private float z;
   private float dx;
   private float dy;
   private float dz;

   public PacketFXBlockZap() {
   }

   public PacketFXBlockZap(float x, float y, float z, float dx, float dy, float dz) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.dx = dx;
      this.dy = dy;
      this.dz = dz;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeFloat(this.x);
      buffer.writeFloat(this.y);
      buffer.writeFloat(this.z);
      buffer.writeFloat(this.dx);
      buffer.writeFloat(this.dy);
      buffer.writeFloat(this.dz);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readFloat();
      this.y = buffer.readFloat();
      this.z = buffer.readFloat();
      this.dx = buffer.readFloat();
      this.dy = buffer.readFloat();
      this.dz = buffer.readFloat();
   }

   public IMessage onMessage(PacketFXBlockZap message, MessageContext ctx) {
      Thaumcraft.proxy.nodeBolt(Thaumcraft.proxy.getClientWorld(), message.x, message.y, message.z, message.dx, message.dy, message.dz);
      Thaumcraft.proxy.getClientWorld().playSound(message.x, message.y, message.z, "thaumcraft:zap", 0.1F, 1.0F + Thaumcraft.proxy.getClientWorld().rand.nextFloat() * 0.2F, false);
      return null;
   }
}
