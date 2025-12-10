package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import thaumcraft.client.fx.other.FXShieldRunes;
import thaumcraft.common.Thaumcraft;

public class PacketFXShield implements IMessage, IMessageHandler<PacketFXShield,IMessage> {
   private int source;
   private int target;

   public PacketFXShield() {
   }

   public PacketFXShield(int source, int target) {
      this.source = source;
      this.target = target;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.source);
      buffer.writeInt(this.target);
   }

   public void fromBytes(ByteBuf buffer) {
      this.source = buffer.readInt();
      this.target = buffer.readInt();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketFXShield message, MessageContext ctx) {
      Entity p = Thaumcraft.proxy.getClientWorld().getEntityByID(message.source);
      if (p != null && Thaumcraft.proxy.getClientWorld() != null) {
         float pitch = 0.0F;
         float yaw = 0.0F;
         if (message.target >= 0) {
            Entity t = Thaumcraft.proxy.getClientWorld().getEntityByID(message.target);
            if (t != null) {
               double d0 = p.posX - t.posX;
               double d1 = (p.boundingBox.minY + p.boundingBox.maxY) / (double)2.0F - (t.boundingBox.minY + t.boundingBox.maxY) / (double)2.0F;
               double d2 = p.posZ - t.posZ;
               double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
               float f = (float)(Math.atan2(d2, d0) * (double)180.0F / Math.PI) - 90.0F;
               float f1 = (float)(-(Math.atan2(d1, d3) * (double)180.0F / Math.PI));
               pitch = f1;
               yaw = f;
            } else {
               pitch = 90.0F;
               yaw = 0.0F;
            }

            FXShieldRunes fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.posX, p.posY, p.posZ, p, 8, yaw, pitch);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
         } else if (message.target == -1) {
            FXShieldRunes fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.posX, p.posY, p.posZ, p, 8, 0.0F, 90.0F);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
            fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.posX, p.posY, p.posZ, p, 8, 0.0F, 270.0F);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
         } else if (message.target == -2) {
            FXShieldRunes fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.posX, p.posY, p.posZ, p, 8, 0.0F, 270.0F);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
         } else if (message.target == -3) {
            FXShieldRunes fb = new FXShieldRunes(Thaumcraft.proxy.getClientWorld(), p.posX, p.posY, p.posZ, p, 8, 0.0F, 90.0F);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fb);
         }

         return null;
      } else {
         return null;
      }
   }
}
