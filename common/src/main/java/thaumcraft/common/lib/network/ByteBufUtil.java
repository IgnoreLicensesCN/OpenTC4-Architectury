package thaumcraft.common.lib.network;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectView;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static thaumcraft.api.aspects.Aspects.ASPECT_PALETTE_VIEW;
import static thaumcraft.common.lib.network.gamedata.PacketSyncItemAspectsS2C.CLIENT_PALETTE;

public class ByteBufUtil {
    public static final FriendlyByteBuf.Writer<Item> ITEM_WRITER = (buf,item) -> buf.writeId(BuiltInRegistries.ITEM, item);
    public static final FriendlyByteBuf.Reader<Item> ITEM_READER = buf -> buf.readById(BuiltInRegistries.ITEM);
    public static final FriendlyByteBuf.Writer<AspectList<Aspect>> ASPECT_LIST_WRITER = ByteBufUtil::writeAspectList;

    private static void writeAspectList(FriendlyByteBuf friendlyByteBuf, AspectList<Aspect> aspectList) {
        friendlyByteBuf.writeInt(aspectList.size());
        aspectList.forEach(
                (aspect, amount) -> {
                    writeAspect(friendlyByteBuf,aspect);
                    friendlyByteBuf.writeInt(amount);
                }
        );
    }

    public static final FriendlyByteBuf.Reader<AspectList<Aspect>> LINKED_ASPECT_LIST_READER =
            friendlyByteBuf ->
                    new LinkedHashAspectList<>(readAspectListAsObject2IntLinkedOpenHashMap(friendlyByteBuf));
    public static final FriendlyByteBuf.Reader<AspectList<Aspect>> LINKED_UNMODIFIABLE_ASPECT_LIST_READER =
            friendlyByteBuf ->
                    new UnmodifiableAspectView<>(readAspectListAsObject2IntLinkedOpenHashMap(friendlyByteBuf));
    public static final FriendlyByteBuf.Writer<Map<Item, AspectList<Aspect>>> ITEM_ASPECTS_MAP_WRITER_SERVER_PALETTE = (friendlyByteBuf, map) -> {
//        writeAspectPalette(friendlyByteBuf);
        friendlyByteBuf.writeVarInt(map.size());
        for (var entry:map.entrySet()) {
            ITEM_WRITER.accept(friendlyByteBuf, entry.getKey());
            var aspectList = entry.getValue();
            writeAspectListWithServerPalette(friendlyByteBuf, aspectList);
        }
    };
    public static final FriendlyByteBuf.Reader<Map<Item,AspectList<Aspect>>> ITEM_ASPECT_MAP_READER_CLIENT_PALETTE = friendlyByteBuf -> {
        int size = friendlyByteBuf.readVarInt();
        Map<Item, AspectList<Aspect>> result = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            var item = ITEM_READER.apply(friendlyByteBuf);
            var aspectList = readAspectListWithPalette(friendlyByteBuf, CLIENT_PALETTE);
            result.put(item, aspectList);
        }
        return result;
    };



    public static @NotNull Object2IntLinkedOpenHashMap<Aspect> readAspectListAsObject2IntLinkedOpenHashMap(FriendlyByteBuf friendlyByteBuf) {
        int size = friendlyByteBuf.readInt();
        Object2IntLinkedOpenHashMap<Aspect> resultInner = new Object2IntLinkedOpenHashMap<>(size);
        for (var _i=0;_i<size;_i++){
            var aspect = readAspect(friendlyByteBuf);
            resultInner.put(aspect, friendlyByteBuf.readInt());
        }
        return resultInner;
    }

    public static void writeAspect(FriendlyByteBuf friendlyByteBuf, Aspect aspect) {
        friendlyByteBuf.writeResourceLocation(aspect.aspectKey);
    }

    public static @NotNull Aspect readAspect(FriendlyByteBuf friendlyByteBuf) {
        var resLoc = friendlyByteBuf.readResourceLocation();
        return verifyAspectResLoc(resLoc);
    }
    public static void writeAspectPalette(FriendlyByteBuf buf){
        buf.writeVarInt(ASPECT_PALETTE_VIEW.size());
        for (var entry : ASPECT_PALETTE_VIEW.object2IntEntrySet()) {
            buf.writeResourceLocation(entry.getKey());
            buf.writeInt(entry.getIntValue());
        }
    }
    public static Int2ObjectMap<AspectResourceLocation> readAspectPalette(FriendlyByteBuf buf){
        int size = buf.readVarInt();
        Int2ObjectMap<AspectResourceLocation> result = new Int2ObjectOpenHashMap<>(size);
        for (int i = 0; i < size; i++) {
            var resLoc = buf.readResourceLocation();
            result.put(buf.readInt(),verifyAspectResLoc(resLoc).getAspectKey());
        }
        return result;
    }

    public static Aspect verifyAspectResLoc(ResourceLocation resLoc) {
        var aspect = Aspect.getAspect(AspectResourceLocation.ofExisting(resLoc));
        if (aspect == null){
            throw new IllegalArgumentException("Invalid aspect " + resLoc);
        }
        return aspect;
    }

    public static void writeAspectListWithServerPalette(FriendlyByteBuf friendlyByteBuf, AspectList<Aspect> aspectList) {
        friendlyByteBuf.writeVarInt(aspectList.size());
        aspectList.forEach(
                (aspect, amount) -> {
                    friendlyByteBuf.writeInt(ASPECT_PALETTE_VIEW.getInt(aspect));
                    friendlyByteBuf.writeInt(amount);
                }
        );
    }
    public static AspectList<Aspect> readAspectListWithPalette(FriendlyByteBuf friendlyByteBuf, Int2ObjectMap<AspectResourceLocation> palette) {
        int size = friendlyByteBuf.readVarInt();
        Object2IntLinkedOpenHashMap<Aspect> result = new Object2IntLinkedOpenHashMap<>(size);
        for (int i = 0; i < size; i++) {
            int id = friendlyByteBuf.readInt();
            int amount = friendlyByteBuf.readInt();
            result.put(Aspect.getAspect(palette.get(id)),amount);
        }
        return new UnmodifiableAspectView<>(result);
    }
}
