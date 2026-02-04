package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import thaumcraft.common.lib.research.ResearchManager;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncClueS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_research";
    public static MessageType messageType;

    public List<ResourceLocation> data;

    // ---------------- 构造 ----------------

    public PacketSyncClueS2C(){}
    /**
     * 服务端发送用构造
     */
    public PacketSyncClueS2C(Player player) {
        List<ResourceLocation> list = ResearchManager.getResearchForPlayer(player.getGameProfile().getName());
        this.data = list != null ? list : new ArrayList<>();
    }

    /**
     * 解码用构造
     */
    public PacketSyncClueS2C(List<ResourceLocation> data) {
        this.data = data;
    }

    // ---------------- Architectury 必要方法 ----------------

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        for (ResourceLocation s : data) {
            buf.writeResourceLocation(s);
        }
    }

    public static PacketSyncClueS2C decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<ResourceLocation> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(buf.readResourceLocation());
        }
        return new PacketSyncClueS2C(list);
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
        public static void handle(PacketSyncClueS2C msg) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            for (var key : msg.data) {
                Thaumcraft.researchManager.completeClue(player, key);
            }

            GuiResearchBrowser.completedClue.put(player.getGameProfile().getName(), msg.data);
        }
    }
}
