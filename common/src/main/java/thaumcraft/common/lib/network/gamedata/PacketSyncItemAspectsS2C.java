package thaumcraft.common.lib.network.gamedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.UnmodifiableAspectList;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static com.linearity.opentc4.OpenTC4.platformUtils;

public class PacketSyncItemAspectsS2C extends BaseS2CMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":sync_item_aspects_result";

    @Override
    public MessageType getType() {
        return messageType;
    }

    private final Map<Item, UnmodifiableAspectList<Aspect>> syncResult;

    public PacketSyncItemAspectsS2C() {
        Map<Item, UnmodifiableAspectList<Aspect>> result = new HashMap<>();
        BuiltInRegistries.ITEM.forEach(
                i -> result.put(i,ItemBasicAspectGetter.getBasicAspects(i))
        );
        syncResult = result;
    }
    public PacketSyncItemAspectsS2C(
            Map<Item, UnmodifiableAspectList<Aspect>> syncResult
    ) {
        this.syncResult = syncResult;
    }

    @Override
    public void write(FriendlyByteBuf bufOuter) {
        bufOuter.writeMap(
                syncResult,
                (friendlyByteBuf, item) -> bufOuter.writeId(BuiltInRegistries.ITEM, item),
                (bufForAspList, aspectUnmodifiableAspectList) ->
                        bufForAspList.writeMap(
                                aspectUnmodifiableAspectList.aspectView,
                                (bufForAsp, aspect) -> bufForAsp.writeResourceLocation(
                                        bufForAsp.readResourceLocation()),
                                FriendlyByteBuf::writeInt
                        )
        );
    }

    public static PacketSyncItemAspectsS2C decode(FriendlyByteBuf bufOuter) {
        return new PacketSyncItemAspectsS2C(
                bufOuter.readMap(
                        bufForItem -> bufForItem.readById(BuiltInRegistries.ITEM),
                        bufForAspList -> new UnmodifiableAspectList<>(
                                bufForAspList.readMap(
                                        bufForAspect -> Aspect.getAspect(AspectResourceLocation.of(bufForAspect.readResourceLocation())),
                                        FriendlyByteBuf::readInt
                                        )
                        )
                )
        );
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ItemBasicAspectGetter.CLIENT_CACHE.clear();
        ItemBasicAspectGetter.CLIENT_CACHE.putAll(syncResult);
    }
}
