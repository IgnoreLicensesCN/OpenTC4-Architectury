package thaumcraft.common.lib.network.misc;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.items.armor.Hover;

public class PacketFlyToServer implements IMessage, IMessageHandler<PacketFlyToServer,IMessage> {
   private int playerid;
   private boolean hover;

   public PacketFlyToServer() {
   }

   public PacketFlyToServer(Player player, boolean hover) {
      this.playerid = player.getEntityId();
      this.hover = hover;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.playerid);
      buffer.writeBoolean(this.hover);
   }

   public void fromBytes(ByteBuf buffer) {
      this.playerid = buffer.readInt();
      this.hover = buffer.readBoolean();
   }

   public IMessage onMessage(PacketFlyToServer message, MessageContext ctx) {
      Hover.setHover(message.playerid, message.hover);
      return null;
   }
}
