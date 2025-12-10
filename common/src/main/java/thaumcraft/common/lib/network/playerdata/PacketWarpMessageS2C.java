package thaumcraft.common.lib.network.playerdata;


package thaumcraft.common.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import com.linearity.opentc4.utils.StatCollector;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;

public class PacketWarpMessage {
   public static final ResourceLocation ID = new ResourceLocation(Thaumcraft.MOD_ID, "warp_message");

   public final int data;
   public final byte type;

   public PacketWarpMessage(byte type,int data) {
      this.data = data;
      this.type = type;
   }

   /** 解码 */
   public static PacketWarpMessage decode(FriendlyByteBuf buf) {
      int data = buf.readInt();
      byte type = buf.readByte();
      return new PacketWarpMessage(type,data);
   }

   /** 编码 */
   public static void encode(PacketWarpMessage msg, FriendlyByteBuf buf) {
      buf.writeInt(msg.data);
      buf.writeByte(msg.type);
   }

   /** 处理（只在客户端执行） */
   public static void receive(PacketWarpMessage msg, Player player) {
      if (player.level().isClientSide) {
         ClientHandler.handle(msg);
      }
   }

   // ---------------- CLIENT LOGIC -------------------
   public static class ClientHandler {
      public static void handle(PacketWarpMessage message) {
         if (message.data == 0) return;

         String text;
         int change = message.data;

         if (message.type == 0) { // NORMAL WARP
            text = change < 0 ?
                    StatCollector.translateToLocal("tc.removewarp") :
                    StatCollector.translateToLocal("tc.addwarp");

            if (change > 0)
               playWhisper();
         }
         else if (message.type == 1) { // STICKY WARP
            text = change < 0 ?
                    StatCollector.translateToLocal("tc.removewarpsticky") :
                    StatCollector.translateToLocal("tc.addwarpsticky");

            if (change > 0)
               playWhisper();
         }
         else { // TEMP WARP
            text = change < 0 ?
                    StatCollector.translateToLocal("tc.removewarptemp") :
                    StatCollector.translateToLocal("tc.addwarptemp");
         }

         PlayerNotifications.addNotification(text);
      }

      private static void playWhisper() {
         var mc = net.minecraft.client.Minecraft.getInstance();
         mc.player.playSound(
                 net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_RESONATE, // 可替换为你自己的
                 0.5F, 1.0F
         );
      }
   }
}


//import cpw.mods.fml.common.network.simpleimpl.IMessage;
//import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
//import cpw.mods.fml.common.network.simpleimpl.MessageContext;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import io.netty.buffer.ByteBuf;
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.entity.player.Player;
//import com.linearity.opentc4.utils.StatCollector;
//import thaumcraft.client.lib.PlayerNotifications;
//
//public class PacketWarpMessage implements IMessage, IMessageHandler<PacketWarpMessage,IMessage> {
//   protected int data = 0;
//   protected byte type = 0;
//
//   public PacketWarpMessage() {
//   }
//
//   public PacketWarpMessage(Player player, byte type, int change) {
//      this.data = change;
//      this.type = type;
//   }
//
//   public void toBytes(ByteBuf buffer) {
//      buffer.writeInt(this.data);
//      buffer.writeByte(this.type);
//   }
//
//   public void fromBytes(ByteBuf buffer) {
//      this.data = buffer.readInt();
//      this.type = buffer.readByte();
//   }
//
//   @SideOnly(Side.CLIENT)
//   public IMessage onMessage(PacketWarpMessage message, MessageContext ctx) {
//      if (message.data != 0) {
//         if (message.type == 0 && message.data > 0) {
//            String text = StatCollector.translateToLocal("tc.addwarp");
//            if (message.data < 0) {
//               text = StatCollector.translateToLocal("tc.removewarp");
//            } else {
//               Minecraft.getMinecraft().thePlayer.playSound("thaumcraft:whispers", 0.5F, 1.0F);
//            }
//
//            PlayerNotifications.addNotification(text);
//         } else if (message.type == 1) {
//            String text = StatCollector.translateToLocal("tc.addwarpsticky");
//            if (message.data < 0) {
//               text = StatCollector.translateToLocal("tc.removewarpsticky");
//            } else {
//               Minecraft.getMinecraft().thePlayer.playSound("thaumcraft:whispers", 0.5F, 1.0F);
//            }
//
//            PlayerNotifications.addNotification(text);
//         } else if (message.data > 0) {
//            String text = StatCollector.translateToLocal("tc.addwarptemp");
//            if (message.data < 0) {
//               text = StatCollector.translateToLocal("tc.removewarptemp");
//            }
//
//            PlayerNotifications.addNotification(text);
//         }
//      }
//
//      return null;
//   }
//}
