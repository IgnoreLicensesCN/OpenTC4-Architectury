package thaumcraft.common.lib.network.playerdata.syncdata;

import com.linearity.opentc4.simpleutils.SimplePair;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;
import thaumcraft.common.researches.ResearchAndScannedInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketSyncAllScannedS2C extends ThaumcraftBaseS2CMessage {

    public static final String ID = Thaumcraft.MOD_ID + ":sync_all_scanned";
    private final Map<ScannedTypeResourceLocation, Set<ResourceLocation>> scanneds;
    public static MessageType messageType;

    public PacketSyncAllScannedS2C(Map<ScannedTypeResourceLocation, Set<ResourceLocation>> scanneds) {
        this.scanneds = new HashMap<>(scanneds);
    }

    public static void writeOne(FriendlyByteBuf buf, ScannedTypeResourceLocation scannedType, Set<ResourceLocation> scannedThings){
        buf.writeResourceLocation(scannedType);
        buf.writeInt(scannedThings.size());
        for (ResourceLocation thing : scannedThings) {
            buf.writeResourceLocation(thing);
        }
    }

    public static SimplePair<ScannedTypeResourceLocation, Set<ResourceLocation>> readOne(FriendlyByteBuf buf) {
        var type = ScannedTypeResourceLocation.of(buf.readResourceLocation());
        int size = buf.readInt();
        Set<ResourceLocation> things = ConcurrentHashMap.newKeySet();
        for (int i = 0; i < size; i++) {
            things.add(buf.readResourceLocation());
        }
        return new SimplePair<>(type,things);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(scanneds.size());
        for (Map.Entry<ScannedTypeResourceLocation, Set<ResourceLocation>> entry : scanneds.entrySet()) {
            writeOne(buf,entry.getKey(),entry.getValue());
        }
    }

    public static PacketSyncAllScannedS2C read(FriendlyByteBuf buf) {
        int types = buf.readInt();
        Map<ScannedTypeResourceLocation, Set<ResourceLocation>> scanneds = new ConcurrentHashMap<>(types);
        for (int i=0;i<types;i++) {
            var scanned = readOne(buf);
            scanneds.put(scanned.a(),scanned.b());
        }
        return new PacketSyncAllScannedS2C(scanneds);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        var player = context.getPlayer();
        if (player != null) {
            var info = ResearchAndScannedInfo.getFromPlayer(player);
            info.syncScannedClientSide(scanneds);
        }
    }
}
