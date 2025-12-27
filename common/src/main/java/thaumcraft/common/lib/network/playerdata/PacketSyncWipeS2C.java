package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

public class PacketSyncWipeS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_wipe";

    public PacketSyncWipeS2C() {
    }

    // ------------- ThaumcraftBaseS2CMessage 必须实现的 3 个方法 -------------

    public static PacketSyncWipeS2C decode(FriendlyByteBuf buf) {
        return new PacketSyncWipeS2C();
    }

    /**
     * 空包 → 不写任何东西
     */
    @Override
    public void write(FriendlyByteBuf buf) {
    }

    /**
     * 收到消息时执行
     */
    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {
            ClientHandler.handle();
        }
    }

    public static MessageType messageType;

    /**
     * 返回消息类型
     */
    @Override
    public MessageType getType() {
        return messageType;
    }

    // ---------------- CLIENT LOGIC -------------------
    public static class ClientHandler {
        public static void handle() {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            String name = player.getName().getString();
            Thaumcraft.playerKnowledge.wipePlayerKnowledge(name);
        }
    }
}
