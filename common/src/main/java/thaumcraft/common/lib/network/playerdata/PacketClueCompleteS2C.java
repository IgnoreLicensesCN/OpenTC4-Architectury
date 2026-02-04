package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.ThaumcraftSounds;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;

import java.util.ArrayList;
import java.util.List;

public class PacketClueCompleteS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":clue_complete";

    public static MessageType messageType;

    private ResourceLocation key;

    public PacketClueCompleteS2C(){}
    public PacketClueCompleteS2C(ResourceLocation key) {
        this.key = key;
    }

    // 编码
    public static void encode(PacketClueCompleteS2C msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.key);
    }

    // 解码
    public static PacketClueCompleteS2C decode(FriendlyByteBuf buf) {
        return new PacketClueCompleteS2C(buf.readResourceLocation());
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

        Thaumcraft.researchManager.completeClue(player, key);

        // clue

        String text = String.valueOf(Component.translatable("tc.addclue"));
        PlayerNotifications.addNotification("§a" + text);
        player.playSound(ThaumcraftSounds.LEARN, 0.2F, 1.0F + player.getRandom().nextFloat() * 0.1F);

        // GUI 更新
        if (Minecraft.getInstance().screen instanceof GuiResearchBrowser gui) {
            List<ResourceLocation> al = GuiResearchBrowser.completedClue.get(player.getGameProfile().getName());
            if (al == null) al = new ArrayList<>();
            al.add(key);
            GuiResearchBrowser.completedClue.put(player.getGameProfile().getName(), al);
            gui.updateResearch();
        }
    }
}
