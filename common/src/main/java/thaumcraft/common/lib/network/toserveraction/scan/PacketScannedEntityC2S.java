package thaumcraft.common.lib.network.toserveraction.scan;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;

import static thaumcraft.api.scan.ScanManager.onPlayerScanEntity;

public class PacketScannedEntityC2S extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":scanned_entity_2s";
    public static MessageType messageType;

    @Override
    public MessageType getType() {
        return messageType;
    }

    private final int entityID;
    public PacketScannedEntityC2S(int entityID) {
        this.entityID = entityID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
    }
    public static PacketScannedEntityC2S read(FriendlyByteBuf buf) {
        return new PacketScannedEntityC2S(buf.readInt());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        var player = context.getPlayer();
        if (player == null) {return;}
        var level = player.level();
        var entity = level.getEntity(entityID);
        if (entity != null){
            onPlayerScanEntity(player, entity);
        }
    }
}
