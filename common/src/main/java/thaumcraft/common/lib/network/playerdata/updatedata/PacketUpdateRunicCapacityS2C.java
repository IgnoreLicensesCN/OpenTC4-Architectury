package thaumcraft.common.lib.network.playerdata.updatedata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.common.runicshield.RunicShieldInfo;
import thaumcraft.common.runicshield.shieldtypes.AbstractRunicShieldType;

import static thaumcraft.common.lib.network.ByteBufUtil.*;

public class PacketUpdateRunicCapacityS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":runic_capacity";

    public static MessageType messageType;

    public final Object2IntMap<AbstractRunicShieldType<?>> shieldCapacity;
    public PacketUpdateRunicCapacityS2C(Object2IntMap<AbstractRunicShieldType<?>> shieldCharged) {
        this.shieldCapacity = shieldCharged;
    }

    public static void encode(PacketUpdateRunicCapacityS2C msg, FriendlyByteBuf buf) {
        writeRunicShieldsMap(buf, msg.shieldCapacity);
    }

    public static PacketUpdateRunicCapacityS2C decode(FriendlyByteBuf buf) {
        return new PacketUpdateRunicCapacityS2C(readRunicShieldsMap(buf));
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
        var info = RunicShieldInfo.getFromLiving(player);
        if (info != null) {
            info.syncCapacityClientSide(
                    this.shieldCapacity
            );
        }
    }
}
