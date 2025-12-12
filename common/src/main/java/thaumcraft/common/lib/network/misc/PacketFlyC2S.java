package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.armor.Hover;

public class PacketFlyC2S extends BaseC2SMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":set_fly";
    public static MessageType messageType;
    private boolean hover;

    public PacketFlyC2S() {}

    public PacketFlyC2S(boolean hover) {
        this.hover = hover;
    }


    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(this,buf);
    }

    public static void encode(PacketFlyC2S msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.hover);
    }

    public static PacketFlyC2S decode(FriendlyByteBuf buf) {
        return new PacketFlyC2S(buf.readBoolean());
    }
    @Override
    public void handle(NetworkManager.PacketContext context) {
        Hover.setHover(context.getPlayer().getName().toString(), hover);
    }

}
