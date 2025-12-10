package thaumcraft.common.lib.network.fx;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import thaumcraft.client.fx.beams.FXBeam;

import java.awt.*;

public class PacketFXBeamPulse implements IMessage, IMessageHandler<PacketFXBeamPulse,IMessage> {
   private int source;
   private int target;
   private int color;

   public PacketFXBeamPulse() {
   }

   public PacketFXBeamPulse(int source, int target, int color) {
      this.source = source;
      this.target = target;
      this.color = color;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.source);
      buffer.writeInt(this.target);
      buffer.writeInt(this.color);
   }

   public void fromBytes(ByteBuf buffer) {
      this.source = buffer.readInt();
      this.target = buffer.readInt();
      this.color = buffer.readInt();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketFXBeamPulse message, MessageContext ctx) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      WorldClient world = mc.theWorld;
      Entity var2 = this.getEntityByID(message.source, mc, world);
      Entity var3 = this.getEntityByID(message.target, mc, world);
      if (var2 != null && var3 != null) {
         Color c = new Color(message.color);
         FXBeam beamcon = new FXBeam(world, var2.posX, var2.posY + (double)var2.getEyeHeight(), var2.posZ, var3, (float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 20);
         beamcon.blendmode = 771;
         beamcon.width = 2.5F;
         beamcon.setType(1);
         beamcon.setReverse(true);
         beamcon.setPulse(true);
         FMLClientHandler.instance().getClient().effectRenderer.addEffect(beamcon);
      }

      return null;
   }

   @SideOnly(Side.CLIENT)
   private Entity getEntityByID(int par1, Minecraft mc, WorldClient world) {
      return par1 == mc.thePlayer.getEntityId() ? mc.thePlayer : world.getEntityByID(par1);
   }
}
