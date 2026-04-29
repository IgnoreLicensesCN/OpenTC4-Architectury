package thaumcraft.common.lib.network.playerdata;


import dev.architectury.networking.NetworkManager;
import net.minecraft.network.chat.Component;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;

public class PacketWarpMessageS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":warp_message";

    public final int data;
    public final byte type;

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

    public static void encode(PacketWarpMessageS2C msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.data);
        buf.writeByte(msg.type);
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
            if (data == 0) return;

            Component text;
            int change = data;

            if (type == 0) { // NORMAL WARP
                text = change < 0 ?
                        Component.translatable("tc.removewarp") :
                        Component.translatable("tc.addwarp");

                if (change > 0) {
                    player.playSound(
                            ThaumcraftSounds.WHISPERS,
                            0.5F, 1.0F
                    );
                }
            } else if (type == 1) { // STICKY WARP
                text = change < 0 ?
                        Component.translatable("tc.removewarpsticky") :
                        Component.translatable("tc.addwarpsticky");

                if (change > 0){
                    player.playSound(
                            ThaumcraftSounds.WHISPERS,
                            0.5F, 1.0F
                    );
                }
            } else { // TEMP WARP
                text = change < 0 ?
                        Component.translatable("tc.removewarptemp") :
                        Component.translatable("tc.addwarptemp");
            }

            PlayerNotifications.addNotification(text);
        }
    }


}