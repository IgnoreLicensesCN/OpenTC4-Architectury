package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.ThaumcraftSounds;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PacketResearchCompleteS2C extends BaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":research_complete";

    public static MessageType messageType;

    private String key;

    public PacketResearchCompleteS2C(){}
    public PacketResearchCompleteS2C(String key) {
        this.key = key;
    }

    // 编码
    public static void encode(PacketResearchCompleteS2C msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.key);
    }

    // 解码
    public static PacketResearchCompleteS2C decode(FriendlyByteBuf buf) {
        return new PacketResearchCompleteS2C(buf.readUtf());
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(this, buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer(); // 客户端玩家

        if (key == null || key.isEmpty()) return;

        Thaumcraft.researchManager.completeResearch(player, key);

        // clue
        if (key.startsWith("@")) {
            String text = String.valueOf(Component.translatable("tc.addclue"));
            PlayerNotifications.addNotification("§a" + text);
            player.playSound(ThaumcraftSounds.LEARN, 0.2F, 1.0F + player.getRandom().nextFloat() * 0.1F);
        } else if (!ResearchCategories.getResearch(key).isVirtual()) {
            ClientTickEventsFML.researchPopup.queueResearchInformation(ResearchCategories.getResearch(key));
            GuiResearchBrowser.highlightedItem.add(key);
            GuiResearchBrowser.highlightedItem.add(ResearchCategories.getResearch(key).category);
        }

        // GUI 更新
        if (Minecraft.getInstance().screen instanceof GuiResearchBrowser gui) {
            List<String> al = GuiResearchBrowser.completedResearch.get(player.getName().getString());
            if (al == null) al = new ArrayList<>();
            al.add(key);
            GuiResearchBrowser.completedResearch.put(player.getName().getString(), al);
            gui.updateResearch();
        }
    }
}
