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
import thaumcraft.client.fx.bolt.FXLightningBolt;

public class PacketFXZap implements IMessage, IMessageHandler<PacketFXZap,IMessage> {
   private int source;
   private int target;

   public PacketFXZap() {
   }

   public PacketFXZap(int source, int target) {
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
   public IMessage onMessage(PacketFXZap message, MessageContext ctx) {
      Minecraft mc = FMLClientHandler.instance().getClient();
      WorldClient world = mc.theWorld;
      Entity var2 = this.getEntityByID(message.source, mc, world);
      Entity var3 = this.getEntityByID(message.target, mc, world);
      if (var2 != null && var3 != null) {
         FXLightningBolt bolt = new FXLightningBolt(world, var2.posX, var2.boundingBox.minY + (double)(var2.height / 2.0F), var2.posZ, var3.posX, var3.boundingBox.minY + (double)(var3.height / 2.0F), var3.posZ, world.rand.nextLong(), 6, 0.5F, 8);
         bolt.defaultFractal();
         bolt.setType(2);
         bolt.setWidth(0.125F);
         bolt.finalizeBolt();
      }

      return null;
   }

   @SideOnly(Side.CLIENT)
   private Entity getEntityByID(int par1, Minecraft mc, WorldClient world) {
      return par1 == mc.thePlayer.getEntityId() ? mc.thePlayer : world.getEntityByID(par1);
   }
}
