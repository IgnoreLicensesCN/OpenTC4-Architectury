package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncResearchS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_research";
    public static MessageType messageType;

    public List<String> data;

    // ---------------- 构造 ----------------

    public PacketSyncResearchS2C(){}
    /**
     * 服务端发送用构造
     */
    public PacketSyncResearchS2C(Player player) {
        List<String> list = ResearchManager.getResearchForPlayer(player.getName().getString());
        this.data = list != null ? list : new ArrayList<>();
    }

    /**
     * 解码用构造
     */
    public PacketSyncResearchS2C(List<String> data) {
        this.data = data;
    }

    // ---------------- Architectury 必要方法 ----------------

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeShort(data.size());
        for (String s : data) {
            buf.writeUtf(s);
        }
    }

    public static PacketSyncResearchS2C decode(FriendlyByteBuf buf) {
        short size = buf.readShort();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(buf.readUtf());
        }
        return new PacketSyncResearchS2C(list);
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

    // ---------------- 客户端逻辑 ----------------

    public static class ClientHandler {
        public static void handle(PacketSyncResearchS2C msg) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            for (String key : msg.data) {
                Thaumcraft.researchManager.completeResearch(player, key);
            }

            GuiResearchBrowser.completedResearch.put(player.getName().getString(), msg.data);
        }
    }
}
