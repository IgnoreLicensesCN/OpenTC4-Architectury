package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncScannedItemsS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_scanned_items";
    public static MessageType messageType;

    public List<String> data;

    // ------------------ 构造 ------------------

    public PacketSyncScannedItemsS2C(){}
    /**
     * 解码用构造
     */
    public PacketSyncScannedItemsS2C(List<String> data) {
        this.data = data;
    }

    /**
     * 服务端发送用构造
     */
    public PacketSyncScannedItemsS2C(Player player) {
        List<String> list = Thaumcraft.getScannedObjects().get(player.getName().getString());
        this.data = list != null ? list : new ArrayList<>();
    }

    // ------------------ Architectury 必要方法 ------------------

    /**
     * 编码
     */
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeShort(data.size());
        for (String s : data) {
            buf.writeUtf(s);
        }
    }

    /**
     * 解码方法（注册时使用）
     */
    public static PacketSyncScannedItemsS2C decode(FriendlyByteBuf buf) {
        short size = buf.readShort();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(buf.readUtf());
        }
        return new PacketSyncScannedItemsS2C(list);
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

    // ------------------ 客户端逻辑 ------------------

    public static class ClientHandler {
        public static void handle(PacketSyncScannedItemsS2C msg) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            for (String key : msg.data) {
                Thaumcraft.researchManager.completeScannedObject(player.getName().getString(), key);
            }
        }
    }
}
