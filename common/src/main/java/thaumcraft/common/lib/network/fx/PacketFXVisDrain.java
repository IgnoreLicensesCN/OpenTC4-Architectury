package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXVisSparkle;
import thaumcraft.common.Thaumcraft;

import java.awt.*;

public class PacketFXVisDrain implements IMessage, IMessageHandler<PacketFXVisDrain,IMessage> {
   private int x;
   private int y;
   private int z;
   private int color;
   private int dx;
   private int dy;
   private int dz;

   public PacketFXVisDrain() {
   }

   public PacketFXVisDrain(int x, int y, int z, int xd, int xy, int xz, int color) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.color = color;
      this.dx = xd - x;
      this.dy = xy - y;
      this.dz = xz - z;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.x);
      buffer.writeShort(this.y);
      buffer.writeShort(this.z);
      buffer.writeByte(this.color);
      buffer.writeByte(this.dx);
      buffer.writeByte(this.dy);
      buffer.writeByte(this.dz);
   }

   public void fromBytes(ByteBuf buffer) {
      this.x = buffer.readShort();
      this.y = buffer.readShort();
      this.z = buffer.readShort();
      this.color = Aspect.getPrimalAspects().get(buffer.readByte()).getColor();
      this.dx = this.x + buffer.readByte();
      this.dy = this.y + buffer.readByte();
      this.dz = this.z + buffer.readByte();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketFXVisDrain message, MessageContext ctx) {
      World worldObj = Thaumcraft.proxy.getClientWorld();
      FXVisSparkle fb = new FXVisSparkle(worldObj, (double)message.dx + 0.4 + (double)(worldObj.rand.nextFloat() * 0.2F), (double)message.dy + 0.4 + (double)(worldObj.rand.nextFloat() * 0.2F), (double)message.dz + 0.4 + (double)(worldObj.rand.nextFloat() * 0.2F), (float)message.x + worldObj.rand.nextFloat(), (float)message.y + worldObj.rand.nextFloat(), (float)message.z + worldObj.rand.nextFloat());
      Color c = new Color(message.color);
      fb.setRBGColorF((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F);
      ParticleEngine.instance.addEffect(worldObj, fb);
      return null;
   }
}
