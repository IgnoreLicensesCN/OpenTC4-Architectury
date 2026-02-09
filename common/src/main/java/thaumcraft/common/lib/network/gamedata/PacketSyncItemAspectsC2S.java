package thaumcraft.common.lib.network.gamedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspectsS2C;

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

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            new PacketSyncItemAspectsS2C().sendTo(serverPlayer);
        }
    }
}
