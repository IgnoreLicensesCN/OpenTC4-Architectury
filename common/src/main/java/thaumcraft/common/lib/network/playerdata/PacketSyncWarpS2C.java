package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

public class PacketSyncWarpS2C extends BaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_warp";
    public static MessageType messageType;

    public final int data;
    public final byte type;

    /**
     * 解码用构造
     */
    public PacketSyncWarpS2C(int data, byte type) {
        this.data = data;
        this.type = type;
    }

    /**
     * 服务端发送用构造
     */
    public PacketSyncWarpS2C(Player player, byte type) {
        String name = player.getName().getString();

        if (type == 0)
            this.data = Thaumcraft.playerKnowledge.getWarpPerm(name);
        else if (type == 1)
            this.data = Thaumcraft.playerKnowledge.getWarpSticky(name);
        else
            this.data = Thaumcraft.playerKnowledge.getWarpTemp(name);

        this.type = type;
    }

    // ------------------------ Architectury 必要方法 ------------------------

    /**
     * 编码
     */
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data);
        buf.writeByte(type);
    }

    /**
     * 解码方法（注册时使用）
     */
    public static PacketSyncWarpS2C decode(FriendlyByteBuf buf) {
        int data = buf.readInt();
        byte type = buf.readByte();
        return new PacketSyncWarpS2C(data, type);
    }

    /**
     * 处理（S2C，只在客户端）
     */
    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {
            ClientHandler.handle(this);
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    // ------------------------ 客户端逻辑 ------------------------

    public static class ClientHandler {
        public static void handle(PacketSyncWarpS2C msg) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            String name = player.getName().getString();

            if (msg.type == 0) {
                Thaumcraft.playerKnowledge.setWarpPerm(name, msg.data);
            } else if (msg.type == 1) {
                Thaumcraft.playerKnowledge.setWarpSticky(name, msg.data);
            } else {
                Thaumcraft.playerKnowledge.setWarpTemp(name, msg.data);
            }
        }
    }
}
