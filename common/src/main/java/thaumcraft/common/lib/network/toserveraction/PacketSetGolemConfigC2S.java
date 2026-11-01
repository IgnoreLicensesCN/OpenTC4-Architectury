package thaumcraft.common.lib.network.toserveraction;

import com.linearity.opentc4.OpenTC4;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.golems.BaseGolemEntity;

public class PacketSetGolemConfigC2S extends BaseC2SMessage {
    public static MessageType messageType;
    public static String ID = Thaumcraft.MOD_ID + ":set_golem_config";

    public final CompoundTag config;
    public final int golemID;

    public PacketSetGolemConfigC2S(int golemID, CompoundTag config) {
        this.config = config;
        this.golemID = golemID;
    }

    @Override
    public MessageType getType() {
        return null;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(golemID);
        buf.writeNbt(config);
    }
    public static PacketSetGolemConfigC2S decode(FriendlyByteBuf buf) {
        return new PacketSetGolemConfigC2S(buf.readInt(),buf.readNbt());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        var entityForID = context.getPlayer().level().getEntity(golemID);
        if (!(entityForID instanceof BaseGolemEntity golem)){
            OpenTC4.LOGGER.warn("{} is not a golem--cancel config set",entityForID);
            return;
        }
        if (!golem.isOwnedBy(context.getPlayer().getUUID())) {
            OpenTC4.LOGGER.warn("golem {} is not owned by player {}--cancel config set",golem,context.getPlayer());
        }
        golem.readConfigFromTag(config);

    }
}
