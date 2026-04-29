package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncScannedPhenomenaS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_scanned_phenomena";
    public static MessageType messageType;

    public List<String> data;

    // ------------------ 构造 ------------------

    public PacketSyncScannedPhenomenaS2C(){}
    /**
     * 解码用构造
     */
    public PacketSyncScannedPhenomenaS2C(List<String> data) {
        this.data = data;
    }

    /**
     * 服务端发送用构造
     */
    public PacketSyncScannedPhenomenaS2C(Player player) {
        List<String> list = Thaumcraft.getScannedPhenomena().get(player.getGameProfile().getName());
        this.data = list != null ? list : new ArrayList<>();
    }

    // ------------------ Architectury 必要方法 ------------------

    /**
     * 编码
     */
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        for (String s : data) {
            buf.writeUtf(s);
        }
    }

    /**
     * 解码方法（注册时使用）
     */
    public static PacketSyncScannedPhenomenaS2C decode(FriendlyByteBuf buf) {
        var size = buf.readInt();
        List<String> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(buf.readUtf());
        }
        return new PacketSyncScannedPhenomenaS2C(list);
    }

    /**
     * 处理（S2C，只在客户端）
     */
    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {
            for (String key : data) {
                Thaumcraft.researchManager.completeScannedPhenomena(player, key);
            }
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
