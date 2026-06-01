package thaumcraft.common.lib.network.gamedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.LinkedTreeAspectList;
import thaumcraft.api.aspects.aspectlists.UnmodifiableAspectView;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter.REQUESTED_ASPECT_LIST;

public class PacketSyncItemAspectsS2C extends BaseS2CMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":sync_item_aspects_result";

    @Override
    public MessageType getType() {
        return messageType;
    }

    private final Map<Item, AspectList<Aspect>> syncResult;

    public PacketSyncItemAspectsS2C() {
        Map<Item, AspectList<Aspect>> result = new HashMap<>();
        BuiltInRegistries.ITEM.forEach(
                i -> result.put(i,ItemBasicAspectGetter.getBasicAspectsServer(i))
        );
        syncResult = result;
    }
    public PacketSyncItemAspectsS2C(
            Map<Item, AspectList<Aspect>> syncResult
    ) {
        this.syncResult = syncResult;
    }

    @Override
    public void write(FriendlyByteBuf bufOuter) {
        bufOuter.writeMap(
                syncResult,
                (friendlyByteBuf, item) -> bufOuter.writeId(BuiltInRegistries.ITEM, item),
                (bufForAspList, aspectUnmodifiableAspectList) -> {
                    bufForAspList.writeInt(aspectUnmodifiableAspectList.size());
                    aspectUnmodifiableAspectList.forEach(
                            (aspect,amount) -> {
                                bufForAspList.writeResourceLocation(aspect.aspectKey);
                                bufForAspList.writeInt(amount);
                            }
                    );
                }
        );
    }

    public static PacketSyncItemAspectsS2C decode(FriendlyByteBuf bufOuter) {
        REQUESTED_ASPECT_LIST.set(true);
        return new PacketSyncItemAspectsS2C(
                bufOuter.readMap(
                        bufForItem -> bufForItem.readById(BuiltInRegistries.ITEM),
                        bufForAspList -> {
                            int mapSize = bufForAspList.readInt();
                            Object2IntLinkedOpenHashMap<Aspect> aspForItem = new Object2IntLinkedOpenHashMap<>(mapSize,1);
                            for (int i = 0; i < mapSize; i++) {
                                var aspectResLoc = bufForAspList.readResourceLocation();
                                var aspect = Aspect.getAspect(AspectResourceLocation.of(aspectResLoc));
                                if (aspect == null) {
                                    throw new IllegalArgumentException("Invalid aspect resource location: " + aspectResLoc);
                                }
                                aspForItem.put(aspect, bufForAspList.readInt());
                            }
                            return new UnmodifiableAspectView<>(aspForItem);
                        }
                    )
        );

    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ItemBasicAspectGetter.CLIENT_CACHE.clear();
        ItemBasicAspectGetter.CLIENT_CACHE.putAll(syncResult);
        REQUESTED_ASPECT_LIST.set(false);
    }
}
