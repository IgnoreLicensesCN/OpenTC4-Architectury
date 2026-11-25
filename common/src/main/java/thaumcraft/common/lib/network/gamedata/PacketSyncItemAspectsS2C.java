package thaumcraft.common.lib.network.gamedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.common.Thaumcraft;


public class PacketSyncItemAspectsS2C extends BaseS2CMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":sync_item_aspects_result";

    @Override
    public MessageType getType() {
        return messageType;
    }

    public PacketSyncItemAspectsS2C() {
    }

    @Override
    public void write(FriendlyByteBuf bufOuter) {
    }

    public static PacketSyncItemAspectsS2C decode(FriendlyByteBuf bufOuter) {
        return new PacketSyncItemAspectsS2C(/*readAspectReadPalette(bufOuter)*/);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ItemBasicAspectGetter.CLIENT_CACHE.clear();
//        CLIENT_READ_PALETTE = palette;
    }
}
