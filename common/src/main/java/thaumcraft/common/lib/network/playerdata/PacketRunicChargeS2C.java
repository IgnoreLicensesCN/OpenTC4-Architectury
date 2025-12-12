package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;

public class PacketRunicChargeS2C extends BaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":runic_charge";

    public static MessageType messageType;

    public String playerName;
    public short amount;
    public short max;

    public PacketRunicChargeS2C(){}
    // 构造
    public PacketRunicChargeS2C(String playerName, short amount, short max) {
        this.playerName = playerName;
        this.amount = amount;
        this.max = max;
    }

    // 解码
    public static PacketRunicChargeS2C decode(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        short amount = buf.readShort();
        short max = buf.readShort();
        return new PacketRunicChargeS2C(name, amount, max);
    }

    // 编码
    public static void encode(PacketRunicChargeS2C msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.playerName);
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
        Player player = context.getPlayer(); // 客户端玩家
        Thaumcraft.instance.runicEventHandler.runicCharge.put(playerName, (int) amount);
        Thaumcraft.instance.runicEventHandler.runicInfo.put(playerName, new Integer[]{(int) max, 0, 0, 0, 0});
    }
}
