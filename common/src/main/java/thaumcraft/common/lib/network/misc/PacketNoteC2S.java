package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileSensor;

public class PacketNoteC2S extends BaseC2SMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":note_c2s";

    public static MessageType messageType;

    private final BlockPos pos;
    private final ResourceKey<Level> dim;
    private final byte note; // -1 = 请求服务器返回

    public PacketNoteC2S(BlockPos pos, ResourceKey<Level> dim) {
        this(pos, dim, (byte) -1);
    }

    public PacketNoteC2S(BlockPos pos, ResourceKey<Level> dim, byte note) {
        this.pos = pos;
        this.dim = dim;
        this.note = note;
    }

    public static PacketNoteC2S decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
        byte note = buf.readByte();
        return new PacketNoteC2S(pos, dim, note);
    }

    public static void encode(PacketNoteC2S msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeResourceKey(msg.dim);
        buf.writeByte(msg.note);
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
    public void handle(NetworkManager.PacketContext ctx) {
        var player = ctx.getPlayer();
        if (player == null) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        var server = serverPlayer.getServer();
        if (server == null) return;
        Level world = server.getLevel(dim);
        if (world == null) return;

        BlockPos pos = this.pos;

        int noteOut = -1;

        // ★ 处理 NoteBlock
        if (world.getBlockState(pos).getBlock() instanceof NoteBlock) {
            noteOut = world.getBlockState(pos).getValue(NoteBlock.NOTE);
        } else {
            // ★ 处理 TileSensor
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TileSensor sensor) {
                noteOut = sensor.note;
            }
        }

        if (noteOut >= 0) {
            // 发回客户端
            new PacketNoteS2C(pos, dim, (byte) noteOut).sendTo(serverPlayer);
        }
    }
}
