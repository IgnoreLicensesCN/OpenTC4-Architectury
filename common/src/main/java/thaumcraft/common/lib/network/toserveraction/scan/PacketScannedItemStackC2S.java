package thaumcraft.common.lib.network.toserveraction.scan;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.ThaumcraftBaseS2CMessage;

import static thaumcraft.api.scan.ScanManager.onPlayerScanItemStack;

public class PacketScannedItemStackC2S extends ThaumcraftBaseS2CMessage {
    public static final String ID = Thaumcraft.MOD_ID + ":scanned_stack_2s";
    public static MessageType messageType;

    @Override
    public MessageType getType() {
        return messageType;
    }

    private final ItemStack stack;
    public PacketScannedItemStackC2S(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }
    public static PacketScannedItemStackC2S read(FriendlyByteBuf buf) {
        return new PacketScannedItemStackC2S(buf.readItem());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            var player = context.getPlayer();
            if (!(player instanceof ServerPlayer serverPlayer)) {return;}

            onPlayerScanItemStack(serverPlayer, stack);
        });
    }
}
