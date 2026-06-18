package thaumcraft.common.lib.network.playerdata.syncdata;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import thaumcraft.api.warp.WarpInfo;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.Thaumcraft;

public class PacketSyncWarpS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_warp";
    public static MessageType messageType;

    public final WarpInfo data;
    public PacketSyncWarpS2C(WarpInfo warpInfo) {
        this.data = warpInfo;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(data.getTempWarp());
        buf.writeInt(data.getStickyWarp());
        buf.writeInt(data.getPermWarp());
    }

    public static PacketSyncWarpS2C decode(FriendlyByteBuf buf) {
        var data = new WarpInfo();
        data.setTempWarp(buf.readInt());
        data.setStickyWarp(buf.readInt());
        data.setPermWarp(buf.readInt());
        return new PacketSyncWarpS2C(data);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            WarpInfo.setForLivingEntity(player,data);
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
