package thaumcraft.common.lib.network.misc;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.items.wands.WandCastingItem;
import thaumcraft.common.items.wands.WandManager;

@Deprecated(forRemoval = true,since = "i want to order to server that change which 'FocusContainer' and which focus inside,or remove focus.")
public class PacketFocusChangeToServer implements IMessage, IMessageHandler<PacketFocusChangeToServer,IMessage> {
   private int dim;
   private int playerid;
   private String focus;

   public PacketFocusChangeToServer() {
   }

   public PacketFocusChangeToServer(Player player, String focus) {
      this.dim = player.level().dimension();
      this.playerid = player.getEntityId();
      this.focus = focus;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.dim);
      buffer.writeInt(this.playerid);
      ByteBufUtils.writeUTF8String(buffer, this.focus);
   }

   public void fromBytes(ByteBuf buffer) {
      this.dim = buffer.readInt();
      this.playerid = buffer.readInt();
      this.focus = ByteBufUtils.readUTF8String(buffer);
   }

   public IMessage onMessage(PacketFocusChangeToServer message, MessageContext ctx) {
      Level world = DimensionManager.getWorld(message.dim);
      if (world != null && (ctx.getServerHandler().playerEntity == null || ctx.getServerHandler().playerEntity.getEntityId() == message.playerid)) {
         Entity player = world.getEntityByID(message.playerid);
         if (player instanceof Player && ((Player) player).getHeldItem() != null && ((Player) player).getHeldItem().getItem() instanceof WandCastingItem && !((WandCastingItem) ((Player) player).getHeldItem().getItem()).isSceptre(((Player) player).getHeldItem())) {
            WandManager.changeFocus(((Player)player).getHeldItem(), world, (Player)player, message.focus);
         }

         return null;
      } else {
         return null;
      }
   }
}
