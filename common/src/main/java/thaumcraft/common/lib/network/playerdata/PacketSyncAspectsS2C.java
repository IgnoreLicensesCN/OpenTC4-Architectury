package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.AspectResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PacketSyncAspectsS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":sync_aspects";
    public static MessageType messageType;

    public AspectList<Aspect> data;

    public PacketSyncAspectsS2C(Player player) {
        this.data = Thaumcraft.playerKnowledge.getAspectsDiscovered(player.getGameProfile().getName());
    }

    /**
     * 解码用构造
     */
    public PacketSyncAspectsS2C(AspectList<Aspect> data) {
        this.data = data;
    }

    // ---------------- Architectury 必要方法 ----------------

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(
                data.aspectView,
                (aspBuf,asp) -> aspBuf.writeResourceLocation(asp.aspectKey),
                FriendlyByteBuf::writeInt
        );
    }

    public static PacketSyncAspectsS2C decode(FriendlyByteBuf buf) {
        return new PacketSyncAspectsS2C(
                new AspectList<>(
                        buf.readMap(
                                aspBuf -> Aspect.getAspect(AspectResourceLocation.of(aspBuf.readResourceLocation())),
                                FriendlyByteBuf::readInt
                        )
                )
        );
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        data.forEach(
                (asp,amount) ->Thaumcraft.researchManager.completeAspect(
                        context.getPlayer().getGameProfile().getName(), asp, amount)
        );
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

}
