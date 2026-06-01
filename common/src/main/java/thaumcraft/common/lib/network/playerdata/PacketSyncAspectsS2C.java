package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedHashAspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

public class PacketSyncAspectsS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_aspects";
    public static MessageType messageType;

    public AspectList<Aspect> data;

    public PacketSyncAspectsS2C(Player player) {
        this.data = Thaumcraft.playerKnowledge.getAspectsDiscovered(player);
    }

    public PacketSyncAspectsS2C(AspectList<Aspect> data) {
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

    public static PacketSyncAspectsS2C decode(FriendlyByteBuf buf) {
        int mapSize = buf.readInt();
        Object2IntLinkedOpenHashMap<Aspect> dataMap = new Object2IntLinkedOpenHashMap<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            var aspLoc = buf.readResourceLocation();
            var aspect = Aspect.getAspect(AspectResourceLocation.of(aspLoc));
            if (aspect == null) {
                throw new IllegalArgumentException("Invalid aspect resource location: " + aspLoc);
            }
            dataMap.put(aspect, buf.readInt());
        }
        return new PacketSyncAspectsS2C(LinkedHashAspectList.viewOf(dataMap));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        data.forEach(
                (asp,amount) ->Thaumcraft.researchManager.completeAspect(
                        context.getPlayer(), asp, amount)
        );
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
