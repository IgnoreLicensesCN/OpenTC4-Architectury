package thaumcraft.common.lib.network.gamedata;

import com.google.common.collect.MapMaker;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.common.Thaumcraft;

import java.util.Map;

public class PacketSyncItemAspectsC2S extends BaseC2SMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":require_sync_item_aspects";
    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    public static PacketSyncItemAspectsC2S decode(FriendlyByteBuf buf) {
        return new PacketSyncItemAspectsC2S();
    }

    private static final Map<ServerPlayer,Integer> PLAYER_PACKET_COOLDOWN = new MapMaker().weakKeys().makeMap();
    public static final int PACKET_COOLDOWN = 20*5;
    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            var nextRun = PLAYER_PACKET_COOLDOWN.getOrDefault(serverPlayer,0);
            if (serverPlayer.tickCount >= nextRun) {
                new PacketSyncItemAspectsS2C().sendTo(serverPlayer);
                PLAYER_PACKET_COOLDOWN.put(serverPlayer,serverPlayer.tickCount + PACKET_COOLDOWN);
            }
        }
    }
}
