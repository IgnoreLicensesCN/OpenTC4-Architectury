package thaumcraft.common.lib.network.misc;

import dev.architectury.networking.NetworkManager;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.junkbox.TileSensor;

public class PacketNoteS2C extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":note_s2c";

    public static MessageType messageType;

    private final BlockPos pos;
    private final byte note;

    public PacketNoteS2C(BlockPos pos, byte note) {
        this.pos = pos;
        this.note = note;
    }

    public static PacketNoteS2C decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        byte note = buf.readByte();
        return new PacketNoteS2C(pos, note);
    }

    public static void encode(PacketNoteS2C msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
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

        BlockPos pos = this.pos;

        if (world.getBlockState(pos).getBlock() instanceof NoteBlock) {
            world.setBlock(
                    pos,
                    world.getBlockState(pos).setValue(NoteBlock.NOTE, (int) note),
                    3
            );
            return;
        }

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TileSensor sensor) {
            sensor.note = note;
        }
    }
}
