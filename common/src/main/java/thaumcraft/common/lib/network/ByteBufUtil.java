package thaumcraft.common.lib.network;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.aspects.aspectlists.baseimpl.LinkedHashAspectList;
import thaumcraft.api.aspects.aspectlists.unmodifiable.UnmodifiableAspectView;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.lib.resourcelocations.VariedResourceLocation;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static thaumcraft.api.aspects.Aspects.*;
import static thaumcraft.common.lib.network.gamedata.PacketSyncPaletteS2C.*;
import static thaumcraft.common.runicshield.ThaumcraftRunicShieldTypes.*;

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
//        writeAspectReadPalette(friendlyByteBuf);
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
            var aspectList = readAspectListWithPalette(friendlyByteBuf, CLIENT_READ_ASPECT_PALETTE);
            result.put(item, aspectList);
        }
        return result;
    };
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



    public static @NotNull Object2IntLinkedOpenHashMap<Aspect> readAspectListAsObject2IntLinkedOpenHashMap(FriendlyByteBuf friendlyByteBuf) {
        int size = friendlyByteBuf.readInt();
        Object2IntLinkedOpenHashMap<Aspect> resultInner = new Object2IntLinkedOpenHashMap<>(size);
        for (var _i=0;_i<size;_i++){
            var aspect = readAspect(friendlyByteBuf);
            resultInner.put(aspect, friendlyByteBuf.readInt());
        }
        return resultInner;
    }

    public static void writeRunicShield(FriendlyByteBuf buf, AbstractRunicShieldType<?> runicShieldType){
        writeVariedResLocReferredViaPalette(
                buf,
                runicShieldType,
                RUNIC_SHIELD_WRITE_PALETTE_VIEW,
                CLIENT_WRITE_RUNIC_SHIELD_PALETTE,
                r -> r.id
        );
    }

    public static void writeAspect(FriendlyByteBuf buf, Aspect aspect) {
        writeVariedResLocReferredViaPalette(
                buf,
                aspect,
                ASPECT_WRITE_PALETTE_VIEW,
                CLIENT_WRITE_ASPECT_PALETTE,
                Aspect::getAspectKey
        );
    }
    public static @NotNull AbstractRunicShieldType<?> readRunicShield(FriendlyByteBuf buf) {
        return readVariedResLocReferredViaPalette(
                buf,
                RUNIC_SHIELD_READ_PALETTE_VIEW,
                CLIENT_READ_RUNIC_SHIELD_PALETTE,
                RunicShieldTypeResourceLocation::ofExisting,
                RUNIC_SHIELD_TYPES_VIEW
        );
    }

    public static @NotNull Aspect readAspect(FriendlyByteBuf buf) {
        return readVariedResLocReferredViaPalette(
                buf,
                ASPECT_READ_PALETTE_VIEW,
                CLIENT_READ_ASPECT_PALETTE,
                AspectResourceLocation::ofExisting,
                ALL_ASPECTS
        );
    }

    public static void writeAspectListWithServerPalette(FriendlyByteBuf friendlyByteBuf, AspectList<Aspect> aspectList) {
        friendlyByteBuf.writeVarInt(aspectList.size());
        aspectList.forEach(
                (aspect, amount) -> {
                    friendlyByteBuf.writeInt(ASPECT_WRITE_PALETTE_VIEW.getInt(aspect));
                    friendlyByteBuf.writeInt(amount);
                }
        );
    }

    public static <O,R extends VariedResourceLocation<O,R>> void writeVariedResLocBasedMap(
            FriendlyByteBuf buf,
            Object2IntMap<O> toWrite,
            BiConsumer<FriendlyByteBuf,O> keyWriter
    ){
        buf.writeInt(toWrite.size());
        for (var entry : toWrite.object2IntEntrySet()) {
            keyWriter.accept(buf,entry.getKey());
            buf.writeInt(entry.getIntValue());
        }
    }
    public static <O,R extends VariedResourceLocation<O,R>> Object2IntMap<O> readVariedResLocBasedMap(
            FriendlyByteBuf buf,
            Function<FriendlyByteBuf,O> keyReader
    ) {
        int size = buf.readInt();
        Object2IntMap<O> decoded = new Object2IntOpenHashMap<>(size);
        for (int i = 0; i < size; i++) {
            var key = keyReader.apply(buf);
            var value = buf.readInt();
            decoded.put(key,value);
        }
        return decoded;
    }
    public static <O,R extends VariedResourceLocation<O,R>> void writeVariedResLocReferredViaPalette(
            FriendlyByteBuf buf,
            O toWrite,
            Object2IntMap<R> serverPalette,
            Object2IntMap<R> clientPalette,
            Function<O,R> resLocGetter
    ) {
        if (Platform.getEnvironment() == Env.SERVER) {
            buf.writeInt(serverPalette.getInt(resLocGetter.apply(toWrite)));
        }else {
            buf.writeInt(clientPalette.getInt(resLocGetter.apply(toWrite)));
        }
    }
    public static <O,R extends VariedResourceLocation<O,R>> @NotNull O readVariedResLocReferredViaPalette(
            FriendlyByteBuf buf,
            Int2ObjectMap<R> serverPalette,
            Int2ObjectMap<R> clientPalette,
            Function<ResourceLocation,@Nullable R> variedResLocFormer,
            Map<R,O> fetchObjectFrom
    ){
        var paletteValue = buf.readInt();
        R resLoc = null;
        if (Platform.getEnvironment() == Env.SERVER) {
            resLoc = serverPalette.get(paletteValue);
        }else {
            resLoc = clientPalette.get(paletteValue);
        }
        return verifyVariedResLocAndGetReferredObject(variedResLocFormer, fetchObjectFrom, resLoc);
    }

    public static <O, R extends VariedResourceLocation<O,R>> @NotNull O verifyVariedResLocAndGetReferredObject(
            Function<ResourceLocation, @Nullable R> variedResLocFormer,
            Map<R, O> fetchObjectFrom,
            R resLoc
    ) {
        var referredObject = fetchObjectFrom.get(variedResLocFormer.apply(resLoc));
        if (referredObject == null){
            throw new IllegalArgumentException("Invalid referredObject " + resLoc);
        }
        return referredObject;
    }

    public static void writeRunicShieldsMap(FriendlyByteBuf buf, Object2IntMap<AbstractRunicShieldType<?>> shields) {
        writeVariedResLocBasedMap(buf,shields,ByteBufUtil::writeRunicShield);
    }
    public static Object2IntMap<AbstractRunicShieldType<?>> readRunicShieldsMap(FriendlyByteBuf buf) {
        return readVariedResLocBasedMap(buf,ByteBufUtil::readRunicShield);
    }
}
