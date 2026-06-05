package thaumcraft.common.lib.network.playerdata;

import com.linearity.opentc4.mixinaccessors.PlayerRunicShieldInfoMixinAccessor;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import thaumcraft.common.lib.resourcelocations.RunicShieldTypeResourceLocation;
import thaumcraft.common.runicshield.RunicShieldType;

public class PacketRunicCapacityS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":runic_capacity";

    public static MessageType messageType;

    public final Object2IntMap<RunicShieldType> shieldCapacity;
    public PacketRunicCapacityS2C(Object2IntMap<RunicShieldType> shieldCharged) {
        this.shieldCapacity = shieldCharged;
    }

    public static void encode(PacketRunicCapacityS2C msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.shieldCapacity.size());
        msg.shieldCapacity.forEach((key, value) -> {
            buf.writeResourceLocation(key.id);
            buf.writeInt(value);
        });
    }

    public static PacketRunicCapacityS2C decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        Object2IntMap<RunicShieldType> decoded = new Object2IntOpenHashMap<>(size);
        for (int i = 0; i < size; i++) {
            var key = buf.readResourceLocation();
            var value = buf.readInt();
            var type = RunicShieldType.RUNIC_SHIELD_TYPES_VIEW.get(RunicShieldTypeResourceLocation.of(key));
            if (type != null) {
                decoded.put(type,value);
            }
        }
        return new PacketRunicCapacityS2C(decoded);
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
        ((PlayerRunicShieldInfoMixinAccessor)player).opentc4$getPlayerRunicShieldInfo().syncCapacityS2C(
                this.shieldCapacity
        );
    }
}
