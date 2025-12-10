package thaumcraft.common.lib.network.playerdata;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.Thaumcraft;

import java.util.Objects;

public class PacketScannedToServerC2S extends BaseC2SMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":scanned_to_server";
    public static MessageType messageType;

    private int playerId;
    private ResourceKey<Level> dim;
    private byte type;
    private int id;
    private int md;
    private int entityId;
    private String phenomena;
    private String prefix;

    // ---------------- 构造 ----------------
    public PacketScannedToServerC2S() {}

    /** 服务端发送扫描数据 */
    public PacketScannedToServerC2S(ScanResult scan, Player player, String prefix) {
        this.playerId = player.getId();
        this.dim = player.level().dimension();
        this.type = scan.type;
        this.id = scan.id;
        this.md = scan.meta;
        this.entityId = scan.entity == null ? 0 : scan.entity.getId();
        this.phenomena = scan.phenomena;
        this.prefix = prefix;
    }

    // ---------------- 编码解码 ----------------
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(playerId);
        buf.writeResourceLocation(dim.location()); // ResourceKey -> ResourceLocation
        buf.writeByte(type);
        buf.writeInt(id);
        buf.writeInt(md);
        buf.writeInt(entityId);
        buf.writeUtf(Objects.requireNonNullElse(phenomena, ""));
        buf.writeUtf(Objects.requireNonNullElse(prefix, ""));
    }

    public static PacketScannedToServerC2S decode(FriendlyByteBuf buf) {
        PacketScannedToServerC2S msg = new PacketScannedToServerC2S();
        msg.playerId = buf.readInt();

        ResourceLocation dimId = buf.readResourceLocation();
        ResourceKey<Level> dimKey = ResourceKey.create(Registries.DIMENSION, dimId);
        msg.dim = dimKey;
        msg.type = buf.readByte();
        msg.id = buf.readInt();
        msg.md = buf.readInt();
        msg.entityId = buf.readInt();
        msg.phenomena = buf.readUtf();
        msg.prefix = buf.readUtf();
        return msg;
    }

    // ---------------- 处理 ----------------
    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        Level world = player.getServer().getLevel(dim); // 通过 ResourceKey 获取世界
        if (world != null) {
            Entity e = entityId == 0 ? null : world.getEntity(entityId);
            ScanManager.completeScan(player, new ScanResult(type, id, md, e, phenomena), prefix);
        }
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
