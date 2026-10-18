package thaumcraft.common.lib.network.gamedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import static thaumcraft.api.aspects.Aspects.ASPECT_PALETTE_VIEW;
import static thaumcraft.common.lib.network.ByteBufUtil.*;


public class PacketSyncItemAspectsS2C extends BaseS2CMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":sync_item_aspects_result";

    @Override
    public MessageType getType() {
        return messageType;
    }

//    private final Map<Item, AspectList<Aspect>> syncResult;
    public Int2ObjectMap<AspectResourceLocation> palette;
    public PacketSyncItemAspectsS2C() {
        this.palette = new Int2ObjectOpenHashMap<>();
        ASPECT_PALETTE_VIEW.forEach((key, value) -> {
            palette.put(value, key);
        });
    }

    public PacketSyncItemAspectsS2C(Int2ObjectMap<AspectResourceLocation> palette) {
        this.palette = palette;
    }
    @Override
    public void write(FriendlyByteBuf bufOuter) {
        writeAspectPalette(bufOuter);
//        ITEM_ASPECTS_MAP_WRITER.accept(bufOuter, syncResult);
    }

    public static Int2ObjectMap<AspectResourceLocation> CLIENT_PALETTE = new Int2ObjectOpenHashMap<>();

    public static PacketSyncItemAspectsS2C decode(FriendlyByteBuf bufOuter) {
        return new PacketSyncItemAspectsS2C(readAspectPalette(bufOuter));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ItemBasicAspectGetter.CLIENT_CACHE.clear();//maybe not safe?especially for udp.
        CLIENT_PALETTE = palette;
    }
}
