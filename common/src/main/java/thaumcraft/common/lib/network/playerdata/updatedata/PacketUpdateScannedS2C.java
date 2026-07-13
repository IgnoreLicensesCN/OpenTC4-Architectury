package thaumcraft.common.lib.network.playerdata.updatedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;

public class PacketUpdateScannedS2C extends BaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":update_scanned";
    public static MessageType messageType;

    private final ScannedTypeResourceLocation scannedType;
    private final ResourceLocation thingsScanned;
    public PacketUpdateScannedS2C(ScannedTypeResourceLocation scannedType,ResourceLocation thingsScanned){
        this.scannedType = scannedType;
        this.thingsScanned = thingsScanned;
    }
    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(scannedType);
        buf.writeResourceLocation(thingsScanned);
    }
    public static PacketUpdateScannedS2C read(FriendlyByteBuf buf) {
        return new PacketUpdateScannedS2C(ScannedTypeResourceLocation.of(buf.readResourceLocation()),buf.readResourceLocation());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        var player = context.getPlayer();
        if (player != null) {
            var researchInfo = ResearchAndScannedInfo.getFromLiving(player);
            researchInfo.addScannedForType(scannedType, thingsScanned);
        }
    }
}
