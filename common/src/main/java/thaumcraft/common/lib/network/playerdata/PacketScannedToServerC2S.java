package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import thaumcraft.api.research.scan.ScanResult;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.research.ScanManager;

import java.util.Objects;

public class PacketScannedToServerC2S extends BaseC2SMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":scanned_to_server";
    public static MessageType messageType;

    private byte type;
    private String id;
    private int entityId;
    private String phenomena;
    private String prefix;

    public PacketScannedToServerC2S() {
    }

    public PacketScannedToServerC2S(ScanResult scan, String prefix) {
        this.type = scan.type;
        this.id = scan.item.toString();
        this.entityId = scan.entity == null ? 0 : scan.entity.getId();
        this.phenomena = scan.phenomena;
        this.prefix = prefix;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte(type);
        buf.writeUtf(id);
        buf.writeInt(entityId);
        buf.writeUtf(Objects.requireNonNullElse(phenomena, ""));
        buf.writeUtf(Objects.requireNonNullElse(prefix, ""));
    }

    public static PacketScannedToServerC2S decode(FriendlyByteBuf buf) {
        PacketScannedToServerC2S msg = new PacketScannedToServerC2S();
        msg.type = buf.readByte();
        msg.id = buf.readUtf();
        msg.entityId = buf.readInt();
        msg.phenomena = buf.readUtf();
        msg.prefix = buf.readUtf();
        return msg;
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        var server = player.getServer();
        if (server == null) {
            return;
        }
        Level world = player.level();
        Entity e = entityId == 0 ? null : world.getEntity(entityId);
        ScanManager.completeScan(player, new ScanResult(type, id, e, phenomena), prefix);
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
