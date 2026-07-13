package thaumcraft.common.lib.network.playerdata.syncdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.common.lib.resourcelocations.ClueResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

import java.util.Collection;

public class PacketSyncClueCompletedS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_clue";
    public static MessageType messageType;

    public Collection<ClueResourceLocation> data;

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
            ResearchAndScannedInfo.getFromLiving(player).syncClueClientSide(this.data);
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
