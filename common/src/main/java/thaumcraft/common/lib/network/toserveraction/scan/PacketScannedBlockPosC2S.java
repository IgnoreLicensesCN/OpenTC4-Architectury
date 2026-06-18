package thaumcraft.common.lib.network.toserveraction.scan;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import thaumcraft.common.Thaumcraft;

import static thaumcraft.api.scan.ScanManager.onScanBlockPos;

public class PacketScannedBlockPosC2S extends BaseC2SMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":scanned_block_pos_2s";
    public static MessageType messageType;

    @Override
    public MessageType getType() {
        return messageType;
    }

    private final BlockPos pos;
    public PacketScannedBlockPosC2S(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
    public static PacketScannedBlockPosC2S read(FriendlyByteBuf buf) {
        return new PacketScannedBlockPosC2S(buf.readBlockPos());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            var player = context.getPlayer();
            if (!(player instanceof ServerPlayer serverPlayer)) {return;}

            onScanBlockPos(serverPlayer, pos);
        });
    }
}
