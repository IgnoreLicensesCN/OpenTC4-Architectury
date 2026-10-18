package thaumcraft.common.lib.network.gamedata;

import com.google.common.collect.MapMaker;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketRequestSyncItemAspectsC2S extends BaseC2SMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":require_sync_item_aspects";
    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {

    }

    public static PacketRequestSyncItemAspectsC2S decode(FriendlyByteBuf buf) {
        return new PacketRequestSyncItemAspectsC2S();
    }

    private static final Map<ServerPlayer,Integer> PLAYER_PACKET_COOLDOWN = new MapMaker().weakKeys().makeMap();
    public static final Map<Item, AspectList<Aspect>> resultToSync = new HashMap<>();
    public static final List<Map<Item, AspectList<Aspect>>> resultToSyncSplit = new ArrayList<>();
    public static final int SPLIT_SIZE = 1000;

    public static synchronized void checkAndRebuildResultToSync() {
        if (resultToSync.isEmpty()) {
            BuiltInRegistries.ITEM.forEach(
                    i -> {
                        var value = ItemBasicAspectGetter.getBasicAspectsServer(i);
                        if (!value.isEmpty()) {
                            resultToSync.put(i, value);
                        }
                    }
            );
            splitResultToSync();
        }
    }
    public static synchronized void splitResultToSync() {
        resultToSyncSplit.clear();
        Map<Item, AspectList<Aspect>> mapSplit = new HashMap<>();
        for (var entry:resultToSync.entrySet()) {
            if (mapSplit.size() == SPLIT_SIZE) {
                resultToSyncSplit.add(mapSplit);
                mapSplit = new HashMap<>();
            }
            mapSplit.put(entry.getKey(), entry.getValue());
        }
        if (!mapSplit.isEmpty()){
            resultToSyncSplit.add(mapSplit);
        }
    }
    public static final int PACKET_COOLDOWN = 20*30;
    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            var nextRun = PLAYER_PACKET_COOLDOWN.getOrDefault(serverPlayer,0);
            if (serverPlayer.tickCount >= nextRun) {
                checkAndRebuildResultToSync();
                new PacketSyncItemAspectsS2C().sendTo(serverPlayer);
                for (var splitPart:resultToSyncSplit){
                    new PacketAddItemAspectsS2C(splitPart).sendTo(serverPlayer);
                }
                PLAYER_PACKET_COOLDOWN.put(serverPlayer,serverPlayer.tickCount + PACKET_COOLDOWN);
            }
        }
    }
}
