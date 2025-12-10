package thaumcraft.common.lib.network.misc;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.client.lib.RenderEventHandler;

public class PacketMiscEvent implements IMessage, IMessageHandler<PacketMiscEvent,IMessage> {
   private short type;
   public static final short WARP_EVENT = 0;
   public static final short MIST_EVENT = 1;
   public static final short MIST_EVENT_SHORT = 2;

   public PacketMiscEvent() {
   }

   public PacketMiscEvent(short type) {
      this.type = type;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeShort(this.type);
   }

   public void fromBytes(ByteBuf buffer) {
      this.type = buffer.readShort();
   }

   @SideOnly(Side.CLIENT)
   public IMessage onMessage(PacketMiscEvent message, MessageContext ctx) {
      Player p = Minecraft.getMinecraft().thePlayer;
      switch (message.type) {
         case 0:
            ClientTickEventsFML.warpVignette = 100;
            p.level().playSound(p.posX, p.posY, p.posZ, "thaumcraft:heartbeat", 1.0F, 1.0F, false);
            break;
         case 1:
            RenderEventHandler.fogFiddled = true;
            RenderEventHandler.fogDuration = 2400;
            break;
         case 2:
            RenderEventHandler.fogFiddled = true;
            if (RenderEventHandler.fogDuration < 200) {
               RenderEventHandler.fogDuration = 200;
            }
         case 3:
      }

      return null;
   }
}
