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
import net.minecraft.entity.EntityLivingBase;
import thaumcraft.client.fx.beams.FXBeamGolemBoss;

public class PacketFXBeamPulseGolemBoss implements IMessage, IMessageHandler<PacketFXBeamPulseGolemBoss,IMessage> {
   private int source;
   private int target;

   public PacketFXBeamPulseGolemBoss() {
   }

   public PacketFXBeamPulseGolemBoss(int source, int target) {
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
   public IMessage onMessage(PacketFXBeamPulseGolemBoss message, MessageContext ctx) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      WorldClient world = mc.theWorld;
      EntityLivingBase var2 = (EntityLivingBase)this.getEntityByID(message.source, mc, world);
      EntityLivingBase var3 = (EntityLivingBase)this.getEntityByID(message.target, mc, world);
      if (var2 != null && var3 != null) {
         FXBeamGolemBoss beamcon = new FXBeamGolemBoss(world, var2, var3, 0.07F, 0.376F, 0.325F, 20);
         beamcon.blendmode = 1;
         beamcon.width = 3.0F;
         beamcon.setType(2);
         beamcon.setReverse(false);
         beamcon.setPulse(true);
         FMLClientHandler.instance().getClient().effectRenderer.addEffect(beamcon);
         FXBeamGolemBoss beamcon2 = new FXBeamGolemBoss(world, var2, var3, 1.0F, 0.5F, 0.5F, 20);
         beamcon2.blendmode = 1;
         beamcon2.width = 1.5F;
         beamcon2.setType(1);
         beamcon2.setReverse(false);
         beamcon2.setPulse(true);
         FMLClientHandler.instance().getClient().effectRenderer.addEffect(beamcon2);
      }

      return null;
   }

   @SideOnly(Side.CLIENT)
   private Entity getEntityByID(int par1, Minecraft mc, WorldClient world) {
      return par1 == mc.thePlayer.getEntityId() ? mc.thePlayer : world.getEntityByID(par1);
   }
}
