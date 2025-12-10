package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.boss.EntityCultistPortal;

public class PacketFXBlockArc implements IMessage, IMessageHandler<PacketFXBlockArc,IMessage> {
   private int x;
   private int y;
   private int z;
   private int source;

   public PacketFXBlockArc() {
   }

   public PacketFXBlockArc(int x, int y, int z, int source) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.source = source;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.x);
      buffer.writeInt(this.y);
      buffer.writeInt(this.z);
      buffer.writeInt(this.source);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readInt();
      this.y = buffer.readInt();
      this.z = buffer.readInt();
      this.source = buffer.readInt();
   }

   public IMessage onMessage(PacketFXBlockArc message, MessageContext ctx) {
      Entity p = Thaumcraft.proxy.getClientWorld().getEntityByID(message.source);
      if (p != null) {
         float r = 0.3F - Thaumcraft.proxy.getClientWorld().rand.nextFloat() * 0.1F;
         float g = 0.0F;
         float b = 0.5F + Thaumcraft.proxy.getClientWorld().rand.nextFloat() * 0.2F;
         if (p instanceof EntityCultistPortal) {
            r = 0.5F + Thaumcraft.proxy.getClientWorld().rand.nextFloat() * 0.2F;
            g = 0.0F;
            b = 0.0F;
         }

         Thaumcraft.proxy.arcLightning(Thaumcraft.proxy.getClientWorld(), p.posX, p.boundingBox.minY + (double)(p.height / 2.0F), p.posZ, (double)message.x + (double)0.5F, message.y + 1, (double)message.z + (double)0.5F, r, g, b, 0.5F);
      }

      return null;
   }
}
