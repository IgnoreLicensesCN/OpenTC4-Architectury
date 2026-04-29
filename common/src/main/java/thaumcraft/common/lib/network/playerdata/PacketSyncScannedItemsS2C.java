package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncScannedItemsS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_scanned_items";
    public static MessageType messageType;

    public List<String> data;

    public PacketSyncScannedItemsS2C(List<String> data) {
        this.data = data;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data.size());
        for (String s : data) {
            buf.writeUtf(s);
        }
    }

    public static PacketSyncScannedItemsS2C decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<String> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(buf.readUtf());
        }
        return new PacketSyncScannedItemsS2C(list);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {
            for (String key : data) {
                Thaumcraft.researchManager.completeScannedObject(player, key);
            }
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
