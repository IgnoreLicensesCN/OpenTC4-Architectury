package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import thaumcraft.client.lib.PlayerNotifications;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;

public class PacketPlayerNotificationS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":player_notification";
    public static MessageType messageType;
    private final Component component;
    public PacketPlayerNotificationS2C(Component component) {
        this.component = component;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeComponent(this.component);
    }

    public static PacketPlayerNotificationS2C read(FriendlyByteBuf buf) {
        return new PacketPlayerNotificationS2C(buf.readComponent());
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerNotifications.addNotification(component);
    }
}
