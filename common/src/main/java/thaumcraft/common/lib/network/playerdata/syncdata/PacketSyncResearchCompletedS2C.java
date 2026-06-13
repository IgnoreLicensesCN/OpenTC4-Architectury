package thaumcraft.common.lib.network.playerdata.syncdata;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ResearchItemResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

import java.util.Collection;
import java.util.HashSet;

public class PacketSyncResearchCompletedS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_research";
    public static MessageType messageType;

    private final Collection<ResearchItemResourceLocation> data;

    public PacketSyncResearchCompletedS2C(ResearchAndScannedInfo data) {
        this.data = new HashSet<>(data.completedResearches);
    }
    public PacketSyncResearchCompletedS2C(Collection<ResearchItemResourceLocation> data) {
        this.data = new HashSet<>(data);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        for (var res : data) {
            buf.writeResourceLocation(res);
        }
    }

    public static PacketSyncResearchCompletedS2C decode(FriendlyByteBuf buf) {
        ResearchAndScannedInfo data = new ResearchAndScannedInfo();
        int i = buf.readInt();
        for (int j = 0; j < i; ++j) {
            data.completedResearches.add(ResearchItemResourceLocation.of(buf.readResourceLocation()));
        }

        return new PacketSyncResearchCompletedS2C(data);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            ResearchAndScannedInfo.getFromPlayer(player).syncResearchClientSide(data);
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
