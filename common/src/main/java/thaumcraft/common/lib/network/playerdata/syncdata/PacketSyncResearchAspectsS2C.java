package thaumcraft.common.lib.network.playerdata.syncdata;

import dev.architectury.networking.NetworkManager;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import thaumcraft.api.aspects.aspectlists.baseimpl.ArrayAspectList;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.api.research.ResearchAndScannedInfo;

public class PacketSyncResearchAspectsS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_aspects";
    public static MessageType messageType;

    public AspectList<Aspect> data;

    public PacketSyncResearchAspectsS2C(AspectList<Aspect> data) {
        this.data = data;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        data.forEach((aspect,amount) -> {
            buf.writeResourceLocation(aspect.aspectKey);
            buf.writeInt(amount);
        });
    }

    public static PacketSyncResearchAspectsS2C decode(FriendlyByteBuf buf) {
        int mapSize = buf.readInt();
        Object2IntArrayMap<Aspect> dataMap = new Object2IntArrayMap<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            var aspLoc = buf.readResourceLocation();
            var aspect = Aspect.getAspect(AspectResourceLocation.of(aspLoc));
            if (aspect == null) {
                throw new IllegalArgumentException("Invalid aspect resource location: " + aspLoc);
            }
            dataMap.put(aspect, buf.readInt());
        }
        return new PacketSyncResearchAspectsS2C(ArrayAspectList.viewOf(dataMap));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        var player = context.getPlayer();
        if (player != null) {
            ResearchAndScannedInfo.getFromLiving(player).syncResearchAspectClientSide(data);
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
