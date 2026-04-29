package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

public class PacketSyncWarpS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_warp";
    public static MessageType messageType;

    public final int data;
    public final byte type;

    public PacketSyncWarpS2C(int data, byte type) {
        this.data = data;
        this.type = type;
    }

    public PacketSyncWarpS2C(Player player, byte type) {
        if (type == 0)
            this.data = Thaumcraft.playerKnowledge.getWarpPerm(player);
        else if (type == 1)
            this.data = Thaumcraft.playerKnowledge.getWarpSticky(player);
        else
            this.data = Thaumcraft.playerKnowledge.getWarpTemp(player);

        this.type = type;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data);
        buf.writeByte(type);
    }

    public static PacketSyncWarpS2C decode(FriendlyByteBuf buf) {
        int data = buf.readInt();
        byte type = buf.readByte();
        return new PacketSyncWarpS2C(data, type);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        if (player != null && player.level().isClientSide) {
            if (type == 0) {
                Thaumcraft.playerKnowledge.setWarpPerm(player, data);
            } else if (type == 1) {
                Thaumcraft.playerKnowledge.setWarpSticky(player, data);
            } else {
                Thaumcraft.playerKnowledge.setWarpTemp(player, data);
            }
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
