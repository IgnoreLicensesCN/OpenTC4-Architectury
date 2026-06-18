package thaumcraft.common.lib.network.playerdata.syncdata.scan;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.common.lib.resourcelocations.ScannedTypeResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


public abstract class AbstractPacketSyncScannedS2C extends ThaumcraftBaseS2CMessage {

    protected abstract ScannedTypeResourceLocation getScannedTypeToSync();

    private final Set<ResourceLocation> data;
    public AbstractPacketSyncScannedS2C(Set<ResourceLocation> data) {
        this.data = ConcurrentHashMap.newKeySet(data.size());
        this.data.addAll(data);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        for (var s : data) {
            buf.writeResourceLocation(s);
        }
    }

    public static <P extends AbstractPacketSyncScannedS2C> P decodeTo(FriendlyByteBuf buf, Function<Set<ResourceLocation>, P> constructor) {
        var size = buf.readInt();
        Set<ResourceLocation> data = ConcurrentHashMap.newKeySet(size);
        for (int i = 0; i < size; i++) {
            data.add(buf.readResourceLocation());
        }
        return constructor.apply(data);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {
            var info = ResearchAndScannedInfo.getFromLiving(player);
            info.syncScannedClientSide(getScannedTypeToSync(),data);
        }
    }

}
