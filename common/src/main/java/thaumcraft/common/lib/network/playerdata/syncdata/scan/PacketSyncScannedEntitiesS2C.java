package thaumcraft.common.lib.network.playerdata.syncdata.scan;

import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.scan.ThaumcraftScannedTypes;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;

import java.util.List;
import java.util.Set;

public class PacketSyncScannedEntitiesS2C extends AbstractPacketSyncScannedS2C {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_scanned_entities";
    public static MessageType messageType;

    public List<String> data;

    protected ScannedTypeResourceLocation getScannedTypeToSync() {
        return ThaumcraftScannedTypes.ENTITY;
    }
    public PacketSyncScannedEntitiesS2C(Set<ResourceLocation> data) {
        super(data);
    }

    public static PacketSyncScannedEntitiesS2C decode(FriendlyByteBuf buf) {
        return decodeTo(buf, PacketSyncScannedEntitiesS2C::new);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
