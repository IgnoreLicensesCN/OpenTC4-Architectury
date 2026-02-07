package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.api.research.ResearchCategory;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PacketResearchCompleteS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":research_complete";

    public static MessageType messageType;

    private ResearchItemResourceLocation key;

    public PacketResearchCompleteS2C(){}
    public PacketResearchCompleteS2C(ResearchItemResourceLocation key) {
        this.key = key;
    }

    // 编码
    public static void encode(PacketResearchCompleteS2C msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.key);
    }

    // 解码
    public static PacketResearchCompleteS2C decode(FriendlyByteBuf buf) {
        return new PacketResearchCompleteS2C(ResearchItemResourceLocation.of(buf.readResourceLocation()));
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

        Thaumcraft.researchManager.completeResearch(player, key);

        // clue
        if (!ResearchItem.getResearch(key).isVirtual()) {
            ClientTickEventsFML.researchPopup.queueResearchInformation(ResearchItem.getResearch(key));
            GuiResearchBrowser.highlightedResearch.add(key);
            for (var category: ResearchCategory.researchCategories.values()) {
                if (category.researches.containsKey(key)) {
                    GuiResearchBrowser.highlightedCategory.add(category.categoryKey);
                }
            }
        }

        // GUI 更新
        if (Minecraft.getInstance().screen instanceof GuiResearchBrowser gui) {
            List<ResearchItemResourceLocation> al = GuiResearchBrowser.completedResearch.get(player.getGameProfile().getName());
            if (al == null) al = new ArrayList<>();
            al.add(key);
            GuiResearchBrowser.completedResearch.put(player.getGameProfile().getName(), al);
            gui.updateResearch();
        }
    }
}
