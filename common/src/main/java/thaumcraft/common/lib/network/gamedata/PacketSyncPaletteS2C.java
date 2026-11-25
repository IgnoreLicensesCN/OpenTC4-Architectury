package thaumcraft.common.lib.network.gamedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.*;

import static thaumcraft.api.aspects.Aspects.ASPECT_READ_PALETTE_VIEW;
import static thaumcraft.api.aspects.Aspects.ASPECT_WRITE_PALETTE_VIEW;
import static thaumcraft.common.runicshield.ThaumcraftRunicShieldTypes.RUNIC_SHIELD_READ_PALETTE_VIEW;
import static thaumcraft.common.runicshield.ThaumcraftRunicShieldTypes.RUNIC_SHIELD_WRITE_PALETTE_VIEW;

@RequiredArgsConstructor
public class PacketSyncPaletteS2C extends BaseS2CMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":sync_palette";
    public static Int2ObjectMap<AspectResourceLocation> CLIENT_READ_ASPECT_PALETTE = ASPECT_READ_PALETTE_VIEW;
    public static Object2IntMap<AspectResourceLocation> CLIENT_WRITE_ASPECT_PALETTE = ASPECT_WRITE_PALETTE_VIEW;
    public static Int2ObjectMap<RunicShieldTypeResourceLocation> CLIENT_READ_RUNIC_SHIELD_PALETTE = RUNIC_SHIELD_READ_PALETTE_VIEW;
    public static Object2IntMap<RunicShieldTypeResourceLocation> CLIENT_WRITE_RUNIC_SHIELD_PALETTE = RUNIC_SHIELD_WRITE_PALETTE_VIEW;

    @Override
    public MessageType getType() {
        return messageType;
    }
    public final Int2ObjectMap<AspectResourceLocation> aspectPalette;
    public final Int2ObjectMap<RunicShieldTypeResourceLocation> shieldPalette;
    public PacketSyncPaletteS2C() {
        this.aspectPalette = ASPECT_READ_PALETTE_VIEW;
        this.shieldPalette = RUNIC_SHIELD_READ_PALETTE_VIEW;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        writePalette(buf, aspectPalette);
        writePalette(buf, shieldPalette);
    }

    public static PacketSyncPaletteS2C decode(FriendlyByteBuf buf) {
        return new PacketSyncPaletteS2C(
                readPalette(buf,AspectResourceLocation.BUILDER),
                readPalette(buf,RunicShieldTypeResourceLocation.BUILDER)
        );
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        CLIENT_READ_ASPECT_PALETTE = aspectPalette;
        var clientWriteAspect = new Object2IntOpenHashMap<AspectResourceLocation>();
        CLIENT_READ_ASPECT_PALETTE.forEach((key, value) -> clientWriteAspect.put(value, key));
        CLIENT_WRITE_ASPECT_PALETTE = clientWriteAspect;


        CLIENT_READ_RUNIC_SHIELD_PALETTE = shieldPalette;
        var clientWriteRunicShield = new Object2IntOpenHashMap<RunicShieldTypeResourceLocation>();
        CLIENT_READ_RUNIC_SHIELD_PALETTE.forEach((key, value) -> clientWriteRunicShield.put(value, key));
        CLIENT_WRITE_RUNIC_SHIELD_PALETTE = clientWriteRunicShield;

    }

    public static void writePalette(FriendlyByteBuf buf, Int2ObjectMap<? extends ResourceLocation> paletteToRead){
        buf.writeInt(paletteToRead.size());
        for (var entry : paletteToRead.int2ObjectEntrySet()) {
            buf.writeInt(entry.getIntKey());
            buf.writeResourceLocation(entry.getValue());
        }
    }

    public static <T extends VariedResourceLocation<?,T>> Int2ObjectMap<T> readPalette(
            FriendlyByteBuf buf,
            VariedResourceLocationBuilder<?,T> builder){
        int size = buf.readInt();
        var mapToRead = new Int2ObjectOpenHashMap<T>(size);
        for (int i = 0; i < size; i++) {
            int key = buf.readInt();
            var loc = buf.readResourceLocation();
            var value = builder.tryBuild(loc.getNamespace(),loc.getPath());
            mapToRead.put(key, value);
        }
        return mapToRead;
    }


}
