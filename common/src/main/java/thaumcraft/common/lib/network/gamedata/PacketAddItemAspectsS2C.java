package thaumcraft.common.lib.network.gamedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.aspectlists.AspectList;
import thaumcraft.api.listeners.aspects.item.basic.getters.ItemBasicAspectGetter;
import thaumcraft.common.Thaumcraft;

import java.util.Map;

import static thaumcraft.common.lib.network.ByteBufUtil.*;


public class PacketAddItemAspectsS2C extends BaseS2CMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":add_item_aspects_result";

    @Override
    public MessageType getType() {
        return messageType;
    }

    private final Map<Item, AspectList<Aspect>> addResult;

    public PacketAddItemAspectsS2C(
            Map<Item, AspectList<Aspect>> addResult
    ) {
        this.addResult = addResult;
    }

    @Override
    public void write(FriendlyByteBuf bufOuter) {
        ITEM_ASPECTS_MAP_WRITER_SERVER_PALETTE.accept(bufOuter, addResult);
    }

    public static PacketAddItemAspectsS2C decode(FriendlyByteBuf bufOuter) {
        return new PacketAddItemAspectsS2C(ITEM_ASPECT_MAP_READER_CLIENT_PALETTE.apply(bufOuter));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ItemBasicAspectGetter.CLIENT_CACHE.putAll(addResult);

    }
}
