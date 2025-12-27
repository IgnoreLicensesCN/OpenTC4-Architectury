package thaumcraft.common.lib.network.playerdata;

import com.linearity.opentc4.utils.StatCollector;
import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;

public class PacketWarpMessageS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":warp_message";

    public int data;
    public byte type;

    public PacketWarpMessageS2C() {}

    public PacketWarpMessageS2C(byte type, int data) {
        this.data = data;
        this.type = type;
    }

    /**
     * 解码
     */
    public static PacketWarpMessageS2C decode(FriendlyByteBuf buf) {
        int data = buf.readInt();
        byte type = buf.readByte();
        return new PacketWarpMessageS2C(type, data);
    }

    /**
     * 编码
     */
    public static void encode(PacketWarpMessageS2C msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.data);
        buf.writeByte(msg.type);
    }

    /**
     * 处理（只在客户端执行）
     */
    public static void receive(PacketWarpMessageS2C msg, Player player) {
        if (player.level().isClientSide) {
            ClientHandler.handle(msg);
        }
    }

    public static MessageType messageType;

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data);
        buf.writeByte(type);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {
            ClientHandler.handle(this);
        }
    }


    // ---------------- CLIENT LOGIC -------------------
    public static class ClientHandler {
        public static void handle(PacketWarpMessageS2C message) {
            if (message.data == 0) return;

            String text;
            int change = message.data;

            if (message.type == 0) { // NORMAL WARP
                text = change < 0 ?
                        StatCollector.translateToLocal("tc.removewarp") :
                        StatCollector.translateToLocal("tc.addwarp");

                if (change > 0)
                    playWhisper();
            } else if (message.type == 1) { // STICKY WARP
                text = change < 0 ?
                        StatCollector.translateToLocal("tc.removewarpsticky") :
                        StatCollector.translateToLocal("tc.addwarpsticky");

                if (change > 0)
                    playWhisper();
            } else { // TEMP WARP
                text = change < 0 ?
                        StatCollector.translateToLocal("tc.removewarptemp") :
                        StatCollector.translateToLocal("tc.addwarptemp");
            }

            PlayerNotifications.addNotification(text);
        }

        private static void playWhisper() {
            var mc = net.minecraft.client.Minecraft.getInstance();
            mc.player.playSound(
                    net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_RESONATE,
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
//public class PacketWarpMessageS2C implements IMessage, IMessageHandler<PacketWarpMessageS2C,IMessage> {
//   protected int data = 0;
//   protected byte type = 0;
//
//   public PacketWarpMessageS2C() {
//   }
//
//   public PacketWarpMessageS2C(Player player, byte type, int change) {
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
//   public IMessage onMessage(PacketWarpMessageS2C message, MessageContext ctx) {
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
