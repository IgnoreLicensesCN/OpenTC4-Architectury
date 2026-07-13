package thaumcraft.common.lib.network.playerdata.syncdata.scan;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.scan.ThaumcraftScannedTypes;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;

import java.util.Set;


public class PacketSyncScannedVisNodeS2C extends AbstractPacketSyncScannedS2C {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_scanned_vis_node";
    public static MessageType messageType;

    @Override
    protected ScannedTypeResourceLocation getScannedTypeToSync() {
        return ThaumcraftScannedTypes.VIS_NODE;
    }

    public PacketSyncScannedVisNodeS2C(Set<ResourceLocation> data) {
        super(data);
    }

    public static PacketSyncScannedVisNodeS2C decode(FriendlyByteBuf buf) {
        return decodeTo(buf, PacketSyncScannedVisNodeS2C::new);
    }
    @Override
    public MessageType getType() {
        return messageType;
    }
}
