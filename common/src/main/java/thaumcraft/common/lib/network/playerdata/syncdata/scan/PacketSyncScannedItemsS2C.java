package thaumcraft.common.lib.network.playerdata.syncdata.scan;

import net.minecraft.resources.ResourceLocation;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.api.scan.ThaumcraftScannedTypes;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;

import java.util.Set;

public class PacketSyncScannedItemsS2C extends AbstractPacketSyncScannedS2C {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_scanned_items";
    public static MessageType messageType;

    @Override
    protected ScannedTypeResourceLocation getScannedTypeToSync() {
        return ThaumcraftScannedTypes.ITEM;
    }

    public PacketSyncScannedItemsS2C(Set<ResourceLocation> data) {
        super(data);
    }

    public static PacketSyncScannedItemsS2C decode(FriendlyByteBuf buf) {
        return decodeTo(buf,PacketSyncScannedItemsS2C::new);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
