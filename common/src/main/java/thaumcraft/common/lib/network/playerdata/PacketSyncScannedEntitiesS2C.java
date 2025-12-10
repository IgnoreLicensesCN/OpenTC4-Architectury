package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncScannedEntitiesS2C extends BaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_scanned_entities";
    public static MessageType messageType;

    public final List<String> data;

    // ------------------ 构造 ------------------

    /**
     * 服务端发送用构造
     */
    public PacketSyncScannedEntitiesS2C(Player player) {
        List<String> list = Thaumcraft.getScannedEntities().get(player.getName().getString());
        this.data = list != null ? list : new ArrayList<>();
    }

    /**
     * 解码用构造
     */
    public PacketSyncScannedEntitiesS2C(List<String> data) {
        this.data = data;
    }

    // ------------------ Architectury 必要方法 ------------------

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeShort(data.size());
        for (String s : data) {
            buf.writeUtf(s);
        }
    }

    public static PacketSyncScannedEntitiesS2C decode(FriendlyByteBuf buf) {
        short size = buf.readShort();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(buf.readUtf());
        }
        return new PacketSyncScannedEntitiesS2C(list);
    }

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

    // ------------------ 客户端逻辑 ------------------

    public static class ClientHandler {
        public static void handle(PacketSyncScannedEntitiesS2C msg) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            for (String key : msg.data) {
                Thaumcraft.researchManager.completeScannedEntity(player.getName().getString(), key);
            }
        }
    }
}
