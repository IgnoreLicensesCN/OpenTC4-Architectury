package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncClueS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_research";
    public static MessageType messageType;

    public List<ClueResourceLocation> data;
    /**
     * 解码用构造
     */
    public PacketSyncClueS2C(List<ClueResourceLocation> data) {
        this.data = data;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(data, FriendlyByteBuf::writeResourceLocation);
    }

    public static PacketSyncClueS2C decode(FriendlyByteBuf buf) {
        return new PacketSyncClueS2C(buf.readList(friendlyByteBuf -> ClueResourceLocation.of(friendlyByteBuf.readResourceLocation())));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {

            for (var key : data) {
                Thaumcraft.researchManager.completeClue(player, key);
            }

            GuiResearchBrowser.completedClue.put(player.getGameProfile().getName(), data);
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
