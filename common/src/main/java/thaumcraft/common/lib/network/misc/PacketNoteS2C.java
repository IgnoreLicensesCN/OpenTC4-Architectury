package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileSensor;

public class PacketNoteS2C extends BaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":note_s2c";

    public static MessageType messageType;

    private BlockPos pos;
    private ResourceKey<Level> dim;
    private byte note;

    public PacketNoteS2C() {}
    public PacketNoteS2C(BlockPos pos, ResourceKey<Level> dim, byte note) {
        this.pos = pos;
        this.dim = dim;
        this.note = note;
    }

    public static PacketNoteS2C decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        ResourceKey<Level> dim = buf.readResourceKey(Registries.DIMENSION);
        byte note = buf.readByte();
        return new PacketNoteS2C(pos, dim, note);
    }

    public static void encode(PacketNoteS2C msg, FriendlyByteBuf buf) {
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
        Level world = ctx.getPlayer().level();
        if (!world.dimension().equals(dim)) return;

        BlockPos pos = this.pos;

        // ★ 客户端设置 NoteBlock 的 NOTE 属性
        if (world.getBlockState(pos).getBlock() instanceof NoteBlock) {
            world.setBlock(
                    pos,
                    world.getBlockState(pos).setValue(NoteBlock.NOTE, (int) note),
                    3
            );
            return;
        }

        // ★ 客户端同步 TileSensor
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileSensor sensor) {
            sensor.note = note;
        }
    }
}
