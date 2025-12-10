package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import thaumcraft.common.Thaumcraft;

import java.awt.*;

public class PacketFXBlockBubble implements IMessage, IMessageHandler<PacketFXBlockBubble,IMessage> {
   private int x;
   private int y;
   private int z;
   private int color;

   public PacketFXBlockBubble() {
   }

   public PacketFXBlockBubble(int x, int y, int z, int color) {
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

   public IMessage onMessage(PacketFXBlockBubble message, MessageContext ctx) {
      Color c = new Color(message.color);
      float r = (float)c.getRed() / 255.0F;
      float g = (float)c.getGreen() / 255.0F;
      float b = (float)c.getBlue() / 255.0F;

      for(int a = 0; a < Thaumcraft.particleCount(1); ++a) {
         Thaumcraft.crucibleBubble(Thaumcraft.getClientWorld(), (float)message.x, (float)message.y + Thaumcraft.getClientWorld().rand.nextFloat(), (float)message.z + Thaumcraft.getClientWorld().rand.nextFloat(), r, g, b);
         Thaumcraft.crucibleBubble(Thaumcraft.getClientWorld(), (float)(message.x + 1), (float)message.y + Thaumcraft.getClientWorld().rand.nextFloat(), (float)message.z + Thaumcraft.getClientWorld().rand.nextFloat(), r, g, b);
         Thaumcraft.crucibleBubble(Thaumcraft.getClientWorld(), (float)message.x + Thaumcraft.getClientWorld().rand.nextFloat(), (float)message.y + Thaumcraft.getClientWorld().rand.nextFloat(), (float)message.z, r, g, b);
         Thaumcraft.crucibleBubble(Thaumcraft.getClientWorld(), (float)message.x + Thaumcraft.getClientWorld().rand.nextFloat(), (float)message.y + Thaumcraft.getClientWorld().rand.nextFloat(), (float)(message.z + 1), r, g, b);
      }

      return null;
   }
}
