package thaumcraft.common.lib.network.playerdata.updatedata;

import dev.architectury.networking.NetworkManager;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.runicshield.RunicShieldInfo;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import static thaumcraft.common.lib.network.ByteBufUtil.readRunicShieldsMap;
import static thaumcraft.common.lib.network.ByteBufUtil.writeRunicShieldsMap;

public class PacketUpdateRunicChargeS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":runic_charge";

    public static MessageType messageType;

    public final Object2IntMap<AbstractRunicShieldType<?>> shieldCharged;
    public PacketUpdateRunicChargeS2C(Object2IntMap<AbstractRunicShieldType<?>> shieldCharged) {
        this.shieldCharged = shieldCharged;
    }

    public static void encode(PacketUpdateRunicChargeS2C msg, FriendlyByteBuf buf) {
        writeRunicShieldsMap(buf, msg.shieldCharged);
    }

    public static PacketUpdateRunicChargeS2C decode(FriendlyByteBuf buf) {
        return new PacketUpdateRunicChargeS2C(readRunicShieldsMap(buf));
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
        RunicShieldInfo.getFromLiving(player).syncChargeClientSide(
                this.shieldCharged
        );
    }
}
