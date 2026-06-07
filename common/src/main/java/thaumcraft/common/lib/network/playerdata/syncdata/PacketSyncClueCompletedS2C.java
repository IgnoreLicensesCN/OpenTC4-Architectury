package thaumcraft.common.lib.network.playerdata.syncdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.client.gui.GuiResearchBrowser;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

import java.util.Collection;
import java.util.HashSet;

public class PacketSyncClueCompletedS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_research";
    public static MessageType messageType;

    public Collection<ClueResourceLocation> data;


    public PacketSyncClueCompletedS2C(ResearchAndScannedInfo data) {
        this.data = new HashSet<>(data.completedClues);
    }
    public PacketSyncClueCompletedS2C(Collection<ClueResourceLocation> data) {
        this.data = data;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeCollection(data, FriendlyByteBuf::writeResourceLocation);
    }

    public static PacketSyncClueCompletedS2C decode(FriendlyByteBuf buf) {
        return new PacketSyncClueCompletedS2C(buf.readList(
                friendlyByteBuf -> ClueResourceLocation.of(friendlyByteBuf.readResourceLocation()))
        );
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            ResearchAndScannedInfo.getFromPlayer(player).syncClueClientSide(this.data);
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
