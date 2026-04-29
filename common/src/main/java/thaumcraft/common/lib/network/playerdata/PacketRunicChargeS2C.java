package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.events.EventHandlerRunic;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

public class PacketRunicChargeS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":runic_charge";

    public static MessageType messageType;

    public final short amount;
    public final short max;
    // 构造
    public PacketRunicChargeS2C(short amount, short max) {
        this.amount = amount;
        this.max = max;
    }

    // 解码
    public static PacketRunicChargeS2C decode(FriendlyByteBuf buf) {
        short amount = buf.readShort();
        short max = buf.readShort();
        return new PacketRunicChargeS2C(amount, max);
    }

    // 编码
    public static void encode(PacketRunicChargeS2C msg, FriendlyByteBuf buf) {
        buf.writeShort(msg.amount);
        buf.writeShort(msg.max);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encode(this, buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        EventHandlerRunic.runicCharge.put(player, (int) amount);
        EventHandlerRunic.runicInfo.put(player, new Integer[]{(int) max, 0, 0, 0, 0});
    }
}
